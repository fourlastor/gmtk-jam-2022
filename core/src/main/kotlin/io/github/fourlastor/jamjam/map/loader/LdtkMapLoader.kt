package io.github.fourlastor.jamjam.map.loader

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.utils.Array
import io.github.fourlastor.jamjam.map.LdtkMap
import io.github.fourlastor.ldtk.LDtkMapData

class LdtkMapLoader(
    resolver: FileHandleResolver,
) : SynchronousAssetLoader<LdtkMap, LdtkMapLoader.Parameters>(
    resolver
) {

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
        return LdtkMap(assetManager.get(fileName, LDtkMapData::class.java))
    }

    class Parameters : AssetLoaderParameters<LdtkMap>()
}
