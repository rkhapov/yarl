import com.soywiz.korge.gradle.*

buildscript {
	val korgePluginVersion: String by project

	repositories {
		mavenLocal()
		maven { url = uri("https://dl.bintray.com/korlibs/korlibs") }
		maven { url = uri("https://plugins.gradle.org/m2/") }
		mavenCentral()
		google()
		maven { url = uri("https://dl.bintray.com/kotlin/kotlin-dev") }
		maven { url = uri("https://dl.bintray.com/kotlin/kotlin-eap") }
	}
	dependencies {
		classpath("com.soywiz.korlibs.korge.plugins:korge-gradle-plugin:$korgePluginVersion")
	}
}

apply<KorgeGradlePlugin>()

korge {
	id = "urfu.kotlincourse.yarl"
	supportBox2d()
	
	targetJvm()
	targetJs()
//	targetDesktop()
//	targetIos()
	targetAndroidIndirect() // targetAndroidDirect()
}
