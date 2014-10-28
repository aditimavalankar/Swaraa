package com.fury.login;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class HomePage extends Activity implements OnItemClickListener {
	
	private ArrayList<String> titles=new ArrayList<String>();
	private ArrayList<Integer> views=new ArrayList<Integer>();
	private ArrayList<Integer> likes=new ArrayList<Integer>();
	private ArrayList<Integer> dislikes=new ArrayList<Integer>();
	private ArrayList<String> urls = new ArrayList<String>();
	private ArrayList<Song> songs=new ArrayList<Song>();
	private ListView listview;
	private Adapter adapter;
	
	Button upload;
	Button logout;
	
	JSONObject jobject;
	String jsonstring;
	JSONArray jsonArray;
	
	List<NameValuePair> nameValuePairs;
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Error","starting");
        setContentView(R.layout.activity_home_page);

        listview=(ListView)findViewById(R.id.list);
        upload = (Button) findViewById (R.id.upload_button);
        logout = (Button) findViewById (R.id.logout);
        upload.setOnClickListener(new View.OnClickListener() {
	
        		@Override
        		public void onClick(View v) {
        			// TODO Auto-generated method stub
        			Intent act = new Intent(HomePage.this, Upload.class);
        			startActivity(act);
        		}
        });
        
        logout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(HomePage.this, MainActivity.class));
			}
		});

        new Thread(new Runnable(){
        	public void run(){
        		fetch();
        	}
        }).start();

	}

	void fetch()
	{
		int flag = 0;
		String db_url = "http://192.168.1.112/response.php";
		InputStream is = null;
		String line = null;
		try {
			HttpClient	httpclient= new DefaultHttpClient();
			HttpPost httppost = new HttpPost(db_url);
			nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("username",""));
			nameValuePairs.add(new BasicNameValuePair("password", ""));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			Log.e("sent request", "ys");
			HttpResponse response=httpclient.execute(httppost);
			ResponseHandler<String> responsehanlder=new BasicResponseHandler();
			final String output= httpclient.execute(httppost,responsehanlder);
			System.out.println("Response : "+ output);
			Log.e("try","dsada");
			HttpEntity entity = response.getEntity();
			is = entity.getContent();
		}catch(Exception e) {
			Log.e("log_tag", "Error in http connection" +e.toString());
		}
		String result = "";
		try {
			Log.e("as","ddsa");
			BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
			StringBuilder sb = new StringBuilder();

			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			result=sb.toString();
		}catch(Exception e){
    Log.e("log_tag", "Error in http connection" +e.toString());
		}
		try {
			jobject = new JSONObject(result.trim());	
			jsonArray = jobject.getJSONArray("object");
			for(int i=0; i<jsonArray.length();i++) {
				String value1,value5;
				int value2,value3,value4;
				value1 = jsonArray.getJSONObject(i).getString("title").toString();
				value2 = jsonArray.getJSONObject(i).getInt("views");	
				value3 = jsonArray.getJSONObject(i).getInt("likes");
				value4 = jsonArray.getJSONObject(i).getInt("dislikes"); 	
				value5= jsonArray.getJSONObject(i).getString("uri").toString();
				titles.add(value1);
				views.add(value2);
				likes.add(value3);	
				dislikes.add(value4);
				urls.add(value5);
			}
		}catch(JSONException e){
			Log.e("log_tag", "Error parsing data" +e.toString());
		}
  
		for(int i=0;i<titles.size();i++) {
			Song s=new Song(this,titles.get(i),views.get(i),likes.get(i),dislikes.get(i));
			songs.add(s);
			flag = 1;
			Log.d("Error","adding records");
		}
		adapter=new Adapter(this,songs);
		if(flag == 1) {
			runOnUiThread(new Runnable(){
				public void run(){
					listview.setAdapter(adapter);
					Log.d("Error","set adapter");	  
				}
			});  
		}
		listview.setOnItemClickListener(this);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_page, menu);
		Log.d("Error","exit 3");
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Log.d("Error","entering onclick");
		Log.e("arg2",""+arg2);
		Log.e("arg3",""+arg3);
		Intent i = new Intent(HomePage.this, SongPage.class);
		i.putExtra("url",urls.get(arg2));
		i.putExtra("title", titles.get(arg2));
		i.putExtra("views", views.get(arg2));
		i.putExtra("likes", likes.get(arg2));
		i.putExtra("id", arg2);
		i.putExtra("dislikes", dislikes.get(arg2));
		startActivity(i);
		Log.d("Error","exit");	
	}	
}