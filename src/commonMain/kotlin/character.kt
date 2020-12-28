import com.soywiz.klock.milliseconds
import com.soywiz.klock.seconds
import com.soywiz.korge.animate.animate
import com.soywiz.korge.animate.animateParallel
import com.soywiz.korge.animate.animator
import com.soywiz.korge.animate.play
import com.soywiz.korge.box2d.BoxShape
import com.soywiz.korge.box2d.registerBodyWithFixture
import com.soywiz.korge.input.onClick
import com.soywiz.korge.time.TimerComponents
import com.soywiz.korge.time.timeout
import com.soywiz.korge.time.timers
import com.soywiz.korge.view.*
import com.soywiz.korge.view.tween.moveBy
import com.soywiz.korim.atlas.Atlas
import com.soywiz.korim.atlas.readAtlas
import com.soywiz.korio.async.launch
import com.soywiz.korio.async.launchImmediately
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korio.lang.Closeable
import kotlinx.coroutines.Job
import org.jbox2d.collision.shapes.CircleShape
import org.jbox2d.dynamics.BodyType
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.startCoroutine

class Character(idleAnimation: SpriteAnimation) : Sprite(idleAnimation) {
    fun die() {
        removeFromParent()
    }
}

suspend fun Container.character(views: Views, startX: Int, startY: Int, spritesSrc: String): Character {
    val sprites = resourcesVfs[spritesSrc].readAtlas()

    val idleAnimation = sprites.getSpriteAnimation("idle")
    val walkRightAnimation = sprites.getSpriteAnimation("walk-right")
    val walkLeftAnimation = sprites.getSpriteAnimation("walk-left")
    val walkUpAnimation = sprites.getSpriteAnimation("walk-up")
    val walkDownAnimation = sprites.getSpriteAnimation("walk-down")

    val animations = listOf<SpriteAnimation>(idleAnimation, walkRightAnimation, walkLeftAnimation, walkDownAnimation, walkUpAnimation)

    val character = Character(idleAnimation).position(startX, startY).registerBodyWithFixture(type= BodyType.STATIC, gravityScale = 0, shape = BoxShape(2f, 2f))
    addChild(character)

    fun doActionInInterval(action: () -> Unit): Closeable {
        val timer = timers.interval(200.milliseconds) {action()}

        return timer
    }

    var action = doActionInInterval {  }

    fun move() {
        val animationToPlay = animations.random()
        when(animationToPlay) {
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

    timers.interval(5000.milliseconds) { move() }

    return character
}