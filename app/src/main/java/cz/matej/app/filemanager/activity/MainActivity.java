package cz.matej.app.filemanager.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import cz.matej.app.filemanager.R;
import cz.matej.app.filemanager.databinding.ActivityMainBinding;
import cz.matej.app.filemanager.fragment.FileListFragment;


/**
 * Created by Matej Sandera on 14.10.16.
 */

public class MainActivity extends AppCompatActivity
{
	private ActivityMainBinding mBinding;


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		this.mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
		initFilesFragment(null);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.files, menu);
		return true;
	}


	public void initFilesFragment(@Nullable String path)
	{
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.replace(R.id.filesFragment, FileListFragment.newInstance(path));

		if(path != null)
			transaction.addToBackStack(path);

		transaction.commit();
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
			case R.id.menu_refresh:
				Toast.makeText(this, "Klik na refresh", Toast.LENGTH_SHORT).show();
				return true;
			case R.id.menu_files_settings:
				startActivity(SettingsActivity.newIntent(getApplicationContext()));
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}


}


