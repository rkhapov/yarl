import com.soywiz.klock.*
import com.soywiz.korev.Key
import com.soywiz.korge.*
import com.soywiz.korge.box2d.body
import com.soywiz.korge.box2d.registerBodyWithFixture
import com.soywiz.korge.scene.Module
import com.soywiz.korge.scene.Scene
import com.soywiz.korge.tiled.readTiledMap
import com.soywiz.korge.tiled.tiledMapView
import com.soywiz.korge.tiled.toXml
import com.soywiz.korge.view.*
import com.soywiz.korge.view.ktree.readKTree
import com.soywiz.korim.atlas.readAtlas
import com.soywiz.korim.color.*
import com.soywiz.korinject.AsyncInjector
import com.soywiz.korio.file.std.*
import com.soywiz.korma.geom.SizeInt
import org.jbox2d.common.Vec2
import org.jbox2d.dynamics.BodyType
import player
import kotlin.reflect.KClass

suspend fun main() = Korge(Korge.Config(module = ConfigModule))
//{
//	val testTree = resourcesVfs["testTree2.ktree"].readKTree(views)
//	addChild(testTree)

//	val testTmx = resourcesVfs["/FreePlatformerNA/Foreground/forest.tmx"].readTiledMap()
//	tiledMapView(testTmx)
//	val imageLayer = map.tiledMap.imageLayers[0]
//	println(imageLayer.image)

//	val player = player(views, 0,500)
//	val character = character(views, 20, 300, "test_animations.xml")

//	val anotherCharacterSprites = resourcesVfs["skeleton_animations.xml"].readAtlas()
//	val idleAnimation = anotherCharacterSprites.getSpriteAnimation("idle")
//	val walkAnimation = anotherCharacterSprites.getSpriteAnimation("walk")
//	val jumpAnimation = anotherCharacterSprites.getSpriteAnimation("jump")
//	val attackAnimation = anotherCharacterSprites.getSpriteAnimation("attack")
//
//	val skeleton = sprite(idleAnimation).xy(0, 500).scale(1.5).registerBodyWithFixture(type=BodyType.DYNAMIC)
//	skeleton.playAnimationLooped(idleAnimation, spriteDisplayTime = 200.milliseconds)
//	skeleton.onAnimationCompleted {skeleton.playAnimationLooped(idleAnimation, spriteDisplayTime = 200.milliseconds)}
//
//	solidRect(1000, 40, Colors.GREEN).position(0, 650).registerBodyWithFixture(type=BodyType.STATIC)
//
//	val circle = circle(20.0, Colors.RED).position(300, 550).registerBodyWithFixture(type=BodyType.DYNAMIC)
//	val rect = solidRect(20, 20, Colors.RED).position(200, 550).registerBodyWithFixture(type=BodyType.DYNAMIC)

//	skeleton.onCollision {
//		if (views.input.keys[Key.G]) {
//			when (it) {
//				rect -> rect.body?.applyForceToCenter(Vec2(10f, 0f))
//				circle -> circle.body?.applyForceToCenter(Vec2(100f, 0f))
//			}
//		}
//	}
//
//	addFixedUpdater(60.timesPerSecond) {
//		val cameraSpeed = 4
//		val cameraCenter = 400
//		val target = cameraCenter - skeleton.x
//		if (x< target) {
//			x += cameraSpeed
//			if ( x > target) x = target
//		} else if ( x > target) {
//			x -= cameraSpeed
//			if ( x < target) x = target
//		}
//
//		if(x>0) x = 0.0
////		if(x < - (tiledMap.width - screenSize)) x = - (tiledMap.width - screenSize)
//
////		cam.setTo(skeleton)
//
//		if (views.input.keys[Key.D]) {
//			skeleton.playAnimation(walkAnimation)
//			skeleton.x++
//		}
//		if (views.input.keys.justReleased(Key.D))
//		{
//			skeleton.playAnimationLooped(idleAnimation)
//		}
//		if (views.input.keys[Key.SPACE]) {
//			skeleton.playAnimation(jumpAnimation)
//			skeleton.x += 5
//		}
//		if (views.input.keys[Key.G]) {
////			if (skeleton.collidesWith(rect)) {
////				rect.body?.applyForceToCenter(Vec2(50f, 0f))
//////				circle.body?.applyForceToCenter(Vec2(50f, 0f))
////			}
//			skeleton.playAnimation(attackAnimation)
//		}
//	}
//}

object ConfigModule : Module() {
	override val size = SizeInt(800, 800)
	override val mainScene: KClass<out Scene> = SnowLevel::class
	override suspend fun AsyncInjector.configure() {
		mapPrototype { VillageLevel() }
		mapPrototype { SnowLevel() }
	}
}