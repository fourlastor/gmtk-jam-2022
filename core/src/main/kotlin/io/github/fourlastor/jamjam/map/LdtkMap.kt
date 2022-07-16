package io.github.fourlastor.jamjam.map

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.maps.Map
import com.badlogic.gdx.maps.MapLayer

class LdtkMap(
    val layers: List<LdtkMapLayer>,
) : Map()

class LdtkMapLayer(
    val tiles: List<Sprite>,
) : MapLayer()
