package cz.matej.app.filemanager.adapter;

import android.support.annotation.DrawableRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cz.matej.app.filemanager.R;
import cz.matej.app.filemanager.listener.ItemClickListener;
import cz.matej.app.filemanager.model.FileModel;


/**
 * Created by Fanda on 17.10.2016.
 */
public class FilesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

	private enum FileAdapterViewType
	{
		FOLDER(0, R.drawable.ic_folder),
		FILE(1, R.drawable.ic_file);

		@DrawableRes
		final int drawableResource;
		final int value;


		FileAdapterViewType(int value, @DrawableRes int drawableResource)
		{
			this.value = value;
			this.drawableResource = drawableResource;
		}


		public int getValue()
		{
			return this.value;
		}


		public int getDrawableResource()
		{
			return this.drawableResource;
		}
	}


	private ItemClickListener<FileModel> mItemClickListener;
	private List<FileModel> fileModels;


	public FilesAdapter(final List<FileModel> fileModels)
	{
		this.fileModels = fileModels;
	}


	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
	{
		if(viewType == FileAdapterViewType.FILE.getValue() || viewType == FileAdapterViewType.FOLDER.getValue())
		{
			View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_file, parent, false);
			return new FileAdapterViewHolder(view, this.mItemClickListener);
		}
		else
		{
			throw new RuntimeException("There is no view type that matches the type" + viewType);
		}
	}


	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
	{
		if(holder instanceof FileAdapterViewHolder)
		{

			((FileAdapterViewHolder) holder).bind(this.fileModels.get(position));
		}
	}


	@Override
	public int getItemCount()
	{
		return this.fileModels.size();
	}


	@Override
	public int getItemViewType(final int position)
	{
		return this.fileModels.get(position).isFolder() ? FileAdapterViewType.FOLDER.getValue() : FileAdapterViewType.FILE.getValue();
	}


	private static class FileAdapterViewHolder extends RecyclerView.ViewHolder
	{
		private ItemClickListener<FileModel> mItemClickListener;
		private TextView mFileNameTextView;
		private ImageView mFileIconImageView;


		FileAdapterViewHolder(View itemView, ItemClickListener<FileModel> itemClickListener)
		{
			super(itemView);
			this.mItemClickListener = itemClickListener;
			this.mFileNameTextView = (TextView) itemView.findViewById(R.id.fileName);
			this.mFileIconImageView = (ImageView) itemView.findViewById(R.id.fileIcon);
		}


		void bind(final FileModel fileModel)
		{
			this.mFileNameTextView.setText(fileModel.getName());
			this.mFileIconImageView.setImageResource(fileModel.isFolder() ? FileAdapterViewType.FOLDER.drawableResource : FileAdapterViewType.FILE.drawableResource);
			if(this.mItemClickListener != null)
			{
				this.itemView.setOnClickListener(new View.OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						FileAdapterViewHolder.this.mItemClickListener.onItemClick(fileModel);
					}
				});
			}
		}

	}


	public void setFileModels(List<FileModel> fileModels)
	{
		this.fileModels = fileModels;
	}


	public ItemClickListener<FileModel> getItemClickListener()
	{
		return this.mItemClickListener;
	}


	public void setItemClickListener(ItemClickListener<FileModel> itemClickListener)
	{
		this.mItemClickListener = itemClickListener;
	}
}
