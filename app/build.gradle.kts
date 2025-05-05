plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "app.retailer.krina.shop.com.mp_shopkrina_retailer"
    compileSdk = 35

    defaultConfig {
        applicationId = "app.retailer.krina.shop.com.shopkrina_retailer"
        minSdk = 21
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

}

dependencies {
    val map: MutableMap<String, String> = HashMap()
    map.put("path", ":mylibrary")
    implementation(project(map))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
//    implementation(platform(libs.firebase.bom))         // Firebase BOM
//    implementation(libs.firebase.messaging)
//    implementation(libs.firebase.config)
//    implementation(libs.firebase.analytics)
//    implementation(libs.firebase.firestore)
//    implementation(libs.firebase.crashlytics)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}