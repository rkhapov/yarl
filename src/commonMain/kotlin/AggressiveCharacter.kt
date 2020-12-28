import com.soywiz.klock.milliseconds
import com.soywiz.klock.seconds
import com.soywiz.klock.timesPerSecond
import com.soywiz.korge.box2d.BoxShape
import com.soywiz.korge.box2d.body
import com.soywiz.korge.box2d.registerBodyWithFixture
import com.soywiz.korge.tiled.TiledMapView
import com.soywiz.korge.time.timeout
import com.soywiz.korge.view.*
import com.soywiz.korim.format.readBitmap
import com.soywiz.korio.file.std.resourcesVfs
import com.soywiz.korma.geom.PointInt
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.BodyType

class AggressiveCharacter(idleAnimation: SpriteAnimation) : Sprite(idleAnimation) {
    private var hp: Int = 5
    private var canTakeDamage = true
    private var lastMoveDirection : Direction = Direction.UP

    var findingPlayer = true

    fun move(direction: Direction) {
        when (direction) {
            Direction.RIGHT -> {
                x += 1.2
            }
            Direction.LEFT -> {
                x -= 1.2
            }
            Direction.UP -> {
                y -= 1.2
            }
            Direction.DOWN -> {
                y += 1.2
            }
        }

        lastMoveDirection = direction
    }

    fun die() {
        removeFromParent()
    }

    fun takeDamage() {
        if (!canTakeDamage) {
            return
        }

        if (--hp == 0) {
            die()
            return
        }

        when (lastMoveDirection) {
            Direction.UP -> {
                body?.applyForceToCenter(Vec2(0f, +1000f))
            }
            Direction.RIGHT -> {
                body?.applyForceToCenter(Vec2(-1000f, 0f))
            }
            Direction.DOWN -> {
                body?.applyForceToCenter(Vec2(0f, -1000f))
            }
            Direction.LEFT -> {
                body?.applyForceToCenter(Vec2(+100f, 0f))
            }
        }

        findingPlayer = false
        canTakeDamage = false

        timeout(1.seconds) {
            body?.force?.set(0, 0)
            findingPlayer = true
            canTakeDamage = true
        }
    }
}

suspend fun Container.aggressiveCharacter(
    views: Views,
    startX: Int,
    startY: Int,
    spriteSrc: String,
    player: Player,
    tiledMapView: TiledMapView,
    tileWidth: Int,
    tileHeight: Int
): AggressiveCharacter {
//    val sprites = resourcesVfs[spriteSrc].readAtlas()

    val walkLeft = resourcesVfs["slime1_side.png"].readBitmap()
    val walkLeftAnimation =
        SpriteAnimation(spriteMap = walkLeft, spriteWidth = 16, spriteHeight = 16, columns = 4, rows = 1)
    val walkUp = resourcesVfs["slime1_back.png"].readBitmap()
    val walkUpAnimation =
        SpriteAnimation(spriteMap = walkUp, spriteWidth = 16, spriteHeight = 16, columns = 4, rows = 1)
    val attack = resourcesVfs["slime_explode.png"].readBitmap()
    val attackAnimation =
        SpriteAnimation(spriteMap = attack, spriteWidth = 37, spriteHeight = 41, columns = 8, rows = 1)

    val character = AggressiveCharacter(walkLeftAnimation).position(startX, startY)
        .registerBodyWithFixture(type = BodyType.DYNAMIC, gravityScale = 0, shape = BoxShape(2f, 2f))

    addChild(character)

    character.playAnimationLooped(attackAnimation, spriteDisplayTime = 200.milliseconds)

    addFixedUpdater(60.timesPerSecond) {
        val collisionLayer = tiledMapView.tiledMap.data.tileLayers.first()

        fun tryGo(x: Int, y: Int, currentPath : List<Direction>, direction: Direction, queue: MutableList<Pair<List<Direction>, PointInt>>, visited: MutableSet<PointInt>) {
            if (x < 0 || y < 0 || x >= tiledMapView.width || y >= tiledMapView.height) {
                return
            }

            val newPoint = PointInt(x, y)

            if (visited.contains(newPoint)) {
                return
            }

            if (collisionLayer.map[x, y].value != 0) {
                return
            }

            val newPath = currentPath.toMutableList()
            newPath.add(direction)

            queue.add(Pair(newPath, newPoint))
            visited.add(newPoint)
        }

        fun findPath(myX: Int, myY: Int, targetX: Int, targetY: Int): List<Direction> {
            val queue = mutableListOf<Pair<List<Direction>, PointInt>>()
            val visited = mutableSetOf<PointInt>()

            queue.add(Pair(emptyList(), PointInt(myX, myY)))
            visited.add(PointInt(myX, myY))

            while (queue.isNotEmpty()) {
                val (path, point) = queue.removeAt(0)

                if (point.x == targetX && point.y == targetY) {
                    return path
                }

                tryGo(point.x + 1, point.y, path, Direction.RIGHT, queue, visited)
                tryGo(point.x - 1, point.y, path, Direction.LEFT, queue, visited)
                tryGo(point.x, point.y - 1, path, Direction.UP, queue, visited)
                tryGo(point.x, point.y + 1, path, Direction.DOWN, queue, visited)
            }

            return emptyList()
        }

        val myTileX = (character.x / tileWidth).toInt()
        val myTileY = (character.y / tileHeight).toInt()
        val playerTileX = (player.x / tileWidth).toInt()
        val playerTileY = (player.y / tileHeight).toInt()
        val path = findPath(myTileX, myTileY, playerTileX, playerTileY)

        if (path.isNotEmpty() && character.findingPlayer) {
            character.move(path.first())
        }
    }

    return character
}