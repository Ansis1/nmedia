package ru.netology.nmedia.ui

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import ru.netology.nmedia.R

class AppActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkGoogleApiAvailability()
        setContentView(R.layout.activity_app)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val name = getString(R.string.channel_remote_name1)
            val descriptionText = getString(R.string.channel_remote_description1)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channelId = getString(R.string.channel1Id)
            val channel = NotificationChannel(channelId, name, importance).apply {
                description = descriptionText
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)

        }

    }

    private fun checkGoogleApiAvailability() { // Проверка наличия Google Play Srevices
        with(GoogleApiAvailability.getInstance()) {

            val code = isGooglePlayServicesAvailable(this@AppActivity)
            if (code == ConnectionResult.SUCCESS) {

                return@with
            }
            if (isUserResolvableError(code)) {
                getErrorDialog(this@AppActivity, code, 9000)?.show()
                return
            }
            Toast.makeText(
                this@AppActivity,
                getString(R.string.google_api_unavailable), Toast.LENGTH_LONG
            ).show()
        }
    }

}