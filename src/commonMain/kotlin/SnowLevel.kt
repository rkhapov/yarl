import com.soywiz.korge.scene.Scene
import com.soywiz.korge.tiled.readTiledMap
import com.soywiz.korge.tiled.tiledMapView
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.camera
import com.soywiz.korio.file.std.resourcesVfs

class SnowLevel: Scene() {
    override suspend fun Container.sceneInit() {
        val map = resourcesVfs["/snow_level/snow.tmx"].readTiledMap()

        camera {
            val mapView = tiledMapView(map)
            val player = player(views, 0, 500, mapView)
            character(views, 600, 500, "deer.xml")
            character(views, 50, 300, "deer.xml")
            val aggressiveCharacter = aggressiveCharacter(views, 600, 200, "src", player)
        }
    }
}