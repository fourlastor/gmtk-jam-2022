package io.github.fourlastor.jamjam.loader

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Stage
import com.kotcrab.vis.ui.widget.VisProgressBar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ktx.app.KtxScreen
import ktx.assets.async.AssetStorage
import ktx.async.KtxAsync
import ktx.scene2d.actors
import ktx.scene2d.vis.visProgressBar
import ktx.scene2d.vis.visTable

class LoaderScreen<Assets>(
    private val assetStorage: AssetStorage,
    private val loadAssets: suspend (AssetStorage) -> Assets,
    private val goToNextScreen: (Assets) -> Unit,
): KtxScreen {

    private val stage = Stage()
    private val progressBar: VisProgressBar

    init {
        stage.actors {
            visTable(defaultSpacing = true) {
                setFillParent(true)
                progressBar = visProgressBar(min = 0f, max = 1f)
            }
        }
    }

    override fun show() {
        KtxAsync.launch {
            assetStorage.apply {
                val minTime = launch { delay(MIN_DELAY_MS) }
                val assets = loadAssets(this)
                minTime.join()
                goToNextScreen(assets)
            }
        }
        Gdx.input.inputProcessor = stage
    }

    override fun hide() {
        super.hide()
        Gdx.input.inputProcessor = null
    }

    override fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    override fun render(delta: Float) {
        super.render(delta)
        val progress = assetStorage.progress

        progressBar.value = progress.percent

        stage.act()
        stage.draw()
    }

    override fun dispose() {
        super.dispose()
        stage.dispose()
    }

    companion object {
        private const val MIN_DELAY_MS = 700L
    }
}
