package com.septian.githubuserfinal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.septian.githubuserfinal.fragment.MyPreferenceFragment

class SettingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        supportFragmentManager.beginTransaction()
            .add(R.id.settingActivity,MyPreferenceFragment())
            .commit()



    }
}