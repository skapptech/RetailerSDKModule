plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.retailer.retailermodule"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.retailer.retailermodule"
        minSdk = 24
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
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.multidex)
    implementation(libs.flexbox)
    implementation(libs.lifecycle.ext)
    implementation(libs.lifecycle.livedata)
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.runtime)
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.config)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.crashlytics)
    implementation(libs.play.analytics)
    implementation(libs.play.auth)
    implementation(libs.places)
    implementation(libs.places.legacy)
    implementation(libs.maps)
    implementation(libs.location)
    implementation(libs.feature.delivery)
    implementation(libs.app.update)
    implementation(libs.okhttp)
    implementation(libs.retrofit)
    implementation(libs.adapter.rxjava2)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)
    implementation(libs.gson)
    implementation(libs.rxandroid)
    implementation(libs.rxjava)
    implementation(libs.gif)
    implementation(libs.compressor)
    implementation(libs.circleimageview)
    implementation(libs.picasso)
    implementation(libs.glide)
    implementation(libs.viewpagerindicator)
    implementation(libs.rangeseekbar)
    implementation(libs.arclayout)
    implementation(libs.shimmer)
    implementation(libs.glide.compiler)
    implementation(libs.room.runtime)
    implementation(libs.room.compiler)
    implementation(libs.room.rxjava2)
    implementation(libs.mixpanel)
    implementation(libs.permissions)
    implementation(libs.azure)
    implementation(libs.installreferrer)
    implementation(libs.zoomage)
    implementation(libs.aws.s3)
    implementation(libs.fab.expand)
    implementation(libs.jsoup)
    implementation(libs.razorpay)
    implementation(libs.ucrop)


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}