object Dependencies {
    // AndroidX
    const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
    const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
    const val material = "com.google.android.material:material:${Versions.material}"

    // Firebase
    const val firebaseBom = "com.google.firebase:firebase-bom:${Versions.firebaseBom}"
    const val firebaseAnalyticsKtx = "com.google.firebase:firebase-analytics-ktx"
    const val firebaseUiAuth = "com.firebaseui:firebase-ui-auth:${Versions.firebaseUiAuth}"
    const val firebaseFirestoreKtx = "com.google.firebase:firebase-firestore-ktx"

    // Google Play Services
    const val playServicesAuth = "com.google.android.gms:play-services-auth:${Versions.playServicesAuth}"

    // Compose
    const val activityCompose = "androidx.activity:activity-compose:${Versions.activityCompose}"
    const val composeUi = "androidx.compose.ui:ui:${Versions.compose}"
    const val composeMaterial = "androidx.compose.material:material:${Versions.compose}"
    const val composeAnimation = "androidx.compose.animation:animation:${Versions.compose}"
    const val composeUiTooling = "androidx.compose.ui:ui-tooling:${Versions.compose}"
    const val composeMaterialIconsExtended = "androidx.compose.material:material-icons-extended:${Versions.compose}"

    // Lifecycle
    const val lifecycleRuntimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"
    const val lifecycleViewModelCompose = "androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.lifecycle}"
    const val lifecycleLivedataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}"

    // Navigation
    const val navigationCompose = "androidx.navigation:navigation-compose:${Versions.navigation}"
    const val navigationFragmentKtx = "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    const val navigationUiKtx = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"

    // Hilt
    const val hiltNavigationCompose = "androidx.hilt:hilt-navigation-compose:${Versions.hiltNavigationCompose}"

    // Gson
    const val gson = "com.google.code.gson:gson:${Versions.gson}"

    // Dagger Hilt
    const val hiltAndroid = "com.google.dagger:hilt-android:${Versions.hilt}"
    const val hiltCompilerKsp = "com.google.dagger:hilt-compiler:${Versions.hilt}"

    // Room
    const val roomRuntime = "androidx.room:room-runtime:${Versions.room}"
    const val roomCompilerKsp = "androidx.room:room-compiler:${Versions.room}"

    // Test dependencies
    const val testCoreKtx = "androidx.test:core-ktx:${Versions.testCoreKtx}"
    const val testExtJunitKtx = "androidx.test.ext:junit-ktx:${Versions.testExtJunitKtx}"
    const val junit = "junit:junit:${Versions.junit}"
    const val testRules = "androidx.test:rules:${Versions.testRules}"
    const val roomTesting = "androidx.room:room-testing:${Versions.room}"
    const val truth = "com.google.truth:truth:${Versions.truth}"
    const val mockk = "io.mockk:mockk:${Versions.mockk}"
    const val coroutinesTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutinesTest}"
    const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutinesTest}"

    // Android test dependencies
    const val androidTestTruth = "com.google.truth:truth:${Versions.truth}"
    const val androidTestCoreTesting = "androidx.arch.core:core-testing:${Versions.androidTestCoreTesting}"
    const val androidTestExtJunit = "androidx.test.ext:junit:${Versions.androidTestExtJunit}"
    const val androidTestEspressoCore = "androidx.test.espresso:espresso-core:${Versions.androidTestEspressoCore}"
    const val hiltAndroidTesting = "com.google.dagger:hilt-android-testing:${Versions.hilt}"
}
