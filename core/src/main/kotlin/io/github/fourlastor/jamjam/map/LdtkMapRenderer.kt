package io.github.fourlastor.jamjam.map

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.maps.MapRenderer
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.utils.viewport.FitViewport
import ktx.graphics.use

class LdtkMapRenderer(
    private val batch: SpriteBatch,
    private val viewport: FitViewport,
    private val map: LdtkMap,
) : MapRenderer {

    override fun setView(camera: OrthographicCamera) {

    }

    override fun setView(
        projectionMatrix: Matrix4?,
        viewboundsX: Float,
        viewboundsY: Float,
        viewboundsWidth: Float,
        viewboundsHeight: Float
    ) {
//        TODO("Not yet implemented")
    }

    override fun render() {
        viewport.apply()
        batch.projectionMatrix = viewport.camera.combined
        map.layers.forEach { layer ->
            layer.tiles.forEach { it.draw(batch) }
        }
    }

    override fun render(layers: IntArray) {
        TODO("Not yet implemented")
    }
}
