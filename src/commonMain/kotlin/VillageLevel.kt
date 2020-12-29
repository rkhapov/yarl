import com.soywiz.klock.milliseconds
import com.soywiz.klock.seconds
import com.soywiz.klock.timesPerSecond
import com.soywiz.korev.Key
import com.soywiz.korge.animate.animate
import com.soywiz.korge.animate.launchAnimate
import com.soywiz.korge.box2d.registerBodyWithFixture
import com.soywiz.korge.input.onClick
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.tiled.readTiledMap
import com.soywiz.korge.tiled.tiledMapView
import com.soywiz.korge.time.timeout
import com.soywiz.korge.time.timers
import com.soywiz.korge.view.*
import com.soywiz.korge.view.camera.CameraContainer
import com.soywiz.korge.view.camera.CameraOld
import com.soywiz.korge.view.camera.cameraContainer
import com.soywiz.korge.view.camera.cameraContainerOld
import com.soywiz.korge.view.tween.moveBy
import com.soywiz.korim.atlas.readAtlas
import com.soywiz.korim.color.Colors
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korma.geom.Rectangle
import org.jbox2d.dynamics.BodyType

class VillageLevel : Scene() {
    override suspend fun Container.sceneInit() {
        val map = resourcesVfs["/village/demo.tmx"].readTiledMap()
        val boomSprites = resourcesVfs["boom.xml"].readAtlas()

        val boomAnimation = boomSprites.getSpriteAnimation("boom")

        lateinit var player: Sprite
//        val container = cameraContainer(800.0, 800.0)
        camera {
            val mapView = tiledMapView(map)
            mapView.scale(2)
            character(views, 400, 200, "test_animations.xml", "Отличная погодка сегодня")
            character(views, 300, 200, "test_animations.xml", "Опять убирать хлев...")
            character(views, 200, 300, "test_animations.xml", "Пойду нажрусь в баре")
            character(views, 350, 400, "test_animations.xml", "Пам-парам")
            character(views, 150, 200, "test_animations.xml", "БЛЭК ЛАЙВС МЭТТА")
            player = player(views, 0,400, mapView, scale=2)

            val boom = sprite(boomAnimation).visible(false).scale(2.5).centerOnStage()
            text("Наш герой", textSize = 20.0).centerOnStage()

            text("Наш герой жил в обычной деревне где-то у моря", textSize = 35.0, color = Colors.WHITESMOKE).position(50.0, 200.0)
            timeout(5.seconds) {
                text("Дела шли как обычно, ничего не предвещало беды", textSize = 35.0, color = Colors.WHITESMOKE).position(30, 240)
                timeout(5.seconds) {
                    text("Пока не наступил 2020!", textSize = 35.0,  color = Colors.DARKRED).position(250, 280)
                    timeout(3.seconds) {
                        boom.visible = true
                        boom.playAnimation(boomAnimation, spriteDisplayTime = 100.milliseconds)
                        text("Кликните, чтобы продолжить", textSize = 35.0, color = Colors.WHITESMOKE).position(150, 320)
                    }
                }
            }
        }

        onClick { sceneContainer.changeTo<SnowLevel>() }

//        container.setZoomAt(player.anchorX, player.anchorY, 2.0)
//        container.setAnchorPosKeepingPos(player.)
//        container.follow(player)

    }
}