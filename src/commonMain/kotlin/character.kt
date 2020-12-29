import com.soywiz.klock.milliseconds
import com.soywiz.klock.seconds
import com.soywiz.korge.box2d.BoxShape
import com.soywiz.korge.box2d.registerBodyWithFixture
import com.soywiz.korge.time.timeout
import com.soywiz.korge.time.timers
import com.soywiz.korge.view.*
import com.soywiz.korim.atlas.readAtlas
import com.soywiz.korim.color.RGBA
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korio.lang.Closeable
import org.jbox2d.dynamics.BodyType

class Character(idleAnimation: SpriteAnimation) : Sprite(idleAnimation) {
    private var canMove: Boolean = true
    private var talk: Boolean = false

    fun move() {
        canMove = true
    }

    fun stop() {
        canMove = false
    }

    fun canMove(): Boolean {
        return canMove
    }

    fun isTalking(): Boolean {
        return talk
    }

    fun startTalking() {
        talk = true
    }

    fun stopTalking() {
        talk = false
    }
}

suspend fun Container.character(
    views: Views,
    startX: Int,
    startY: Int,
    spritesSrc: String,
    text: String,
    textColor: RGBA
): Character {
    val sprites = resourcesVfs[spritesSrc].readAtlas()

    val idleAnimation = sprites.getSpriteAnimation("idle")
    val walkRightAnimation = sprites.getSpriteAnimation("walk-right")
    val walkLeftAnimation = sprites.getSpriteAnimation("walk-left")
    val walkUpAnimation = sprites.getSpriteAnimation("walk-up")
    val walkDownAnimation = sprites.getSpriteAnimation("walk-down")

    val animations = listOf<SpriteAnimation>(
        idleAnimation,
        walkRightAnimation,
        walkLeftAnimation,
        walkDownAnimation,
        walkUpAnimation
    )

    val character = Character(idleAnimation).position(startX, startY)
        .registerBodyWithFixture(type = BodyType.STATIC, gravityScale = 0, shape = BoxShape(2f, 2f))
    addChild(character)

    val characterPhrase = text(text, textSize = 16.0, color = textColor).visible(false)

    characterPhrase.addUpdater {
        position(character.pos)
        if (character.isTalking()) {
            characterPhrase.visible(true)
            timeout(3.seconds) {
                characterPhrase.visible(false)
                character.stopTalking()
            }
        }
    }

    fun doActionInInterval(action: () -> Unit): Closeable {
        return timers.interval(200.milliseconds) { action() }
    }

    var action = doActionInInterval { }

    fun move() {
        if (character.canMove()) {
            when (animations.random()) {
                idleAnimation -> {
                    action.close()
                    character.playAnimationLooped(idleAnimation)
                }
                walkRightAnimation -> {
                    action.close()
                    character.playAnimationLooped(walkRightAnimation, spriteDisplayTime = 200.milliseconds)
                    action = doActionInInterval { character.x++ }
                }
                walkLeftAnimation -> {
                    action.close()
                    character.playAnimationLooped(walkLeftAnimation, spriteDisplayTime = 200.milliseconds)
                    action = doActionInInterval { character.x-- }
                }
                walkUpAnimation -> {
                    action.close()
                    character.playAnimationLooped(walkUpAnimation, spriteDisplayTime = 200.milliseconds)
                    action = doActionInInterval { character.y-- }
                }
                walkDownAnimation -> {
                    action.close()
                    character.playAnimationLooped(walkDownAnimation, spriteDisplayTime = 200.milliseconds)
                    action = doActionInInterval { character.y++ }
                }
            }
        }
    }

    timers.interval(5000.milliseconds) { move() }

    return character
}