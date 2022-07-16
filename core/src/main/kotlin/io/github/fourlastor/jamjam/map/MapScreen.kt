package io.github.fourlastor.jamjam.map

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.utils.viewport.FitViewport
import io.github.fourlastor.jamjam.DiceonGame
import io.github.fourlastor.jamjam.loader.LoaderScreen
import ktx.app.KtxScreen
import ktx.assets.async.AssetStorage
import ktx.graphics.use
import java.lang.Float.min


class MapScreen(
    assets: Assets,
) : KtxScreen {

    private val mapViewport = FitViewport(10f, 10f).apply {
        camera.position.set(5f, 13f, 0f)
    }
    private val spriteBatch = SpriteBatch()

    private val renderer = LdtkMapRenderer(spriteBatch, mapViewport, assets.map)

    override fun resize(width: Int, height: Int) {
        val scale = min(width.toFloat() / 320f, height.toFloat() / 180f)
        mapViewport.setScreenBounds(
            ((width/2)-80*scale).toInt(),
            ((height/2)-80*scale).toInt(),
            (160*scale).toInt(),
            (160*scale).toInt(),
        )
    }

    override fun render(delta: Float) {
        super.render(delta)

        spriteBatch.use {
            renderer.render()
        }
    }

    companion object {
        fun launch(
            game: DiceonGame
        ) {
            game.addScreen(
                LoaderScreen(
                    assetStorage = game.assetStorage,
                    loadAssets = { Assets.load(it) },
                    goToNextScreen = {
                        game.apply {
                            addScreen(MapScreen(it))
                            setScreen<MapScreen>()
                            clearScreen<LoaderScreen<Assets>>()
                        }
                    }
                )
            )
            game.setScreen<LoaderScreen<Assets>>()
        }
    }
}

data class Assets(
    val map: LdtkMap,
) {
    companion object {
        suspend fun load(assetStorage: AssetStorage) = Assets(
            map = assetStorage.load("diceon.ldtk"),
        )
    }
}
