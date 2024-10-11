plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.serialization)
    id("kotlin-parcelize")
}

android {
    namespace = "com.artsam.navigation"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.artsam.navigation"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    // Groovy syntax
    // flavorDimensions = ["version"]

    // Kotlin DSL syntax
    flavorDimensions.add("version")
    // "version" - указывает название "измерения"
    // можно сделать несколько измерений
    // и в каждом по несколько флейворов
    // Если измерение одно - можно опустить строку
    // dimension = "version"

    productFlavors {
        // Вкус "xml"
        create("xmlbased") {
            dimension = "version"
            applicationId = "com.xml.nav"
            versionCode = 1
            versionName = "1.0"
        }

        // Вкус "skippable"
        create("skippable") {
            dimension = "version"
            applicationId = "com.skippable.fun"
            versionCode = 1
            versionName = "1.0"
        }

        // Вкус "Custom compose navigation bad implementation"
        create("composebased-bad") {
            dimension = "version"
            applicationId = "com.compose.nav.bad"
            versionCode = 1
            versionName = "1.0"
        }

        // Вкус "Custom compose navigation fine implementation"
        create("composebased-fine") {
            dimension = "version"
            applicationId = "com.compose.nav.fine"
            versionCode = 1
            versionName = "1.0"
        }

        // Вкус "Native compose navigation with Google lib."
        create("native-nav-comp") {
            isDefault = true
            dimension = "version"
            applicationId = "com.compose.nav.native"
            versionCode = 1
            versionName = "1.0"
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
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
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    kotlinOptions {
        freeCompilerArgs += listOf(
            "-P",
            "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=${rootProject.buildDir.absolutePath}/reports",
            "-P",
            "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=${rootProject.buildDir.absolutePath}/reports",
            "-P",
            "plugin:androidx.compose.compiler.plugins.kotlin:stabilityConfigurationPath=${projectDir.absolutePath}/stability.conf",
        )
    }
}

dependencies {

    implementation(project(":mylibrary"))
    //implementation(project(":navigation"))

    implementation(libs.kotlinx.serialization.core)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.navigation.compose)

    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}