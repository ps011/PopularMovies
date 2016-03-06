package tk.prasheelsoni.popularmovies;


import android.app.ActionBar;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatDelegate;


public class SettingsActivity extends PreferenceActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }

}
