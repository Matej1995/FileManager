package cz.matej.app.filemanager.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;


/**
 * Created by Matej Sandera on 14.10.16.
 */

public class StorageManager implements StorageContract
{
	private static StorageManager sStorageManager;


	public static StorageManager getStorageManager()
	{
		if(sStorageManager == null) throw new RuntimeException("Initialize the storage manager first!");
		return sStorageManager;
	}


	public static void init(Context context)
	{
		sStorageManager = new StorageManager(context);
	}


	private final SharedPreferences mSharedPreferences;


	private StorageManager(Context context)
	{
		this.mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
	}


	@Override
	public void setRootDirectory(String rootDirectory)
	{
		this.mSharedPreferences.edit().putString(ROOT_DIRECTORY_PREF, rootDirectory).apply();

	}


	@Override
	public String getRootDirectory()
	{
		return this.mSharedPreferences.getString(ROOT_DIRECTORY_PREF, Environment.getExternalStorageDirectory().getAbsolutePath());
	}
}
