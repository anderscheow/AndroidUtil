package io.github.anderscheow.androidutil.appCompat.activity

import android.os.Bundle

abstract class BaseAppCompatActivity : FoundationAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        initBeforeSuperOnCreate()

        super.onCreate(savedInstanceState)

        setContentView(getResLayout())

        getToolbar()?.let { toolbar ->
            setSupportActionBar(toolbar)

            supportActionBar?.setDisplayHomeAsUpEnabled(requiredDisplayHomeAsUp())
        }

        init()
    }
}
