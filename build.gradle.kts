// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    val kotlinVersion = "1.3.21"
    repositories {
        google()
        jcenter()
        gradlePluginPortal()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.3.2")
        classpath(kotlin("gradle-plugin", kotlinVersion))
        // classpath("com.vanniktech:gradle-code-quality-tools-plugin:0.15.0")

        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.1.0-alpha02")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

tasks {
    val clean by registering(Delete::class) {
        delete(buildDir)
    }
}

