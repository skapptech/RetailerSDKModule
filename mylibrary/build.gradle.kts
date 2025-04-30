plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    kotlin("kapt")
}

android {
    namespace = "app.retailer.krina.shop.com.mp_shopkrina_retailer"
    compileSdk = 35

    defaultConfig {
        minSdk = 23

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
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
    //implementation(libs.rangeseekbar) // this place use material RangeSlider com.google.android.material.slider.RangeSlider
    implementation(libs.arclayout)
    implementation(libs.shimmer)
    implementation(libs.androidx.swiperefreshlayout)
    kapt(libs.glide.compiler)
    implementation(libs.room.runtime)
    kapt(libs.room.compiler)
    implementation(libs.room.rxjava2)
    implementation(libs.mixpanel)
    implementation(libs.permissions)
    implementation(libs.azure)
    implementation(libs.installreferrer)
    implementation(libs.zoomage)
    implementation(libs.aws.s3)
    //implementation(libs.fab.expand) // this place use material bar  com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
    implementation(libs.jsoup)
    implementation(libs.razorpay)
    implementation(libs.ucrop)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}