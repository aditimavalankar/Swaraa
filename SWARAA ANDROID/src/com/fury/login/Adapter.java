package com.fury.login;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Adapter extends BaseAdapter  {
	
	private Context mContext;
	private LayoutInflater inflater;
	private ArrayList<Song> titles= new ArrayList<Song>();
	public Adapter(Context context, ArrayList<Song> titles)
	{
		this.mContext=context;
		this.titles=titles;
		inflater=LayoutInflater.from(mContext);
	}
	public class ViewHolder
	{
		TextView title;
		TextView views;
		TextView likes;
		TextView dislikes;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return titles.size();
	}
	@Override
	public Song getItem(int arg0) {
		// TODO Auto-generated method stub
		return titles.get(0);
	}
	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		View vi=convertView;
		ViewHolder holder;
		if(convertView==null)
		{
			vi=inflater.inflate(R.layout.tabitem,null);
			holder=new ViewHolder();
			holder.title=(TextView) vi.findViewById(R.id.title);
			holder.views=(TextView) vi.findViewById(R.id.views);
			holder.likes=(TextView) vi.findViewById(R.id.likes);
			holder.dislikes=(TextView) vi.findViewById(R.id.dislikes);
			vi.setTag(holder);
		}
		else
			holder=(ViewHolder) vi.getTag();
		holder.title.setText("Song : "+titles.get(position).gettitle());
		holder.likes.setText("Likes : "+titles.get(position).getlikes());
		holder.dislikes.setText("Dislikes : "+titles.get(position).getdislikes());
		holder.views.setText("Views : "+titles.get(position).getviews());
		Log.d("Value ",titles.get(position).gettitle());
		return vi;
	}
}
