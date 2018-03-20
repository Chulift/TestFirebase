package com.example.chulift.testfirebase;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.EditTextPreferenceDialogFragmentCompat;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

public class PrefsFragment extends PreferenceFragmentCompat {

    private static final String FRAGMENT_DIALOG_TAG = "android.support.v7.preference.PreferenceFragment.DIALOG";
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }

    /*
    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        if (this.getFragmentManager().findFragmentByTag(FRAGMENT_DIALOG_TAG) == null) {
            Object f = null;

            if (preference instanceof EditTextPreference) {
                f = EditTextPreferenceDialogFragmentCompat.newInstance(preference.getKey());
            } else if (preference instanceof EditTextPreference) {
                f = EditTextPreferenceDialogFragmentCompat.newInstance(preference.getKey());
            } else {
                super.onDisplayPreferenceDialog(preference);
            }

            if (f != null) {
                ((DialogFragment) f).setTargetFragment(this, 0);
                ((DialogFragment) f).show(this.getFragmentManager(), FRAGMENT_DIALOG_TAG);
            }
        }
    }
    */
}
