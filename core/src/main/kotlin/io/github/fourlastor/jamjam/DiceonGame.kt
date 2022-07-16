package io.github.fourlastor.jamjam

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.kotcrab.vis.ui.VisUI
import io.github.fourlastor.jamjam.level.LevelScreen
import io.github.fourlastor.jamjam.map.loader.LdtkMapLoader
import io.github.fourlastor.jamjam.map.MapScreen
import io.github.fourlastor.jamjam.map.loader.LdtkJsonLoader
import io.github.fourlastor.ldtk.Definitions
import io.github.fourlastor.ldtk.LDtkLevelDefinition
import ktx.app.KtxGame
import ktx.assets.async.AssetStorage
import ktx.async.KtxAsync
import ktx.scene2d.Scene2DSkin

class DiceonGame : KtxGame<Screen>() {

    val assetStorage = AssetStorage().apply {
        setLoader(suffix = ".ldtk") { LdtkMapLoader(fileResolver) }
        setLoader(suffix = ".ldtk") { LdtkJsonLoader(fileResolver) }
    }

    override fun create() {
        VisUI.load(VisUI.SkinScale.X2)
        Scene2DSkin.defaultSkin = VisUI.getSkin()
        KtxAsync.initiate()
        Gdx.app.logLevel = Application.LOG_DEBUG
        MapScreen.launch(this)
    }

    override fun dispose() {
        super.dispose()
        assetStorage.dispose()
    }

    fun startGame(levelDefinition: LDtkLevelDefinition, defs: Definitions) {
        clearLevel()
        addScreen(LevelScreen(levelDefinition, defs))
        setScreen<LevelScreen>()
    }

    inline fun <reified Type : Screen> clearScreen() {
        removeScreen(Type::class.java)?.dispose()
    }

    private fun clearLevel() {
        if (containsScreen<LevelScreen>()) {
            removeScreen<LevelScreen>()
        }
    }
}
