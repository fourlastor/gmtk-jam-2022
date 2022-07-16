package io.github.fourlastor.jamjam

import com.badlogic.gdx.Application
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.kotcrab.vis.ui.VisUI
import io.github.fourlastor.jamjam.level.LevelScreen
import io.github.fourlastor.jamjam.map.MapScreen
import io.github.fourlastor.ldtk.Definitions
import io.github.fourlastor.ldtk.LDtkLevelDefinition
import io.github.fourlastor.ldtk.LDtkReader
import ktx.app.KtxGame
import ktx.assets.async.AssetStorage
import ktx.async.KtxAsync
import ktx.scene2d.Scene2DSkin

class DiceonGame : KtxGame<Screen>() {

    private val reader = LDtkReader()
    val assetStorage = AssetStorage()

    override fun create() {
        VisUI.load(VisUI.SkinScale.X2)
        Scene2DSkin.defaultSkin = VisUI.getSkin()
        KtxAsync.initiate()
        Gdx.app.logLevel = Application.LOG_DEBUG
        MapScreen.launch(this)
//        val gameData = reader.data(Gdx.files.internal("diceon.ldtk").read())
//
//        addScreen(MenuScreen(this, gameData))
//        setScreen<MenuScreen>()
//        startGame(
//            gameData.levelDefinitions[0],
//            gameData.defs,
//        )
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

    fun goToMenu() {
        clearLevel()
        setScreen<MenuScreen>()
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
