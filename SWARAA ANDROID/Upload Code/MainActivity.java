package com.fury.login;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {

	EditText username;
	EditText password;
	Button login;
	Button cancel;
	Button register;
	
	HttpPost httppost;
	HttpResponse response;
	HttpClient httpclient;
	List<NameValuePair> nameValuePairs;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// all the initializations.
		username = (EditText) findViewById(R.id.usrinput);
		password = (EditText) findViewById(R.id.passinput);
		login = (Button) findViewById(R.id.login);
		cancel = (Button) findViewById(R.id.cancel);
		register = (Button) findViewById(R.id.register);
		login.setOnClickListener(this);
		cancel.setOnClickListener(this);
		register.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Log.e("clikc","dasd");
		switch (v.getId()) {
		case R.id.login:
			new Thread(new Runnable(){
				public void run(){
					server();
				}
			}).start();
			break;
		case R.id.register:
			Intent act = new Intent(MainActivity.this, Register.class);
			Log.e("clickreg", "clicked reg");
			startActivity(act);
			break;
		case R.id.cancel:
			username.setText("");
			password.setText("");
			break;
		default:
			break;
		}
	}
	void server()
	{
		try
		{
			
			httpclient= new DefaultHttpClient();
			httppost = new HttpPost("http://192.168.1.112/android.php");
			nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("username", username.getText().toString().trim()));
			nameValuePairs.add(new BasicNameValuePair("password", password.getText().toString().trim()));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			Log.e("sent request", "ys");
			response=httpclient.execute(httppost);
			
			ResponseHandler<String> responsehanlder=new BasicResponseHandler();
			final String response = httpclient.execute(httppost,responsehanlder);
			System.out.println("Response : "+ response);
			//nsoleMessage();
			Log.e("got request", "sdasd");
			if(response.equalsIgnoreCase("User Found")){
				runOnUiThread(new Runnable(){
					public void run(){
						Toast.makeText(getApplicationContext(),"Login Success",Toast.LENGTH_SHORT).show();
					}	
				});
				startActivity(new Intent(MainActivity.this, HomePage.class));
			}
		
		}
		catch(Exception e)
		{
			System.out.println("Exception : " + e.getMessage());
		}	
	}
}