import com.soywiz.klock.infiniteTimes
import com.soywiz.klock.seconds
import com.soywiz.korau.sound.PlaybackTimes
import com.soywiz.korau.sound.await
import com.soywiz.korau.sound.readMusic
import com.soywiz.korau.sound.readSound
import com.soywiz.korge.box2d.registerBodyWithFixture
import com.soywiz.korge.input.onClick
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.tiled.readTiledMap
import com.soywiz.korge.tiled.tiledMapView
import com.soywiz.korge.time.timeout
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korio.file.std.resourcesVfs
import org.jbox2d.dynamics.BodyType

class SnowLevel: Scene() {
    var enemiesDefeated = false
    override suspend fun Container.sceneInit() {
        val map = resourcesVfs["/snow_level/snow.tmx"].readTiledMap()

        val rectDoor1 = solidRect(96, 32).position(128, 224)
        val rectDoor2 = solidRect(96, 32).position(352, 160)
        val rectDoor3 = solidRect(96, 32).position(608, 128)
        val mapView = tiledMapView(map)

        val jesus = character(views, 160, 224, "jesus.xml","Я родился", Colors.BLACK).visible(false)
        val santa = character(views, 384, 160, "santa.xml", "Йо-хо-хо, спасибо за спасение", Colors.BLACK).visible(false)
        val elf1 = character(views, 608, 128, "elf.xml", "Гномье бурчанье 1", Colors.BLACK).visible(false)
        val elf2 = character(views, 640, 128, "elf.xml", "Гномье бурчанье 2", Colors.BLACK).visible(false)
        val elf3 = character(views, 672, 128, "elf.xml", "Гномье бурчанье 3", Colors.BLACK).visible(false)

        jesus.stop()
        santa.stop()
        elf1.stop()
        elf2.stop()
        elf3.stop()

        character(views, 500, 500, "deer.xml", "Я олень, я не умею говорить!", Colors.BLACK)
        character(views, 50, 300, "deer.xml", "Я олень, я не умею говорить!", Colors.BLACK)
        val player = player(views, 0, 500, mapView)
        val aggressiveCharacter = aggressiveCharacter(views, 600, 200, "src", player, mapView, 32, 32)
        aggressiveCharacter(views, 400, 200, "src", player, mapView, 32, 32)
        aggressiveCharacter(views, 300, 200, "src", player, mapView, 32, 32)
        aggressiveCharacter(views, 200, 200, "src", player, mapView, 32, 32)

        val firstDoorBlock1 = staticObject(views, 128, 224, "gift.xml")
        val firstDoorBlock2 = staticObject(views, 160, 224, "gift.xml")
        val firstDoorBlock3 = staticObject(views, 192, 224, "gift.xml")

        val secondDoorBlock1 = staticObject(views, 352, 160, "gift.xml")
        val secondDoorBlock2 = staticObject(views, 384, 160, "gift.xml")
        val secondDoorBlock3 = staticObject(views, 416, 160, "gift.xml")

        val thirdDoorBlock1 = staticObject(views, 608, 128, "gift.xml")
        val thirdDoorBlock2 = staticObject(views, 640, 128, "gift.xml")
        val thirdDoorBlock3 = staticObject(views, 672, 128, "gift.xml")

        val firstDoorBlockingObjects = listOf<View>(firstDoorBlock1, firstDoorBlock2, firstDoorBlock3)
        val secondDoorBlockingObjects = listOf<View>(secondDoorBlock1, secondDoorBlock2, secondDoorBlock3)
        val thirdDoorBlockingObjects = listOf<View>(thirdDoorBlock1, thirdDoorBlock2, thirdDoorBlock3)

        var playerText = text("Что это?", textSize = 16.0, color = Colors.BLACK).position(player.pos)
        timeout(3.seconds) {
            playerText.text = "Я умер?"
            timeout(2.seconds) {
                playerText.text = "Похоже, что я на северном полюсе"
                timeout(3.seconds) {
                    playerText.text = "Причем в самый канун Нового года"
                    timeout(3.seconds) {
                        playerText.text = "Но где все? Здесь лишь зеленые штуки..."
                        timeout(3.seconds) {
                            playerText.text = "Может эти... вирусы стали причиной такой пустоты"
                            timeout(4.seconds) {
                                playerText.text = "Я думаю, стоит от них избавиться"
                                timeout(4.seconds) {
                                    playerText.visible(false)
                                }
                            }
                        }
                    }
                }
            }
        }

        val aggressiveCharacters = ArrayList<View>()
        forEachChildren {
            if (it is AggressiveCharacter) {
                aggressiveCharacters.add(it)
            }
        }

        var isFirstTaskDone = false
        var isSecondTaskDone = false
        var isThirdTaskDone = false
        var isFourthTaskDone = false

        onClick {
            if (isFirstTaskDone && isSecondTaskDone && isThirdTaskDone && isFourthTaskDone)
                    sceneContainer.changeTo<FinalLevel>()
        }

        addUpdater {
            if (children.intersect(aggressiveCharacters).isEmpty()) {
                playerText.text = "Теперь нужно расчистить завалы у домов"
                playerText.visible(true)
                isFirstTaskDone = true
            }

            playerText.position(player.pos)

            if (!rectDoor1.collidesWith(firstDoorBlockingObjects)) {
                playerText.text = "Этот завал расчищен"
                jesus.visible = true
                jesus.move()
                isSecondTaskDone = true
            }

            if (!rectDoor2.collidesWith(secondDoorBlockingObjects)) {
                playerText.text = "Санта свободен!"
                santa.visible = true
                santa.move()
                isThirdTaskDone = true
            }

            if (!rectDoor3.collidesWith(thirdDoorBlockingObjects)) {
                playerText.text = "Теперь жители этого дома свободны"
                elf1.visible = true
                elf1.move()

                elf2.visible = true
                elf2.move()

                elf3.visible = true
                elf3.move()
                isFourthTaskDone = true
            }

            if (isFirstTaskDone && isSecondTaskDone && isThirdTaskDone && isFourthTaskDone)
                text("Чтобы продолжить кликните левой кнопкой мыши", textSize = 30.0, color = Colors.BLACK).centerOnStage()
        }
    }
}