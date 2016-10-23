package cz.matej.app.filemanager.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.List;

import cz.matej.app.filemanager.R;
import cz.matej.app.filemanager.activity.MainActivity;
import cz.matej.app.filemanager.adapter.FilesAdapter;
import cz.matej.app.filemanager.databinding.FragmentFilesBinding;
import cz.matej.app.filemanager.listener.AsyncTaskContract;
import cz.matej.app.filemanager.listener.ItemClickListener;
import cz.matej.app.filemanager.model.FileModel;
import cz.matej.app.filemanager.task.AcquireFilesAsyncTask;
import cz.matej.app.filemanager.util.PermissionHelper;
import cz.matej.app.filemanager.util.StorageManager;


/**
 * Created by Matej Sandera on 14.10.16.
 */

public class FileListFragment extends Fragment implements AsyncTaskContract<List<FileModel>>, ItemClickListener<FileModel>
{


	private static final String ARG_DIRECTORY_PATH = "directory_path";


	public static FileListFragment newInstance(String path)
	{
		FileListFragment fragment = new FileListFragment();
		Bundle bundle = new Bundle();
		bundle.putString(ARG_DIRECTORY_PATH, path);
		fragment.setArguments(bundle);
		return fragment;
	}


	private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 4469;
	private static final int GRID_ITEMS_COUNT = 3;

	private View mRootView;
	private RecyclerView mRecyclerView;
	private FilesAdapter mFilesAdapter;


	@Override
	public void onPostExecute(final List<FileModel> fileList)
	{
		if(mFilesAdapter == null)
		{
			mFilesAdapter = new FilesAdapter(fileList);
			mFilesAdapter.setItemClickListener(this);
			mRecyclerView.setAdapter(mFilesAdapter);
			mRecyclerView.setLayoutManager(getLayoutManager());
		}
		else
		{
			mFilesAdapter.setFileModels(fileList);
			mFilesAdapter.notifyDataSetChanged();
		}
	}


	@Override
	public void onItemClick(FileModel fileModel)
	{
		if(getActivity() instanceof MainActivity)
		{
			if(fileModel.isFolder())
			{
				((MainActivity) getActivity()).initFilesFragment(fileModel.getPath());
			}
			else
			{
				final Intent fileViewIntent = new Intent(Intent.ACTION_VIEW);
				fileViewIntent.setData(Uri.fromFile(new File(fileModel.getPath())));
				final Intent chooser = Intent.createChooser(fileViewIntent, getString(R.string.choose_intent_app));
				startActivity(chooser);
			}
		}
	}


	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		FragmentFilesBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_files, container, false);
		init(binding);
		return this.mRootView;
	}


	@Override
	public void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		parseIntent();
	}


	@Override
	public void onResume()
	{
		super.onResume();
		revive();
	}


	private void revive()
	{
		if(mRecyclerView.getAdapter() == null)
		{
			mRecyclerView.setAdapter(mFilesAdapter);
			mRecyclerView.setLayoutManager(getLayoutManager());
		}
	}


	private void parseIntent()
	{
		if(getArguments() != null && getArguments().getString(ARG_DIRECTORY_PATH) != null)
		{
			getFiles(getArguments().getString(ARG_DIRECTORY_PATH));
		}
		else
		{
			getFiles();
		}
	}


	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
	{
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if(requestCode == STORAGE_PERMISSIONS_REQUEST_CODE)
		{
			boolean flag = true;
			for(int grantResult : grantResults)
			{
				if(grantResult == PackageManager.PERMISSION_DENIED)
				{
					flag = false;
				}
			}
			if(flag)
				parseIntent();
		}
	}


	private void init(FragmentFilesBinding binding)
	{
		this.mRootView = binding.getRoot();
		this.mRecyclerView = binding.recyclerView;
	}


	private void getFiles(String directory)
	{
		if(directory == null) directory = StorageManager.getStorageManager().getRootDirectory();
		final String directoryPath = directory;

		final Runnable executorRunnable = new Runnable()
		{
			@Override
			public void run()
			{
				new AcquireFilesAsyncTask(FileListFragment.this).execute(directoryPath);
			}
		};

		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
		{
			checkPermissions(executorRunnable);
		}
		else
		{
			executorRunnable.run();
		}
	}


	private void getFiles()
	{
		getFiles(StorageManager.getStorageManager().getRootDirectory());
	}


	private void checkPermissions(Runnable runnable)
	{
		final String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
		if(!PermissionHelper.checkPermissionsGranted(getContext(), permissions))
		{
			PermissionHelper.askForPermission(this, permissions, STORAGE_PERMISSIONS_REQUEST_CODE);
		}
		else
		{
			runnable.run();
		}
	}


	@NonNull
	private LinearLayoutManager getLayoutManager()
	{
		return getResources().getBoolean(R.bool.is_landscape) ?
				new GridLayoutManager(getContext(), GRID_ITEMS_COUNT) : new LinearLayoutManager(getContext());
	}

}
