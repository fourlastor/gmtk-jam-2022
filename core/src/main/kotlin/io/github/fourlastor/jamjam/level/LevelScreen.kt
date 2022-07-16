package io.github.fourlastor.jamjam.level

import com.artemis.WorldConfigurationBuilder
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import io.github.fourlastor.jamjam.AssetFactory
import io.github.fourlastor.jamjam.level.system.RenderSystem
import io.github.fourlastor.ldtk.Definitions
import io.github.fourlastor.ldtk.LDtkLevelDefinition
import ktx.actors.onChange
import ktx.app.KtxScreen
import ktx.box2d.createWorld
import ktx.graphics.center
import ktx.scene2d.actors
import ktx.scene2d.vis.visLabel
import ktx.scene2d.vis.visTable
import ktx.scene2d.vis.visTextButton
import ktx.scene2d.vis.visTextField

class LevelScreen(
    levelDefinition: LDtkLevelDefinition,
    definitions: Definitions
) : KtxScreen {

    private val scale = 1f / 16f
    private val factory = AssetFactory(scale)
    private val converter = LDtkConverter(factory)
    private val level = converter.convert(levelDefinition, definitions)

    private val camera = OrthographicCamera().apply {
        setToOrtho(true)
    }
    private val viewport = FitViewport(16f, 10f, camera).also {
        camera.center(it.worldWidth, it.worldHeight)
    }

    private val box2dWorld = createWorld(gravity = Vector2(0f, 10f))

    private val debug = true

    private val defaultRunSpeed = 4f
    private val defaultJumpSpeed = 6f
    private val defaultJumpMaxHeight = 3.5f
    private val defaultGraceTime = 250f / 1000f


    private val world = WorldConfigurationBuilder().with(
        RenderSystem(camera = camera),
    )
        .build()
        .let { com.artemis.World(it) }

    private val stage = Stage()

    init {

        if (debug) {
            stage.actors {
                visTable(defaultSpacing = true) {
                    padTop(8f)
                    setFillParent(true)
                    defaults()
                        .expandY()
                        .top()
                    visLabel("Run speed:")
                    val runSpeedField = visTextField("$defaultRunSpeed")
                    visLabel("Jump speed:")
                    val jumpSpeedField = visTextField("$defaultJumpSpeed")
                    visLabel("Jump height:")
                    val jumpHeightField = visTextField("$defaultJumpMaxHeight")
                    visLabel("Grace s:")
                    val graceTimeField = visTextField("$defaultGraceTime")
                    visTextButton("Update") {
                        onChange {
//                            runSpeed = (runSpeedField.text.toFloatOrNull() ?: runSpeed)
//                            jumpSpeed = (jumpSpeedField.text.toFloatOrNull() ?: jumpSpeed)
//                            jumpMaxHeight = (jumpHeightField.text.toFloatOrNull() ?: jumpMaxHeight)
//                            graceTime = (graceTimeField.text.toFloatOrNull() ?: graceTime)
                        }
                    }
                    pack()
                }
            }
        }
    }

    override fun show() {
        Gdx.input.inputProcessor = InputMultiplexer(
            stage,
        )
    }

    override fun hide() {
        Gdx.input.inputProcessor = null
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, false)
        stage.viewport.update(width, height, true)
    }

    override fun render(delta: Float) {
        world.setDelta(delta)
        world.process()
        stage.act(delta)
        stage.draw()
    }

    override fun dispose() {
        world.dispose()
        box2dWorld.dispose()
        factory.dispose()
        stage.dispose()
    }
}
