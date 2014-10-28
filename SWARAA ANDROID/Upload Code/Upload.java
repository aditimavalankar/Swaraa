package com.fury.login;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
 
public class Upload extends Activity implements OnClickListener{
	
	HttpPost httppost;
	HttpResponse response;
	HttpClient httpclient;
	List<NameValuePair> nameValuePairs;
	String fileName;
	String delim = "/";
	String[] temp;
	
	EditText Title;
    TextView messageText;
    Button uploadButton;
    Button upload_audio;
    int serverResponseCode = 0;
    ProgressDialog dialog = null;
    private static final int SELECT_AUDIO = 2;
    String selectedPath = "";
       
    String upLoadServerUri = null;
    
    /**********  File Path *************/
    public final String uploadFilePath = "";
    public final String uploadFileName = "";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
    
        /************* Php script path ****************/
      	upLoadServerUri = "http://192.168.1.112/upload.php";
    
        upload_audio = (Button)findViewById(R.id.upload_audio);
        uploadButton = (Button)findViewById(R.id.uploadButton);
        messageText  = (TextView)findViewById(R.id.messageText);
        Title = (EditText) findViewById (R.id.title);
        upload_audio.setOnClickListener(this);
        uploadButton.setOnClickListener(this);
        
    }
	
    public void onClick(View arg0) {
    	switch(arg0.getId()) {
    		case R.id.upload_audio:
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setType("audio/*");
		        intent.setAction(Intent.ACTION_GET_CONTENT);
		        startActivityForResult(Intent.createChooser(intent,"Select Audio "), SELECT_AUDIO);
		        break;
		        
    		case R.id.uploadButton:
    			dialog = ProgressDialog.show(Upload.this, "", "Uploading file...", true);
                
                new Thread(new Runnable() {
                        public void run() {
                             runOnUiThread(new Runnable() {
                                    public void run() {
                                    	messageText.setText("uploading started.....");
                                    	
                                    }
                                });
                             temp = selectedPath.split(delim);
                             fileName = temp[temp.length - 1];
                             Log.d("asdasd",fileName);
                             server();
                             uploadFile("" + selectedPath);
                                                     
                        }
                      }).start();
                	break;
                	
                default:
                	break;
                }
			}
    
    void server() {
    	
    	try
    	{
    		httpclient = new DefaultHttpClient();
			httppost = new HttpPost("http://192.168.1.112/upload_audio.php");
			nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("Title", Title.getText().toString().trim()));
			nameValuePairs.add(new BasicNameValuePair("fileName", fileName.trim()));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			Log.e("sent request", "ys");
			Log.e("executed httppost", "req");
			ResponseHandler<String> responsehanlder=new BasicResponseHandler();
			final String response = httpclient.execute(httppost,responsehanlder);
			System.out.println("Response : "+ response);
			Log.e("got request", "sdasd");
			runOnUiThread(new Runnable(){
				public void run(){
					Toast.makeText(getApplicationContext(),"Uploaded Succesfully",Toast.LENGTH_SHORT).show();
				}	
			});
			startActivity(new Intent(Upload.this, HomePage.class));
    	}
    	catch(Exception e)
		{
			System.out.println("Exception : " + e.getMessage());
		}	
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
		 
        if (resultCode == RESULT_OK) {
 
            if (requestCode == SELECT_AUDIO) {
                System.out.println("SELECT_AUDIO");
                Uri selectedImageUri = data.getData();
                selectedPath = getPath(selectedImageUri);
                System.out.println("SELECT_AUDIO Path : " + selectedPath);
                messageText.setText("Uploading file path :- '" + selectedPath + "'");
            }
 
        }
    }
    
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Audio.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    
    public int uploadFile(String sourceFileUri) {
    	
    	  String fileName = sourceFileUri;
 
          HttpURLConnection conn = null;
          DataOutputStream dos = null;  
          String lineEnd = "\r\n";
          String twoHyphens = "--";
          String boundary = "*****";
          int bytesRead, bytesAvailable, bufferSize;
          byte[] buffer;
          int maxBufferSize = 1 * 1024 * 1024; 
          File sourceFile = new File(sourceFileUri); 
          
          if (!sourceFile.isFile()) {
        	  
	           dialog.dismiss(); 
	           
	           Log.e("uploadFile", "Source File not exist :"
	        		               + selectedPath);
	           
	           runOnUiThread(new Runnable() {
	               public void run() {
	            	   messageText.setText("Source File not exist :"
	            			   + selectedPath);
	               }
	           }); 
	           
	           return 0;
           
          }
          else
          {
	           try { 
	        	   
	            	 // open a URL connection to the Servlet
	               FileInputStream fileInputStream = new FileInputStream(sourceFile);
	               URL url = new URL(upLoadServerUri);
	               
	               // Open a HTTP  connection to  the URL
	               conn = (HttpURLConnection) url.openConnection(); 
	               conn.setDoInput(true); // Allow Inputs
	               conn.setDoOutput(true); // Allow Outputs
	               conn.setUseCaches(false); // Don't use a Cached Copy
	               conn.setRequestMethod("POST");
	               conn.setRequestProperty("Connection", "Keep-Alive");
	               conn.setRequestProperty("ENCTYPE", "multipart/form-data");
	               conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
	               conn.setRequestProperty("uploaded_file", fileName); 
	               
	               dos = new DataOutputStream(conn.getOutputStream());
	     
	               dos.writeBytes(twoHyphens + boundary + lineEnd); 
	               dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
	            		                     + fileName + "\"; title=\"title\" " + lineEnd);
	               
	               dos.writeBytes(lineEnd);
	     
	               // create a buffer of  maximum size
	               bytesAvailable = fileInputStream.available(); 
	     
	               bufferSize = Math.min(bytesAvailable, maxBufferSize);
	               
	               buffer = new byte[bufferSize];
	     
	               // read file and write it into form...
	               bytesRead = fileInputStream.read(buffer, 0, (int) bufferSize);  
	                 
	               while (bytesRead > 0) {
	            	   
	                 dos.write(buffer, 0, bufferSize);
	                 bytesAvailable = fileInputStream.available();
	                 bufferSize = Math.min(bytesAvailable, maxBufferSize);
	                 bytesRead = fileInputStream.read(buffer, 0, bufferSize);   
	                 
	                }
	     
	               // send multipart form data necesssary after file data...
	               dos.writeBytes(lineEnd);
	               dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
	     
	               // Responses from the server (code and message)
	               serverResponseCode = conn.getResponseCode();
	               String serverResponseMessage = conn.getResponseMessage();
	                
	               Log.i("uploadFile", "HTTP Response is : " 
	            		   + serverResponseMessage + ": " + serverResponseCode);
	               
	               if(serverResponseCode == 200) {
	            	   
	                   runOnUiThread(new Runnable() {
	                        public void run() {
	                        	
	                        	String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
	                        		          + "http://192.168.1.112/uploads/" ;
	                        	
	                        	messageText.setText(msg);
	                            Toast.makeText(Upload.this, "File Upload Complete.", 
	                            		     Toast.LENGTH_SHORT).show();
	                        }
	                    });                
	               }    
	               
	               //close the streams //
	               fileInputStream.close();
	               dos.flush();
	               dos.close();
	                
	          } catch (MalformedURLException ex) {
	        	  
	              dialog.dismiss();  
	              ex.printStackTrace();
	              
	              runOnUiThread(new Runnable() {
	                  public void run() {
	                	  messageText.setText("MalformedURLException Exception : check script url.");
	                      Toast.makeText(Upload.this, "MalformedURLException", Toast.LENGTH_SHORT).show();
	                  }
	              });
	              
	              Log.e("Upload file to server", "error: " + ex.getMessage(), ex);  
	          } catch (Exception e) {
	        	  
	              dialog.dismiss();  
	              e.printStackTrace();
	              
	              runOnUiThread(new Runnable() {
	                  public void run() {
	                	  messageText.setText("Got Exception : see logcat ");
	                      Toast.makeText(Upload.this, "Got Exception : see logcat ", 
	                    		  Toast.LENGTH_SHORT).show();
	                  }
	              });
	              Log.e("Upload file to server Exception", "Exception : " 
	            		                           + e.getMessage(), e);  
	          }
	          dialog.dismiss();       
	          return serverResponseCode; 
	          
           } // End else block 
         } 
}