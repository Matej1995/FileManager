package cz.matej.app.filemanager.task;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cz.matej.app.filemanager.listener.AsyncTaskContract;
import cz.matej.app.filemanager.model.FileModel;


/**
 * Created by Matej Sandera on 14.10.16.
 */

public class AcquireFilesAsyncTask extends AsyncTask<String, Void, List<FileModel>>
{

	private AsyncTaskContract<List<FileModel>> mAsyncTaskContract;


	public AcquireFilesAsyncTask(AsyncTaskContract<List<FileModel>> asyncTaskContract)
	{
		this.mAsyncTaskContract = asyncTaskContract;
	}


	@Override
	protected List<FileModel> doInBackground(String... params)
	{
		return loadFileList(params[0]);
	}


	private List<FileModel> loadFileList(String path)
	{
		List<FileModel> fileEntities = new ArrayList<>();
		File baseFolder = new File(path);
		if(baseFolder.listFiles() == null)
		{
			return null;
		}
		for(File file : baseFolder.listFiles())
		{
			fileEntities.add(new FileModel(file.getName(), file.getPath(), !file.isFile()));
		}
		return fileEntities;
	}


	@Override
	protected void onPostExecute(List<FileModel> fileModel)
	{
		super.onPostExecute(fileModel);
		this.mAsyncTaskContract.onPostExecute(fileModel);
	}
}
