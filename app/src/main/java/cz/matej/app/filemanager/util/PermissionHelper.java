package cz.matej.app.filemanager.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;


/**
 * Created by Matej Sandera on 14.10.16.
 */

public class PermissionHelper
{

	public static void askForPermission(Activity activity, String[] permissions, int requestCode)
	{
		ActivityCompat.requestPermissions(activity, permissions, requestCode);
	}


	public static void askForPermission(Fragment fragment, String[] permissions, int requestCode)
	{
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
		{
			fragment.requestPermissions(permissions, requestCode);
		}
	}


	public static boolean checkPermissionsGranted(Context context, String[] permissions)
	{
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
		{
			return context.checkSelfPermission(permissions[0]) == PackageManager.PERMISSION_GRANTED;
		}
		return true;
	}

}
