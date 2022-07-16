package io.github.fourlastor.jamjam.map

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import io.github.fourlastor.jamjam.DiceonGame
import io.github.fourlastor.jamjam.loader.LoaderScreen
import ktx.app.KtxScreen
import ktx.assets.async.AssetStorage

class MapScreen(
    assets: Assets,
) : KtxScreen {

    init {
        Gdx.app.debug("MapScreen", "Init $assets")
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
                            removeScreen<LoaderScreen<Assets>>()
                        }
                    }
                )
            )
            game.setScreen<LoaderScreen<Assets>>()
        }
    }
}

data class Assets(
    val textureAtlas: TextureAtlas,
) {
    companion object {
        suspend fun load(assetStorage: AssetStorage) = Assets(
            textureAtlas = assetStorage.load("atlases/dungeon.atlas")
        )
    }
}
