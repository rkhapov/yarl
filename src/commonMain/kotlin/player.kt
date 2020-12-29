import com.soywiz.klock.milliseconds
import com.soywiz.klock.timesPerSecond
import com.soywiz.korev.Key
import com.soywiz.korge.box2d.body
import com.soywiz.korge.box2d.registerBodyWithFixture
import com.soywiz.korge.tiled.TiledMapView
import com.soywiz.korge.view.*
import com.soywiz.korim.atlas.readAtlas
import com.soywiz.korio.file.std.resourcesVfs
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.BodyType

class Player(idleAnimation: SpriteAnimation) : Sprite(idleAnimation) {
    private var hp: Int = 3
    private var inAttack: Boolean = false;

    fun doAttackState() {
        inAttack = true
    }

    fun endAttackState() {
        inAttack = false
    }

    fun isInAttackState(): Boolean {
        return inAttack
    }

    fun takeDamage() {
    }
}

suspend fun Container.player(
    views: Views,
    startX: Int,
    startY: Int,
    tiledMapView: TiledMapView,
    scale: Int = 1
): Player {
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

    val player = Player(idleRightAnimation).position(startX, startY).scale(1)
        .registerBodyWithFixture(type = BodyType.DYNAMIC, gravityScale = 0)

    addChild(player)
    player.playAnimationLooped(idleRightAnimation, spriteDisplayTime = 200.milliseconds)

    val boundsChecker = BoundsChecker(tiledMapView)
    val tileWidth = tiledMapView.tiledMap.tilewidth * scale
    val tileHeight = tiledMapView.tiledMap.tileheight * scale

    player.onCollision {
        if (it is AggressiveCharacter && views.input.keys[Key.G]) {
            it.takeDamage(direction)
        }
        if (it is StaticObject && views.input.keys[Key.F]) {
            when (direction) {
                Direction.LEFT -> it.position(player.x, player.y + player.height / 2)
                Direction.UP -> it.position(player.x + player.width / 2, player.y + player.height / 2)
                Direction.RIGHT -> it.position(player.x + player.height / 2, player.y + player.height / 2)
                Direction.DOWN -> it.position(player.x + player.width / 2, player.y + player.height / 2)
            }
        }
        if (it is StaticObject && views.input.keys[Key.G]) {
            when (direction) {
                Direction.LEFT -> it.body?.applyForceToCenter(Vec2(-30f, 0f))
                Direction.RIGHT -> it.body?.applyForceToCenter(Vec2(30f, 0f))
                Direction.DOWN -> it.body?.applyForceToCenter(Vec2(0f, 30f))
                Direction.UP -> it.body?.applyForceToCenter(Vec2(0f, -30f))
            }
        }

        if (it is Character && views.input.keys[Key.E]) {
            it.startTalking()
        }
    }

    addFixedUpdater(60.timesPerSecond) {

        if (views.input.keys.justReleased(Key.D) || views.input.keys.justReleased(Key.W)
            || views.input.keys.justReleased(Key.S) || views.input.keys.justReleased(Key.A)
        ) {
            val animationToPlay = when (direction) {
                Direction.RIGHT -> idleRightAnimation
                Direction.UP -> idleUpAnimation
                Direction.LEFT -> idleLeftAnimation
                Direction.DOWN -> idleDownAnimation
            }
            player.playAnimationLooped(animationToPlay)
        }


        if (views.input.keys[Key.D]) {
            player.playAnimation(walkRightAnimation)
            if (!boundsChecker.isPlayerBlocked(
                    player.x,
                    player.y,
                    player.width / 4,
                    player.height / 2,
                    tileWidth,
                    tileHeight
                )
            ) {
                player.x++
            }
            direction = Direction.RIGHT
        }

        if (views.input.keys[Key.W]) {
            player.playAnimation(walkUpAnimation)
            if (!boundsChecker.isPlayerBlocked(
                    player.x,
                    player.y,
                    0.0,
                    +(player.height / 4),
                    tileWidth,
                    tileHeight
                )
            ) {
                player.y--
            }
            direction = Direction.UP
        }

        if (views.input.keys[Key.S]) {
            player.playAnimation(walkDownAnimation)
            if (!boundsChecker.isPlayerBlocked(
                    player.x,
                    player.y,
                    0.0,
                    player.height / 2 + 1,
                    tileWidth,
                    tileHeight
                )
            ) {
                player.y++
            }
            direction = Direction.DOWN
        }

        if (views.input.keys[Key.A]) {
            player.playAnimation(walkLeftAnimation)
            if (!boundsChecker.isPlayerBlocked(
                    player.x,
                    player.y,
                    -1.0,
                    player.height / 2,
                    tileWidth,
                    tileHeight
                )
            ) {
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
