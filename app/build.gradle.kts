plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("com.google.devtools.ksp")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = Configs.compileSdk
    namespace = Configs.namespace

    defaultConfig {
        applicationId = Configs.applicationId
        minSdk = Configs.minSdk
        targetSdk = Configs.targetSdk
        versionCode = Configs.versionCode
        versionName = Configs.versionName

        testInstrumentationRunner = Configs.testInstrumentationRunner

        javaCompileOptions {
            annotationProcessorOptions {
                arguments += mapOf(
                    "room.schemaLocation" to "$projectDir/schemas",
                    "room.incremental" to "true"
                )
            }
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        compose = true
        viewBinding = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Configs.kotlinCompilerExtensionVersion
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = Configs.jvmTarget
    }
}

dependencies {
    implementation(Dependencies.coreKtx)
    implementation(Dependencies.appCompat)
    implementation(Dependencies.material)
    implementation(platform(Dependencies.firebaseBom))
    implementation(Dependencies.firebaseAnalyticsKtx)
    implementation(Dependencies.firebaseUiAuth)
    implementation(Dependencies.firebaseFirestoreKtx)
    implementation(Dependencies.playServicesAuth)
    implementation(Dependencies.activityCompose)
    implementation(Dependencies.composeUi)
    implementation(Dependencies.composeMaterial)
    implementation(Dependencies.composeAnimation)
    implementation(Dependencies.composeUiTooling)
    implementation(Dependencies.composeMaterialIconsExtended)
    implementation(Dependencies.lifecycleRuntimeKtx)
    implementation(Dependencies.navigationCompose)
    implementation(Dependencies.navigationFragmentKtx)
    implementation(Dependencies.navigationUiKtx)
    implementation(Dependencies.hiltNavigationCompose)
    implementation(Dependencies.lifecycleViewModelCompose)
    implementation(Dependencies.lifecycleLivedataKtx)
    implementation(Dependencies.gson)

    implementation(Dependencies.hiltAndroid)
    ksp(Dependencies.hiltCompilerKsp)
    implementation(Dependencies.roomRuntime)
    ksp(Dependencies.roomCompilerKsp)

    testImplementation(Dependencies.testCoreKtx)
    testImplementation(Dependencies.testExtJunitKtx)
    testImplementation(Dependencies.junit)
    testImplementation(Dependencies.testRules)
    testImplementation(Dependencies.roomTesting)
    testImplementation(Dependencies.truth)
    testImplementation(Dependencies.mockk)
    testImplementation(Dependencies.coroutinesTest)
    testImplementation(Dependencies.coroutinesAndroid)
    androidTestImplementation(Dependencies.androidTestTruth)
    androidTestImplementation(Dependencies.androidTestCoreTesting)
    androidTestImplementation(Dependencies.coroutinesTest)
    androidTestImplementation(Dependencies.androidTestExtJunit)
    androidTestImplementation(Dependencies.androidTestEspressoCore)
    androidTestImplementation(Dependencies.hiltAndroidTesting)
}
