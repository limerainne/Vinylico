package win.limerainne.vinylico.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 * Created by Limerainne on 2016-11-03.
 *
 * https://www.bignerdranch.com/blog/splash-screens-the-right-way/
 */
class SplashActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Intent(this, MainActivity::class.java).let{
            startActivity(it)
        }
        finish()
    }
}