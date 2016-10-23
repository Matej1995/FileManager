package cz.matej.app.filemanager.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import cz.matej.app.filemanager.BR;


/**
 * Created by Matej Sandera on 14.10.16.
 */

public class FileModel extends BaseObservable
{

	private String name;
	private String path;
	private boolean isFolder;


	public FileModel(String name, String path, boolean isFolder)
	{
		this.name = name;
		this.path = path;
		this.isFolder = isFolder;
	}


	@Bindable
	public String getName()
	{
		return this.name;
	}


	public void setName(String name)
	{
		this.name = name;
		notifyPropertyChanged(BR.name);
	}


	@Bindable
	public String getPath()
	{
		return this.path;
	}


	public void setPath(String path)
	{
		this.path = path;
		notifyPropertyChanged(BR.path);
	}


	@Bindable
	public boolean isFolder()
	{
		return this.isFolder;
	}


	public void setFolder(boolean folder)
	{
		this.isFolder = folder;
		notifyPropertyChanged(BR.folder);
	}


	@Override
	public String toString()
	{
		return "FileModel{" +
				"name='" + name + '\'' +
				", path='" + path + '\'' +
				", isFolder=" + isFolder +
				'}';
	}
}
