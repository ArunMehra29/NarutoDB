import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kapt.ksp)
}

android {

    val compileTargetSdkVersion: Int = libs.versions.compileTargetSdk.get().toInt()
    val minSdkVersion: Int = libs.versions.minSdk.get().toInt()


    namespace = "com.narutodb.data"
    compileSdk = compileTargetSdkVersion

    defaultConfig {
        minSdk = minSdkVersion
        testOptions.targetSdk = compileTargetSdkVersion

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_22
        targetCompatibility = JavaVersion.VERSION_22
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_22)
        }
    }
}

dependencies {

    implementation (project(projectPath = ":core"))

    //Calling API dependency
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.square.logging.interceptor)
    implementation(libs.square.okhttp)

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)

    ksp(libs.room.compiler)

    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}