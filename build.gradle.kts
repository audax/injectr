// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    val kotlinVersion = "1.3.30"
    repositories {
        google()
        jcenter()
        gradlePluginPortal()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.4.1")
        classpath(kotlin("gradle-plugin", kotlinVersion))
        // classpath("com.vanniktech:gradle-code-quality-tools-plugin:0.15.0")

        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.1.0-alpha04")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

tasks {
    @Suppress("UNUSED_VARIABLE") val clean by registering(Delete::class) {
        delete(buildDir)
    }
}

