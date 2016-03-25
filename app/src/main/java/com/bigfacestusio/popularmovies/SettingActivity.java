package com.bigfacestusio.popularmovies;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class SettingActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.pref_general);
        Preference prefSortBy = findPreference(getString(R.string.pref_sortby_key));
        prefSortBy.setOnPreferenceChangeListener(this);

        onPreferenceChange(prefSortBy, PreferenceManager.getDefaultSharedPreferences(prefSortBy.getContext()).getString(prefSortBy.getKey(), ""));
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String stringValue = newValue.toString();

        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(stringValue);
            if (prefIndex >= 0) {
                preference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        }
        return true;
    }
}
