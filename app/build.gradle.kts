import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("androidx.navigation.safeargs.kotlin")

}

kapt {
    correctErrorTypes = true
}

android {
    compileSdk = 34

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
    defaultConfig {
        applicationId = "net.daxbau.injectr"
        minSdk = 26
        targetSdk = 33
        versionCode = 6
        versionName = "6"
        // testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunner = "net.daxbau.injectr.CustomTestRunner"

    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    packaging {
        resources {
            pickFirsts += setOf("META-INF/atomicfu.kotlin_module")
        }
    }
    testOptions {
        animationsDisabled = true
    }
    sourceSets["androidTest"].java.srcDir("src/sharedTest/java")
    sourceSets["test"].java.srcDir("src/sharedTest/java")
    namespace = "net.daxbau.injectr"
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(kotlin("stdlib-jdk7", KotlinCompilerVersion.VERSION))
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.core:core-ktx:1.3.2")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("com.google.android.material:material:1.2.1")
    implementation("androidx.constraintlayout:constraintlayout:2.0.2")

    implementation("io.reactivex.rxjava2:rxkotlin:2.4.0")
    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")
    implementation("io.reactivex.rxjava2:rxjava:2.2.20")

    val coroutinesVersion = "1.2.0"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-rx2:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")

    testImplementation("com.natpryce:hamkrest:1.8.0.1")
    androidTestImplementation("com.natpryce:hamkrest:1.8.0.1")


    androidTestImplementation("androidx.test:rules:1.3.0")


    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
    androidTestImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
    androidTestImplementation("org.mockito:mockito-android:3.5.13")

    val lifecycleVersion = "2.2.0"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata:$lifecycleVersion")
    testImplementation("androidx.arch.core:core-testing:2.1.0")
    androidTestImplementation("androidx.arch.core:core-testing:2.1.0")
    kapt("androidx.lifecycle:lifecycle-compiler:$lifecycleVersion")

    val roomVersion = "2.2.4"
    implementation("androidx.room:room-runtime:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$roomVersion")
    // optional - RxJava support for Room
    // implementation("androidx.room:room-rxjava2:$roomVersion")
    // Test helpers
    testImplementation("androidx.room:room-testing:$roomVersion")

    val navVersion = "2.2.0"
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")

    val pagingVersion = "2.1.2"
    implementation("androidx.paging:paging-runtime-ktx:$pagingVersion")
    testImplementation("androidx.paging:paging-common-ktx:$pagingVersion")
    // optional - RxJava support
    //implementation("androidx.paging:paging-rxjava2-ktx:$pagingVersion")

    implementation(platform("io.insert-koin:koin-bom:3.5.6"))
    implementation("io.insert-koin:koin-core")
    implementation("io.insert-koin:koin-android")
    testImplementation(platform("io.insert-koin:koin-bom:3.5.6"))
    testImplementation("io.insert-koin:koin-test") {
        exclude("org.mockito")
    }
    androidTestImplementation(platform("io.insert-koin:koin-bom:3.5.6"))
    androidTestImplementation("io.insert-koin:koin-test") {
        exclude("org.mockito")
    }
    implementation("com.louiscad.splitties:splitties-fun-pack-android-base-with-views-dsl:3.0.0")
    implementation("com.louiscad.splitties:splitties-alertdialog:3.0.0")


    testImplementation("junit:junit:4.13.1")
    androidTestImplementation("androidx.test:runner:1.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")

    androidTestImplementation("com.adevinta.android:barista:4.2.0") {
        exclude("com.android.support")
        exclude("org.jetbrains.kotlin")
        exclude("androidx.test.espresso")
    }
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    implementation("com.airbnb.android:epoxy:4.1.0")
    implementation("com.airbnb.android:epoxy-paging:4.1.0")
    kapt("com.airbnb.android:epoxy-processor:4.1.0")

    implementation("io.fotoapparat:fotoapparat:2.7.0")
    implementation("io.fotoapparat.fotoapparat:adapter-rxjava2:2.7.0")
}

repositories {
    mavenCentral()
    maven("https://repository.jetbrains.com/all")
    maven("https://jitpack.io")
    google()
}
