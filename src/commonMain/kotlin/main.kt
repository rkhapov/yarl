import com.soywiz.korge.Korge
import com.soywiz.korge.scene.Module
import com.soywiz.korge.scene.Scene
import com.soywiz.korinject.AsyncInjector
import com.soywiz.korma.geom.SizeInt
import kotlin.reflect.KClass

suspend fun main() = Korge(Korge.Config(module = ConfigModule))


object ConfigModule : Module() {
    override val size = SizeInt(800, 800)
    override val mainScene: KClass<out Scene> = VillageLevel::class
    override suspend fun AsyncInjector.configure() {
        mapPrototype { VillageLevel() }
        mapPrototype { SnowLevel() }
        mapPrototype { FinalLevel() }
    }
}