package com.alice.a7blankproject.activity;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.view.Gravity;
import android.widget.Toast;

import com.alice.a7blankproject.R;

import java.util.Set;

public class PrefActivity extends PreferenceActivity {
    public static final String PREFERENCE_CURRENCIES     = "currencies";
    public static final String PREFERENCE_PERIOD         = "period";
    public static final String PREFERENCE_DEFAULT_PERIOD = "7";

    private static int MIN_DATE_INTERVAL = 1;
    private static int MAX_DATE_INTERVAL = 30;

    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
    }

    public static class MyPreferenceFragment extends PreferenceFragment
    {
        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

            findPreference(PREFERENCE_PERIOD).setOnPreferenceChangeListener(
                    new Preference.OnPreferenceChangeListener() {

                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    String periodStr = (String) newValue;
                    int period = !periodStr.isEmpty() ? Integer.parseInt(periodStr) : 0;
                    if (period < MIN_DATE_INTERVAL || period > MAX_DATE_INTERVAL) {
                        Toast toast = Toast.makeText(
                                getActivity(),
                                getResources().getString(R.string.preferences_error_interval, MIN_DATE_INTERVAL, MAX_DATE_INTERVAL),
                                Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        return false;
                    }
                    Toast toast = Toast.makeText(getActivity(), getResources().getString(R.string.preferences_success), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return true;
                }
            });

            findPreference(PREFERENCE_CURRENCIES).setOnPreferenceChangeListener(
                    new Preference.OnPreferenceChangeListener() {

                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    Set<String> currencies = (Set<String>) newValue;
                    if (currencies.isEmpty()) {
                        Toast toast = Toast.makeText(getActivity(), getResources().getString(R.string.preferences_error_currencies), Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        return false;
                    }
                    Toast toast = Toast.makeText(getActivity(), getResources().getString(R.string.preferences_success), Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return true;
                }
            });
        }
    }


}