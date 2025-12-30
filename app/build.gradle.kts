import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.secrets.gradle)
}
val keyProperties = Properties().apply {
    load(File(rootDir, "key.properties").inputStream())
}

android {
    namespace = "com.giovanna.amatucci.foodbook"
    compileSdk = 36
    defaultConfig {
        applicationId = "com.giovanna.amatucci.foodbook"
        minSdk = 27
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        buildConfigField("String", "BASE_URL", "\"platform.fatsecret.com/rest\"")
        buildConfigField("String", "TOKEN_URL", "\"oauth.fatsecret.com\"")
        buildConfigField(
            "String",
            "FATSECRET_CLIENT_ID",
            "\"${keyProperties.getProperty("FATSECRET_CLIENT_ID")}\""
        )
        buildConfigField(
            "String",
            "FATSECRET_CLIENT_SECRET",
            "\"${keyProperties.getProperty("FATSECRET_CLIENT_SECRET")}\""
        )
    }
    sourceSets {
        getByName("main") {
            java.srcDir("build/generated/ksp/main/kotlin")
        }
    }
    buildTypes {
        release {
            applicationIdSuffix = ".release"
            versionNameSuffix = "-release"
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("Boolean", "DEBUG_MODE", "false")
            buildConfigField("Long", "REQUEST_TIMEOUT", "20_000L")
            buildConfigField("Long", "CONNECT_TIMEOUT", "15_000L")
        }
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
            isDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false
            buildConfigField("Boolean", "DEBUG_MODE", "true")
            buildConfigField("Long", "REQUEST_TIMEOUT", "30_000L")
            buildConfigField("Long", "CONNECT_TIMEOUT", "20_000L")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
        }
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    kotlinOptions {
        freeCompilerArgs = listOf("-XXLanguage:+PropertyParamAnnotationDefaultTargetMode")
    }
}

dependencies {
    // ---------- 🧱 Core & Kotlin ----------
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)

    // ---------- 🧠 State, Lifecycle & ViewModel ----------
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.core.splashscreen)

    // ---------- 💉 Dependency Injection (Koin) ----------
    implementation(platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    implementation(libs.koin.androidx.compose.navigation)
    implementation(libs.koin.annotations)
    ksp(libs.koin.ksp.compiler)

    // ---------- 🎨 Jetpack Compose UI ----------
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)

    // ---------- 🗺️ Navigation ----------
    implementation(libs.androidx.navigation.compose)

    // ---------- 🌐 Networking (Ktor & Serialization) ----------
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.auth)
    implementation(libs.ktor.cio)
    implementation(libs.ktor.okhttp)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.kotlin.coroutines.android)
    implementation(libs.ktor.client.mock)

    // ---------- 🖼️ Image Loading ----------
    implementation(libs.coil.compose)
    implementation(libs.androidx.palette)

    // ---------- 🧪 Testing (Local) ----------
    testImplementation(libs.junit)
    testImplementation(libs.test.mockk)
    testImplementation(libs.test.turbine)
    testImplementation(libs.koin.test)
    testImplementation(libs.paging.test)
    testImplementation(libs.kotlin.coroutines.test)
    testImplementation(libs.androidx.test.coreKtx)
    testImplementation(libs.androidx.paging.common.ktx)

    // --- Instrumentation Tests (Android) --- 🧪
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.android.leakcanary)

    // --- Logging (Timber) ---
    implementation(libs.timber)

    // --- Room ---
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
    implementation(libs.room.paging)

    // --- Security ---
    implementation(libs.sqlcipher)
    implementation(libs.sqlite.ktx)
    implementation(libs.security.crypto)

    // --- Paging ---
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)
}