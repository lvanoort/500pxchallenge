package com.lukevanoort.chall500px

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class ChallengeActivity : AppCompatActivity() {
    private lateinit var component: ActivityComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component = getAppComponent()
            .plusActivity(ActivityModule(this))
            .also { it.inject(this) }

    }
}