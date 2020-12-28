import com.soywiz.klock.milliseconds
import com.soywiz.korge.animate.animate
import com.soywiz.korge.box2d.BoxShape
import com.soywiz.korge.box2d.registerBodyWithFixture
import com.soywiz.korge.view.*
import com.soywiz.korim.atlas.readAtlas
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korma.geom.shape.buildPath
import org.jbox2d.dynamics.BodyType

class AggressiveCharacter(idleAnimation: SpriteAnimation) : Sprite(idleAnimation) {
    private var hp: Int = 5
    var attacking = false
    fun die() {
        removeFromParent()
    }

    fun takeDamage() {
        hp--
        if (hp == 0)
            die()
    }
}

suspend fun Container.aggressiveCharacter(views: Views, startX: Int, startY: Int, spriteSrc: String, player: Player): AggressiveCharacter {
//    val sprites = resourcesVfs[spriteSrc].readAtlas()

    val walkLeft = resourcesVfs["slime1_side.png"].readBitmap()
    val walkLeftAnimation = SpriteAnimation(spriteMap=walkLeft, spriteWidth = 16, spriteHeight = 16, columns = 4, rows = 1)
    val walkUp = resourcesVfs["slime1_back.png"].readBitmap()
    val walkUpAnimation = SpriteAnimation(spriteMap = walkUp, spriteWidth = 16, spriteHeight = 16, columns = 4, rows = 1)
    val attack = resourcesVfs["slime_explode.png"].readBitmap()
    val attackAnimation = SpriteAnimation(spriteMap = attack, spriteWidth = 37, spriteHeight = 41, columns = 8, rows = 1)

    val character = AggressiveCharacter(walkLeftAnimation).position(startX, startY).registerBodyWithFixture(type= BodyType.STATIC, gravityScale = 0, shape = BoxShape(2f, 2f))
    addChild(character)

    character.playAnimationLooped(attackAnimation, spriteDisplayTime = 200.milliseconds)

    onCollision {
        if (it is Player)
            it.takeDamage()
    }

    return character
}