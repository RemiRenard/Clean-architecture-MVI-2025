plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.google.services)
    alias(libs.plugins.google.jupiter.plugin)
}

kotlin {
    jvmToolchain(21)
}

android {
    namespace = "android.architecture.app"
    compileSdk = 35


    buildFeatures {
        compose = true
        buildConfig = true
    }

    defaultConfig {
        applicationId = "android.architecture.app"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "renard.remi.ping.HiltTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            isDebuggable = true
            buildConfigField("String", "API_URL", "\"http://192.168.1.100:8080/api/\"")
        }
        release {
            isMinifyEnabled = false
            buildConfigField("String", "API_URL", "\"http://192.168.1.61:8080/api/prod/\"")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.retrofit)
    implementation(libs.gson)
    implementation(libs.ok.http)
    implementation(libs.ok.http.logs)
    implementation(libs.datastore)
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.biometric)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.messaging.ktx)
    implementation(libs.room.ktx)
    implementation(libs.room.runtime)

    // Sometimes you need to rebuild with ksp
    ksp(libs.hilt.compiler)
    ksp(libs.room.compiler)

    // Unit Tests
    testImplementation(libs.jupiter)
    testImplementation(libs.mockk)
    testImplementation(libs.kotest)
    testImplementation(libs.kotest.core)
    testImplementation(libs.kotest.property)
    testImplementation(libs.kotlinx.coroutine.test)

    androidTestImplementation(platform(libs.androidx.compose.bom))
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Instrumented Tests
    androidTestImplementation(libs.hilt.test)
    kspAndroidTest(libs.hilt.compiler.test)
    androidTestImplementation(libs.androidx.runner)
    androidTestImplementation(libs.androidx.compose.test)
}