package com.septian.githubuserfinal.fragment

import android.os.Bundle
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.septian.githubuserfinal.R

class MyPreferenceFragment :  PreferenceFragmentCompat(){
    private lateinit var switchPreference: SwitchPreference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)

        val alarmReceiver = AlarmReceiver()

        switchPreference = findPreference(resources.getString(R.string.notification))!!

        switchPreference.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener{_,_,->
                if (switchPreference.isChecked){
                    activity?.let { alarmReceiver.cancelAlarm(it) }
                    Toast.makeText(activity,R.string.reminder_off,Toast.LENGTH_SHORT).show()
                    switchPreference.isChecked = false

                }else{
                    activity?.let { alarmReceiver.setRepeatingAlarm(it) }
                    Toast.makeText(activity, R.string.reminder_on, Toast.LENGTH_SHORT).show()
                    switchPreference.isChecked = true
                }
                false
            }

    }
}