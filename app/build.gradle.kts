import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("androidx.navigation.safeargs.kotlin")

}

kapt {
    correctErrorTypes = true
}

android {
    compileSdkVersion(28)
    defaultConfig {
        applicationId = "net.daxbau.injectr"
        minSdkVersion(26)
        targetSdkVersion(28)
        versionCode = 3
        versionName = "3"
        // testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunner = "net.daxbau.injectr.CustomTestRunner"

    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    packagingOptions {
        // See https://github.com/Kotlin/kotlinx.coroutines/issues/1064
        pickFirst("META-INF/atomicfu.kotlin_module")
    }
    testOptions {
        animationsDisabled = true
    }
    sourceSets["androidTest"].java.srcDir("src/sharedTest/java")
    sourceSets["test"].java.srcDir("src/sharedTest/java")
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(kotlin("stdlib-jdk7", KotlinCompilerVersion.VERSION))
    implementation("androidx.appcompat:appcompat:1.1.0-alpha05")
    implementation("androidx.core:core-ktx:1.2.0-alpha01")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("com.google.android.material:material:1.1.0-alpha06")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")

    implementation("io.reactivex.rxjava2:rxkotlin:2.3.0")
    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")
    implementation("io.reactivex.rxjava2:rxjava:2.2.8")

    implementation("org.jetbrains.anko:anko:0.10.8")

    val coroutinesVersion = "1.2.0"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-rx2:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")

    testImplementation("com.natpryce:hamkrest:1.7.0.0")
    androidTestImplementation("com.natpryce:hamkrest:1.7.0.0")


    androidTestImplementation("androidx.test:rules:1.1.1")


    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.1.0")
    androidTestImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.1.0")
    androidTestImplementation("org.mockito:mockito-android:2.27.0")

    val lifecycleVersion = "2.0.0"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata:$lifecycleVersion")
    testImplementation("androidx.arch.core:core-testing:$lifecycleVersion")
    androidTestImplementation("androidx.arch.core:core-testing:$lifecycleVersion")
    kapt("androidx.lifecycle:lifecycle-compiler:$lifecycleVersion")

    val roomVersion = "2.1.0-alpha06"
    implementation("androidx.room:room-runtime:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$roomVersion")
    // optional - RxJava support for Room
    // implementation("androidx.room:room-rxjava2:$roomVersion")
    // Test helpers
    testImplementation("androidx.room:room-testing:$roomVersion")

    val navVersion = "2.1.0-alpha02"
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")

    val pagingVersion = "2.1.0"
    implementation("androidx.paging:paging-runtime-ktx:$pagingVersion")
    testImplementation("androidx.paging:paging-common-ktx:$pagingVersion")
    // optional - RxJava support
    //implementation("androidx.paging:paging-rxjava2-ktx:$pagingVersion")

    implementation("org.koin:koin-android:2.0.0-rc-2")
    implementation("org.koin:koin-android-viewmodel:2.0.0-rc-2")
    testImplementation("org.koin:koin-test:2.0.0-rc-2") {
        exclude("org.mockito")
    }
    androidTestImplementation("org.koin:koin-test:2.0.0-rc-2") {
        exclude("org.mockito")
    }

    testImplementation("junit:junit:4.12")
    androidTestImplementation("androidx.test:runner:1.2.0-beta01")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0-beta01")
    androidTestImplementation("com.schibsted.spain:barista:2.8.0") {
        exclude("com.android.support")
        exclude("org.jetbrains.kotlin")
        exclude("androidx.test.espresso")
    }
    androidTestImplementation("androidx.test.ext:junit:1.1.0")
    implementation("com.airbnb.android:epoxy:3.4.0")
    kapt("com.airbnb.android:epoxy-processor:3.4.0")

    implementation("io.fotoapparat:fotoapparat:2.7.0")
    implementation("io.fotoapparat.fotoapparat:adapter-rxjava2:2.7.0")
}

repositories {
    mavenCentral()
    maven("http://repository.jetbrains.com/all")
    maven("https://jitpack.io")
}
