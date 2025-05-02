package app.retailer.krina.shop.com.mp_shopkrina_retailer.firebase

import android.app.NotificationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class NotificationDismissActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.cancel(intent.getIntExtra("id", -1))
        finish()
    }
}