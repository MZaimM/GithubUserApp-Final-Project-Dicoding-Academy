package com.example.githubuserapp.preference;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;

import com.example.githubuserapp.R;
import com.example.githubuserapp.receiver.AlarmReceiver;

public class Preference extends PreferenceFragmentCompat {
    private String REMINDER;
    private String LANGUAGE_SETTING;
    private Boolean isOn = true;
    public static final String EXTRA_MESSAGE = "message";
    public static final String EXTRA_TITLE = "title";

    private SwitchPreference reminder;
    private androidx.preference.Preference settingLanguage;

    private AlarmReceiver alarmReceiver;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.preference);

        init();

        onClickSwitchPreference();
        onClickSettingLanguage();
    }


    private void init(){
        REMINDER = getResources().getString(R.string.reminder);
        LANGUAGE_SETTING = getResources().getString(R.string.setting_language);

        reminder = findPreference(REMINDER);
        settingLanguage = findPreference(LANGUAGE_SETTING);

    }

    private  void onClickSwitchPreference(){
        reminder.setOnPreferenceClickListener(new androidx.preference.Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(androidx.preference.Preference preference) {
                alarmReceiver = new AlarmReceiver();
                if (reminder.isChecked()){
                    alarmReceiver.setRepeatingReminder(getActivity());
                    preference.setSummary("Switch ON state");
                    preference.getSummary();
                }else {
                    alarmReceiver.cancelAlarm(getActivity());
                    preference.setSummary("Switch OFF State");
                    preference.getSummary();
                }
                return true;
            }
        });
    }

    private void onClickSettingLanguage(){
        settingLanguage.setOnPreferenceClickListener(new androidx.preference.Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(androidx.preference.Preference preference) {
                Intent intent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
                startActivity(intent);
                return false;
            }
        });
    }

}
