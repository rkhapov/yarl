import com.soywiz.korge.box2d.registerBodyWithFixture
import com.soywiz.korge.view.*
import com.soywiz.korim.atlas.readAtlas
import com.soywiz.korio.file.std.resourcesVfs
import org.jbox2d.dynamics.BodyType

class StaticObject(idleAnimation: SpriteAnimation) : Sprite(idleAnimation) {
}

suspend fun Container.staticObject(views: Views, startX: Int, startY: Int, spriteSrc: String): StaticObject {
    val sprites = resourcesVfs[spriteSrc].readAtlas()
    val animation = sprites.getSpriteAnimation("idle")

    val staticObject = StaticObject(animation).position(startX, startY)
        .registerBodyWithFixture(type = BodyType.DYNAMIC, gravityScale = 0, friction = 20f, bullet = true)
    addChild(staticObject)

    return staticObject
}