package cz.matej.app.filemanager;

import android.app.Application;

import cz.matej.app.filemanager.util.StorageManager;


/**
 * Created by Matej Sandera on 14.10.16.
 */

public class FileManagerApp extends Application
{

	public void init()
	{
		StorageManager.init(this);
	}


	@Override
	public void onCreate()
	{
		super.onCreate();
		init();
	}
}
