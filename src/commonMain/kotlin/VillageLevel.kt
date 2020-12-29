import com.soywiz.klock.milliseconds
import com.soywiz.klock.seconds
import com.soywiz.korge.input.onClick
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.tiled.readTiledMap
import com.soywiz.korge.tiled.tiledMapView
import com.soywiz.korge.time.timeout
import com.soywiz.korge.view.*
import com.soywiz.korim.atlas.readAtlas
import com.soywiz.korim.color.Colors
import com.soywiz.korio.file.std.resourcesVfs

class VillageLevel : Scene() {
    override suspend fun Container.sceneInit() {
        val map = resourcesVfs["/village/demo.tmx"].readTiledMap()
        val boomSprites = resourcesVfs["boom.xml"].readAtlas()

        val boomAnimation = boomSprites.getSpriteAnimation("boom")
        camera {
            val mapView = tiledMapView(map)
            mapView.scale(2)
            character(views, 400, 200, "test_animations.xml", "Отличная погодка сегодня", Colors.WHITESMOKE)
            character(views, 300, 200, "test_animations.xml", "Опять убирать хлев...", Colors.WHITESMOKE)
            character(views, 200, 300, "test_animations.xml", "Пойду нажрусь в баре", Colors.WHITESMOKE)
            character(views, 350, 400, "test_animations.xml", "Пам-парам", Colors.WHITESMOKE)
            character(views, 150, 200, "test_animations.xml", "БЛЭК ЛАЙВС МЭТТА", Colors.WHITESMOKE)

            val boom = sprite(boomAnimation).visible(false).scale(2.5).centerOnStage()
            text("Наш герой", textSize = 20.0).centerOnStage()

            text("Наш герой жил в обычной деревне где-то у моря", textSize = 35.0, color = Colors.WHITESMOKE).position(
                50.0,
                200.0
            )
            timeout(5.seconds) {
                text(
                    "Дела шли как обычно, ничего не предвещало беды",
                    textSize = 35.0,
                    color = Colors.WHITESMOKE
                ).position(30, 240)
                timeout(5.seconds) {
                    text("Пока не наступил 2020!", textSize = 35.0, color = Colors.DARKRED).position(250, 280)
                    timeout(3.seconds) {
                        boom.visible = true
                        boom.playAnimation(boomAnimation, spriteDisplayTime = 100.milliseconds)
                        text("Кликните, чтобы продолжить", textSize = 35.0, color = Colors.WHITESMOKE).position(
                            150,
                            320
                        )
                    }
                }
            }
        }

        onClick { sceneContainer.changeTo<SnowLevel>() }
    }
}