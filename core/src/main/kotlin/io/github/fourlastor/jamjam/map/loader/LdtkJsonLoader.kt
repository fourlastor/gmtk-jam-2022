package io.github.fourlastor.jamjam.map.loader

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetLoaderParameters
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.FileHandleResolver
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.utils.Array
import io.github.fourlastor.ldtk.LDtkMapData
import io.github.fourlastor.ldtk.LDtkReader

class LdtkJsonLoader(
    resolver: FileHandleResolver,
) : SynchronousAssetLoader<LDtkMapData, LdtkJsonLoader.Parameters>(
    resolver
) {
    private val reader = LDtkReader()

    override fun getDependencies(
        fileName: String,
        file: FileHandle,
        parameter: Parameters?
    ): Array<AssetDescriptor<*>> = Array()

    override fun load(
        assetManager: AssetManager,
        fileName: String,
        file: FileHandle,
        parameter: Parameters?
    ): LDtkMapData = reader.data(file.read())

    class Parameters : AssetLoaderParameters<LDtkMapData>()
}
