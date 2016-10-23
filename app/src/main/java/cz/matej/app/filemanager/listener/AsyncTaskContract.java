package cz.matej.app.filemanager.listener;

/**
 * Created by Matej Sandera on 14.10.16.
 */

public interface AsyncTaskContract<T>
{
	void onPostExecute(T object);
}
