import com.soywiz.korau.sound.SoundChannel
import com.soywiz.korau.sound.readMusic
import com.soywiz.korge.view.Container
import com.soywiz.korio.file.std.resourcesVfs

suspend fun Container.playMusic(musicSrc: String): SoundChannel {
    val music = resourcesVfs[musicSrc].readMusic()
    val channel = music.playForever()

    return channel
}
