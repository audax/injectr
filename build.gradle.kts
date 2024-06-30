// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    val kotlinVersion = "2.0.21"
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.8.2")
        classpath(kotlin("gradle-plugin", kotlinVersion))
        // classpath("com.vanniktech:gradle-code-quality-tools-plugin:0.15.0")

        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.8.8")
    }
}
plugins {
    id("com.google.devtools.ksp") version "2.0.21-1.0.27" apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

tasks {
    val clean by registering(Delete::class) {
        delete(layout.buildDirectory)
    }
}

