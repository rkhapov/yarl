import com.soywiz.korge.box2d.BoxShape
import com.soywiz.korge.box2d.registerBodyWithFixture
import com.soywiz.korge.view.*
import com.soywiz.korim.atlas.readAtlas
import com.soywiz.korio.file.std.resourcesVfs
import org.jbox2d.dynamics.BodyType

class AgressiveCharacter(idleAnimation: SpriteAnimation) : Sprite(idleAnimation) {
    private var hp: Int = 5
    fun die() {
        removeFromParent()
    }

    fun takeDamage() {
        hp--
    }
}

suspend fun Container.agressiveCharacter(views: Views, startX: Int, startY: Int, spriteSrc: String) {
    val sprites = resourcesVfs[spriteSrc].readAtlas()

    val idleAnimation = sprites.getSpriteAnimation("idle")
    val walkRightAnimation = sprites.getSpriteAnimation("walk-right")
    val walkLeftAnimation = sprites.getSpriteAnimation("walk-left")
    val walkUpAnimation = sprites.getSpriteAnimation("walk-up")
    val walkDownAnimation = sprites.getSpriteAnimation("walk-down")
    val attackAnimation = sprites.getSpriteAnimation("attack")

    val character = AgressiveCharacter(idleAnimation).position(startX, startY).registerBodyWithFixture(type= BodyType.STATIC, gravityScale = 0, shape = BoxShape(2f, 2f))

}