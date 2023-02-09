package com.z100.valentuesday

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.fragment.findNavController
import com.z100.valentuesday.api.service.ApiRequestService
import com.z100.valentuesday.service.DataManagerService
import com.z100.valentuesday.util.Const
import com.z100.valentuesday.util.Debug

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val backgroundImage: ImageView = findViewById(R.id.SplashScreenImage)
        val backgroundImage2: ImageView = findViewById(R.id.SplashScreenImage_2)
        val slideAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        backgroundImage.startAnimation(slideAnimation)
        backgroundImage2.startAnimation(slideAnimation)

        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 2000) // 3000 is the delayed time in milliseconds.
    }
}