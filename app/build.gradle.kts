import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("androidx.navigation.safeargs.kotlin")

}

android {
    compileSdkVersion(28)
    defaultConfig {
        applicationId = "net.daxbau.injectr"
        minSdkVersion(26)
        targetSdkVersion(28)
        versionCode = 1
        versionName = "1"
        // testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunner = "net.daxbau.injectr.CustomTestRunner"

    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    sourceSets["androidTest"].java.srcDir("src/sharedTest/java")
    sourceSets["test"].java.srcDir("src/sharedTest/java")
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(kotlin("stdlib-jdk7", KotlinCompilerVersion.VERSION))
    implementation("androidx.appcompat:appcompat:1.1.0-alpha04")
    implementation("androidx.core:core-ktx:1.1.0-alpha05")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("com.google.android.material:material:1.1.0-alpha05")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")

    testImplementation("com.natpryce:hamkrest:1.7.0.0")
    androidTestImplementation("com.natpryce:hamkrest:1.7.0.0")


    androidTestImplementation("androidx.test:rules:1.1.1")


    testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.1.0")
    androidTestImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.1.0")
    androidTestImplementation("org.mockito:mockito-android:2.23.4")

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
    androidTestImplementation("androidx.test:runner:1.2.0-alpha03")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0-alpha03")
    androidTestImplementation("com.schibsted.spain:barista:2.8.0") {
        exclude("com.android.support")
        exclude("org.jetbrains.kotlin")
        exclude("androidx.test.espresso")
    }
    androidTestImplementation("androidx.test.ext:junit:1.1.0")
}

repositories {
    mavenCentral()
    maven("http://repository.jetbrains.com/all")
}
