import com.soywiz.klock.seconds
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.tiled.readTiledMap
import com.soywiz.korge.tiled.tiledMapView
import com.soywiz.korge.time.timeout
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korio.file.std.resourcesVfs

class FinalLevel: Scene() {
    override suspend fun Container.sceneInit() {
        val map = resourcesVfs["/village/demo.tmx"].readTiledMap()
        camera {
            val mapView = tiledMapView(map)
            mapView.scale(2)
            character(views, 400, 200, "test_animations.xml", "Отличная погодка сегодня", Colors.WHITESMOKE)
            character(views, 300, 200, "test_animations.xml", "Опять убирать хлев...", Colors.WHITESMOKE)
            character(views, 200, 300, "test_animations.xml", "Пойду нажрусь в баре", Colors.WHITESMOKE)
            character(views, 350, 400, "test_animations.xml", "Пам-парам", Colors.WHITESMOKE)
            character(views, 150, 200, "test_animations.xml", "БЛЭК ЛАЙВС МЭТТА", Colors.WHITESMOKE)
            player(views, 0, 400, mapView, scale = 2)

            val text = text("Наш герой вернуться в мир живых!", textSize = 35.0, color = Colors.WHITESMOKE).position(50.0, 200.0)
            timeout(3.seconds) {
                text.text = "И благодаря ему Новый год все-таки наступит"
                timeout(3.seconds) {
                    text.visible = false
                }
            }
        }
    }
}