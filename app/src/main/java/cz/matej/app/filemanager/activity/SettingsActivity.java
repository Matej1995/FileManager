package cz.matej.app.filemanager.activity;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v7.app.ActionBar;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.text.TextUtils;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;

import cz.matej.app.filemanager.R;
import cz.matej.app.filemanager.util.StorageManager;

import java.util.List;


/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends AppCompatPreferenceActivity
{

	public static Intent newIntent(Context context)
	{
		return new Intent(context, SettingsActivity.class);
	}


	/**
	 * A preference value change listener that updates the preference's summary
	 * to reflect its new value.
	 */
	private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener()
	{
		@Override
		public boolean onPreferenceChange(Preference preference, Object value)
		{
			preference.setSummary(value.toString());
			return true;
		}
	};


	/**
	 * Helper method to determine if the device has an extra-large screen. For
	 * example, 10" tablets are extra-large.
	 */
	private static boolean isXLargeTablet(Context context)
	{
		return (context.getResources().getConfiguration().screenLayout
				& Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
	}


	/**
	 * Binds a preference's summary to its value. More specifically, when the
	 * preference's value is changed, its summary (line of text below the
	 * preference title) is updated to reflect the value. The summary is also
	 * immediately updated upon calling this method. The exact display format is
	 * dependent on the type of preference.
	 *
	 * @see #sBindPreferenceSummaryToValueListener
	 */
	private static void bindPreferenceSummaryToValue(Preference preference)
	{
		// Set the listener to watch for value changes.
		preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

		// Trigger the listener immediately with the preference's
		// current value.
		final String defaultValue = getDefaultValue(preference);
		if(preference instanceof EditTextPreference)
		{
			((EditTextPreference) preference).setText(defaultValue);
		}
		sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
				PreferenceManager
						.getDefaultSharedPreferences(preference.getContext())
						.getString(preference.getKey(), defaultValue));
	}


	/**
	 * Should return default value for specific preference, should expand to switch later on.
	 *
	 * @param preference - current preference
	 * @return default value for specific preference
	 */
	private static String getDefaultValue(Preference preference)
	{
		return preference.getKey().equalsIgnoreCase(StorageManager.ROOT_DIRECTORY_PREF) ? StorageManager.getStorageManager().getRootDirectory() : "";
	}


	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item)
	{
		int id = item.getItemId();
		if(id == android.R.id.home)
		{
			if(!super.onMenuItemSelected(featureId, item))
			{
				NavUtils.navigateUpFromSameTask(this);
			}
			return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean onIsMultiPane()
	{
		return isXLargeTablet(this);
	}


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setupActionBar();
		getFragmentManager()
				.beginTransaction()
				.replace(android.R.id.content, GeneralPreferenceFragment.newInstance())
				.commit();
	}


	/**
	 * This method stops fragment injection in malicious applications.
	 * Make sure to deny any unknown fragments here.
	 */
	protected boolean isValidFragment(String fragmentName)
	{
		return PreferenceFragment.class.getName().equals(fragmentName)
				|| GeneralPreferenceFragment.class.getName().equals(fragmentName);
	}


	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	private void setupActionBar()
	{
		ActionBar actionBar = getSupportActionBar();
		if(actionBar != null)
		{
			// Show the Up button in the action bar.
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
	}


	/**
	 * This fragment shows general preferences only. It is used when the
	 * activity is showing a two-pane settings UI.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public static class GeneralPreferenceFragment extends PreferenceFragment
	{
		public static GeneralPreferenceFragment newInstance()
		{
			return new GeneralPreferenceFragment();
		}


		@Override
		public void onCreate(Bundle savedInstanceState)
		{
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.pref_general);
			setHasOptionsMenu(true);

			bindPreferenceSummaryToValue(findPreference(getString(R.string.preference_root_directory)));
		}
	}
}
