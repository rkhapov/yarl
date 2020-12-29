import com.soywiz.klock.seconds
import com.soywiz.korge.box2d.registerBodyWithFixture
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.tiled.readTiledMap
import com.soywiz.korge.tiled.tiledMapView
import com.soywiz.korge.time.timeout
import com.soywiz.korge.view.*
import com.soywiz.korim.color.Colors
import com.soywiz.korio.file.std.resourcesVfs
import org.jbox2d.dynamics.BodyType

class SnowLevel: Scene() {
    override suspend fun Container.sceneInit() {
        val map = resourcesVfs["/snow_level/snow.tmx"].readTiledMap()

        val rectDoor1 = solidRect(96, 32).position(128, 224)
        val rectDoor2 = solidRect(96, 32).position(352, 160)
        val rectDoor3 = solidRect(96, 32).position(608, 128)
        val mapView = tiledMapView(map)

        val jesus = character(views, 160, 224, "jesus.xml").visible(false)
        val santa = character(views, 384, 160, "santa.xml").visible(false)
        val elf1 = character(views, 608, 128, "elf.xml").visible(false)
        val elf2 = character(views, 640, 128, "elf.xml").visible(false)
        val elf3 = character(views, 672, 128, "elf.xml").visible(false)

        character(views, 600, 500, "deer.xml")
        character(views, 50, 300, "deer.xml")
        val aggressiveCharacter = aggressiveCharacter(views, 600, 200, "src")
        val player = player(views, 0, 500, mapView)

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
                playerText.text = "Эти зеленые вирусы не дают начаться новому году!"
                timeout(4.seconds) {
                    playerText.text = "Я должен их уничтожить"
                }
            }
        }

        addUpdater {
            playerText.position(player.pos)
            if (!rectDoor1.collidesWith(firstDoorBlockingObjects)) {
//                jesus.position(160, 224)
                jesus.visible = true
            }

            if (!rectDoor2.collidesWith(secondDoorBlockingObjects)) {
//                santa.position(384, 160)
                santa.visible = true
            }

            if (!rectDoor3.collidesWith(thirdDoorBlockingObjects)) {
//                elf1.position(608, 128)
                elf1.visible = true

//                elf2.position(640, 128)
                elf2.visible = true

//                elf3.position(672, 128)
                elf3.visible = true
            }
        }
    }
}