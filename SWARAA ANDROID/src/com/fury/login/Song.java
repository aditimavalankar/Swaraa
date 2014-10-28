package com.fury.login;

import android.content.Context;

public class Song {
	
	public String title;
	public int num_views;
	public int num_likes;
	public int num_dislikes;
	
	public Song(Context ctx,String s,int v,int l,int d)
	{
		this.title=s;
		this.num_views=v;
		this.num_likes=l;
		this.num_dislikes=d;
		
	}

	public String gettitle()
	{
		return this.title;
	}
    public int getviews()
    {
    	return this.num_views;
    }
    public int getlikes()
    {
    	return this.num_likes;
    }
    public int getdislikes()
    {
    	return this.num_dislikes;
    }
}
