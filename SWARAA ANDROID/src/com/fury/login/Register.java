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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends Activity implements OnClickListener {

	EditText first_name;
	EditText last_name;
	EditText username;
	EditText password;
	EditText emailid;
	EditText aboutme;
	
	Button Done;
	Button Cancel;
	
	HttpPost httppost;
	HttpResponse response;
	HttpClient httpclient;
	List<NameValuePair> nameValuePairs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		
		// all the initializations.
		first_name = (EditText) findViewById(R.id.in_first_name);
		last_name = (EditText) findViewById(R.id.in_last_name);
		username = (EditText) findViewById(R.id.in_username);
		password = (EditText) findViewById(R.id.in_password);
		emailid = (EditText) findViewById(R.id.in_email);
		aboutme = (EditText) findViewById(R.id.in_aboutme);
		Done = (Button) findViewById(R.id.reg_done);
		Cancel = (Button) findViewById(R.id.reg_cancel);
		Done.setOnClickListener(this);
		Cancel.setOnClickListener(this);
		}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.reg_done:
				new Thread(new Runnable(){
					public void run(){
						server();
					}
				}).start();							
				break;
		case R.id.reg_cancel:
			first_name.setText("");
			last_name.setText("");
			username.setText("");
			password.setText("");
			emailid.setText("");
			aboutme.setText("");
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
			httppost = new HttpPost("http://192.168.1.112/login.php");
			nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("first_name", first_name.getText().toString().trim()));
			nameValuePairs.add(new BasicNameValuePair("last_name", last_name.getText().toString().trim()));
			nameValuePairs.add(new BasicNameValuePair("username", username.getText().toString().trim()));
			nameValuePairs.add(new BasicNameValuePair("password", password.getText().toString().trim()));
			nameValuePairs.add(new BasicNameValuePair("emailid", emailid.getText().toString().trim()));
			nameValuePairs.add(new BasicNameValuePair("aboutme", aboutme.getText().toString().trim()));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			Log.e("sent request", "ys");
			//response=httpclient.execute(httppost);
			Log.d("testing",httppost.toString());
			ResponseHandler<String> responsehanlder=new BasicResponseHandler();
			final String response = httpclient.execute(httppost,responsehanlder);
			System.out.println("Response : "+ response);
			//nsoleMessage();
			Log.e("got request", "sdasd");
			if(response.equalsIgnoreCase("Success")){
				runOnUiThread(new Runnable(){
					public void run(){
						Toast.makeText(getApplicationContext(),"Registered Succesfully",Toast.LENGTH_SHORT).show();
					}	
				});
				startActivity(new Intent(Register.this,MainActivity.class));
			}
			else{
				Log.e("asdsad", "asdasd");			}
		
		}
		catch(Exception e)
		{
			System.out.println("Exception : " + e.getMessage());
		}	
	}
}
