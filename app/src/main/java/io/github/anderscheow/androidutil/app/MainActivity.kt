package io.github.anderscheow.androidutil.app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.github.anderscheow.androidutil.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
