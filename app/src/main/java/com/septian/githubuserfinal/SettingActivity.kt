package com.septian.githubuserfinal

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.septian.githubuserfinal.fragment.MyPreferenceFragment

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        supportFragmentManager.beginTransaction()
            .add(R.id.settingActivity,MyPreferenceFragment())
            .commit()

        supportActionBar?.title = "Reminder"




    }
}