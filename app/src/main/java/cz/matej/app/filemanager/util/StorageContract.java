package cz.matej.app.filemanager.util;

/**
 * Created by Matej Sandera on 14.10.16.
 */

public interface StorageContract
{

	String ROOT_DIRECTORY_PREF = "root_directory_pref";
	void setRootDirectory(String rootDirectory);
	String getRootDirectory();


}
