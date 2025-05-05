package app.retailer.krina.shop.com.mp_shopkrina_retailer.utils

import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics

object FirebaseInitializer {
    fun initialize(context: Context) {
        try {
            // Initialize Firebase if it hasn't been initialized yet
            if (FirebaseApp.getApps(context).isEmpty()) {
                FirebaseApp.initializeApp(context)
                println("Doneeeee  dsfsdfdfsf")
            }
            println("Doneeeee  dsfsdfdfsf123")
            // Initialize Crashlytics
            FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)

            // Log something useful (optional)
            FirebaseCrashlytics.getInstance().log("Firebase and Crashlytics initialized from SDK")

        } catch (e: Exception) {
            // Handle any errors or initialization failures
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }
}
