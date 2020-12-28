import com.soywiz.klock.milliseconds
import com.soywiz.klock.seconds
import com.soywiz.klock.timesPerSecond
import com.soywiz.korev.Key
import com.soywiz.korge.animate.play
import com.soywiz.korge.box2d.BoxShape
import com.soywiz.korge.box2d.registerBodyWithFixture
import com.soywiz.korge.input.onClick
import com.soywiz.korge.resources.resourceTtfFont
import com.soywiz.korge.tiled.TiledMapView
import com.soywiz.korge.time.delay
import com.soywiz.korge.view.*
import com.soywiz.korge.view.Camera
import com.soywiz.korge.view.anchor
import com.soywiz.korge.view.camera.*
import com.soywiz.korge.view.tween.moveBy
import com.soywiz.korge.view.tween.moveTo
import com.soywiz.korim.atlas.Atlas
import com.soywiz.korim.atlas.readAtlas
import com.soywiz.korim.color.Colors
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korma.geom.degrees
import com.soywiz.korte.dynamic.Dynamic2.toDouble
import org.jbox2d.dynamics.BodyType
import kotlin.math.abs

class Player(idleAnimation: SpriteAnimation) : Sprite(idleAnimation) {
    private var hp: Int = 3
    private var inAttack : Boolean = false;

    fun doAttackState() {
        inAttack = true
    }

    fun endAttackState() {
        inAttack = false
    }

    fun isInAttackState() : Boolean {
        return inAttack
    }

    fun takeDamage() {
        println("player hp is ${--hp}")
    }
}

suspend fun Container.player(views: Views, startX: Int, startY: Int, tiledMapView: TiledMapView, scale: Int = 1): Player {
    var direction = Direction.RIGHT
    val sprites = resourcesVfs["skeleton_animations.xml"].readAtlas()

    val idleRightAnimation = sprites.getSpriteAnimation("idle-right")
    val idleUpAnimation = sprites.getSpriteAnimation("idle-up")
    val idleLeftAnimation = sprites.getSpriteAnimation("idle-left")
    val idleDownAnimation = sprites.getSpriteAnimation("idle-down")
    val walkRightAnimation = sprites.getSpriteAnimation("walk-right")
    val walkUpAnimation = sprites.getSpriteAnimation("walk-up")
    val walkDownAnimation = sprites.getSpriteAnimation("walk-down")
    val walkLeftAnimation = sprites.getSpriteAnimation("walk-left")
    val firstAttackRightAnimation = sprites.getSpriteAnimation("first-attack-right")
    val firstAttackUpAnimation = sprites.getSpriteAnimation("first-attack-up")
    val firstAttackLeftAnimation = sprites.getSpriteAnimation("first-attack-left")
    val firstAttackDownAnimation = sprites.getSpriteAnimation("first-attack-down")

    val player = Player(idleRightAnimation).position(startX, startY).scale(1).registerBodyWithFixture(type=BodyType.DYNAMIC, gravityScale = 0)
    val playerRect = solidRect(player.width/2, player.height/2).position(startX, startY)

    addChild(player)
    player.playAnimationLooped(idleRightAnimation, spriteDisplayTime = 200.milliseconds)

    val boundsChecker = BoundsChecker(tiledMapView)
    val tileWidth = tiledMapView.tiledMap.tilewidth * scale
    val tileHeight = tiledMapView.tiledMap.tileheight * scale

    player.onCollision {
        if (it is AggressiveCharacter && views.input.keys[Key.G]) {
            it.takeDamage()
        }
    }

//    val cameraSpeed = 4
//    val cameraCenter = 400
//    val screenSize = 800
    addFixedUpdater(60.timesPerSecond) {
//        val target = cameraCenter - (player.x + player.width)
//        if (x< target) {
//            x += cameraSpeed
//            if ( x > target) x = target
//        } else if ( x > target) {
//            x -= cameraSpeed
//            if ( x < target) x = target
//        }
//
//        if(x>0) x = 0.0

//		if(x < - (tiledMapWidth - screenSize)) x = - (tiledMapWidth - screenSize)

        if (views.input.keys[Key.D]) {
            player.playAnimation(walkRightAnimation)
            if (!boundsChecker.isBlockingObject(player.x + player.width/2 + 1, player.y + player.height/2, tileWidth, tileHeight)) {
                playerRect.position(player.x + player.width/2, player.y + player.height/2)
                player.x++
            }
            direction = Direction.RIGHT
        }
        if (views.input.keys.justReleased(Key.D) || views.input.keys.justReleased(Key.W)
            || views.input.keys.justReleased(Key.S) || views.input.keys.justReleased(Key.A)) {
            val animationToPlay = when(direction) {
                Direction.RIGHT -> idleRightAnimation
                Direction.UP -> idleUpAnimation
                Direction.LEFT -> idleLeftAnimation
                Direction.DOWN -> idleDownAnimation
            }
            player.playAnimationLooped(animationToPlay)
        }
        if (views.input.keys[Key.W]) {
            player.playAnimation(walkUpAnimation)
            if (!boundsChecker.isBlockingObject(player.x + player.width/2, player.y - 1, tileWidth, tileHeight)) {
                playerRect.position(player.x + player.width/2, player.y)
                player.y--
            }
            direction = Direction.UP
        }
        if (views.input.keys[Key.S]) {
            player.playAnimation(walkDownAnimation)
            if (!boundsChecker.isBlockingObject(player.x, player.y + player.height/2 + 1, tileWidth, tileHeight)) {
                playerRect.position(player.x, player.y + player.height/2)
                player.y++
            }
            direction = Direction.DOWN
        }
        if (views.input.keys[Key.A]) {
            player.playAnimation(walkLeftAnimation)
            if (!boundsChecker.isBlockingObject(player.x - 1, player.y + player.height/2, tileWidth, tileHeight)) {
                playerRect.position(player.x, player.y + player.height/2)
                player.x--
            }
            direction = Direction.LEFT
        }
        if (views.input.keys[Key.G]) {
            player.doAttackState()

            when (direction) {
                Direction.RIGHT -> player.playAnimation(firstAttackRightAnimation)
                Direction.LEFT -> player.playAnimation(firstAttackLeftAnimation)
                Direction.UP -> player.playAnimation(firstAttackUpAnimation)
                Direction.DOWN -> player.playAnimation(firstAttackDownAnimation)
            }
        }
    }

    player.onAnimationCompleted {
        player.endAttackState()
    }

    return player
}
