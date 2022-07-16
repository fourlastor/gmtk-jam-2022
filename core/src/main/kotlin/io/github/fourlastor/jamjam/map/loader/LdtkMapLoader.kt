package io.github.fourlastor.jamjam.map.loader

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.utils.Array
import io.github.fourlastor.jamjam.map.LdtkMap
import io.github.fourlastor.jamjam.map.LdtkMapLayer
import io.github.fourlastor.ldtk.Definitions
import io.github.fourlastor.ldtk.LDtkLayerInstance
import io.github.fourlastor.ldtk.LDtkMapData
import io.github.fourlastor.ldtk.LDtkTileInstance

class LdtkMapLoader(
    resolver: FileHandleResolver,
) : SynchronousAssetLoader<LdtkMap, LdtkMapLoader.Parameters>(
    resolver
) {

    private val scale = 1f / 16f

    override fun getDependencies(
        fileName: String,
        file: FileHandle,
        parameter: Parameters?
    ): Array<AssetDescriptor<*>> {
        return Array(
            arrayOf(
                AssetDescriptor("atlases/dungeon.atlas", TextureAtlas::class.java),
                AssetDescriptor(fileName, LDtkMapData::class.java),
            )
        )
    }

    override fun load(
        assetManager: AssetManager,
        fileName: String,
        file: FileHandle,
        parameter: Parameters?
    ): LdtkMap {
        val atlas = assetManager.get("atlases/dungeon.atlas", TextureAtlas::class.java)
        val data = assetManager.get(fileName, LDtkMapData::class.java)
        val levelDefinition = data.levelDefinitions[0]
        val layerInstances = levelDefinition.layerInstances.orEmpty()
        val layers = layerInstances.mapNotNull { it.toLayer(data.defs, atlas) }.reversed()
        return LdtkMap(layers)
    }

    private fun LDtkLayerInstance.toLayer(
        defs: Definitions,
        atlas: TextureAtlas
    ): LdtkMapLayer? = when (type) {
        "AutoLayer" -> defs.tilesets.find { it.uid == tilesetDefUid }
            ?.let { tileset ->
                val gridHeight = cHei
                LdtkMapLayer(
                    autoLayerTiles.mapNotNull { tile ->
                        tile.t
                            .let { tileId -> tileset.customData.find { it.tileId == tileId } }
                            ?.let {
                                tile(
                                    atlas,
                                    gridHeight,
                                    it.data,
                                    tile.x,
                                    tile.y,
                                    tile.flipX,
                                    tile.flipY
                                )
                            }
                    }
                ).also {
                    it.name = this.identifier
                }
            }

        else -> null
    }

    private fun tile(
        atlas: TextureAtlas,
        gridHeight: Int,
        name: String,
        x: Float,
        y: Float,
        flipX: Boolean,
        flipY: Boolean
    ): Sprite {
        return atlas.createSprite(name)
            .scaleAtOrigin()
            .apply {
                setPosition(x * scale, gridHeight - y * scale)
                flip(flipX, flipY)
            }
    }


    private fun Sprite.scaleAtOrigin() = apply {
        setOrigin(0f, 0f)
        setScale(scale)
    }

    class Parameters : AssetLoaderParameters<LdtkMap>()
}


private val LDtkTileInstance.x: Float
    get() = px[0].toFloat()
private val LDtkTileInstance.y: Float
    get() = px[1].toFloat()
private val LDtkTileInstance.flipX: Boolean
    get() = f and 1 == 1
private val LDtkTileInstance.flipY: Boolean
    get() = f shr 1 and 1 == 1
