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
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class SongPage extends Activity implements OnClickListener,
		OnTouchListener, OnCompletionListener, OnBufferingUpdateListener {
	private Button btn_play, btn_pause, btn_stop, btn_like, btn_dislike, home;
	private SeekBar seekBar;
	private int liked = 0;
	private int disliked = 0;
	private MediaPlayer mediaPlayer;
	private int lengthOfAudio;
	int id;
	int likes,dislikes,views;
	String title,url;
	Bundle extras;
	HttpPost httppost;
	HttpResponse response;
	HttpClient httpclient;
	List<NameValuePair> nameValuePairs;
	TextView nlike,ndislike,nview,song;
	
	Button log;
	

	
	//private final String URL = "http://android.erkutaras.com/media/audio.mp3";
	private String URL;

	private final Handler handler = new Handler();
	private final Runnable r = new Runnable() {
		@Override
		public void run() {
			updateSeekProgress();
		}
	};

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_song_page);
		extras = getIntent().getExtras();
		id = extras.getInt("id");
		title = extras.getString("title");
		url = extras.getString("url");
		likes = extras.getInt("likes");
		dislikes = extras.getInt("dislikes");
		views = extras.getInt("views");
		URL = url;
		views = views+1;
		init();
		song = (TextView) findViewById (R.id.textview1);
		nlike = (TextView) findViewById(R.id.num_likes);
		ndislike = (TextView) findViewById(R.id.num_dislikes);
		nlike.setText(""+likes);
		ndislike.setText(""+dislikes);
		song.setText(title);
		//TextView tvId = (TextView) findViewById(R.id.num_views);
		
		log = (Button) findViewById(R.id.log);
		log.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				startActivity(new Intent(SongPage.this,MainActivity.class));
			}
		});
	}

	private void init() {
		btn_play = (Button) findViewById(R.id.btn_play);
		btn_play.setOnClickListener(this);
		btn_pause = (Button) findViewById(R.id.btn_pause);
		btn_pause.setOnClickListener(this);
		btn_pause.setEnabled(false);
		btn_stop = (Button) findViewById(R.id.btn_stop);
		btn_stop.setOnClickListener(this);
		btn_stop.setEnabled(false);
		btn_like = (Button) findViewById(R.id.btn_like);
		btn_dislike = (Button) findViewById(R.id.btn_dislike);
		btn_like.setBackgroundColor(Color.RED);
		btn_dislike.setBackgroundColor(Color.RED);
		btn_like.setOnClickListener(this);
		btn_dislike.setOnClickListener(this);
		home = (Button) findViewById (R.id.backButton);
		home.setOnClickListener(this);
		
		seekBar = (SeekBar) findViewById(R.id.seekBar);
		seekBar.setOnTouchListener(this);
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setOnBufferingUpdateListener(this);
		mediaPlayer.setOnCompletionListener(this);

	}

	@Override
	public void onBufferingUpdate(MediaPlayer mediaPlayer, int percent) {
		seekBar.setSecondaryProgress(percent);
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		btn_play.setEnabled(true);
		btn_pause.setEnabled(false);
		btn_stop.setEnabled(false);
		seekBar.setProgress(0);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (mediaPlayer.isPlaying()) {
			SeekBar tmpSeekBar = (SeekBar) v;
			mediaPlayer
					.seekTo((lengthOfAudio / 100) * tmpSeekBar.getProgress());
		}
		return false;
	}

	@Override
	public void onClick(View view) {
		
		try {
			mediaPlayer.setDataSource(URL);
			mediaPlayer.prepare();
			lengthOfAudio = mediaPlayer.getDuration();
		} catch (Exception e) {

			Log.d("Error", " oooohhhh no");
		}

		switch (view.getId()) {
		case R.id.btn_play:
			playAudio();
			break;
		case R.id.btn_pause:
			pauseAudio();
			break;
		case R.id.btn_stop:
			stopAudio();
			break;
		case R.id.btn_like:
			like();
			break;
		case R.id.btn_dislike:
			dislike();
			break;
		case R.id.backButton:
		/*	new Thread(new Runnable(){
				public void run(){
					server();
				}
			}).start(); */
			Intent i = new Intent(SongPage.this, HomePage.class);
			startActivity(i);
			break;
		default:
			break;
		}

		updateSeekProgress();
	}

	private void updateSeekProgress() {
		if (mediaPlayer.isPlaying()) {
			seekBar.setProgress((int) (((float) mediaPlayer
					.getCurrentPosition() / lengthOfAudio) * 100));
			handler.postDelayed(r, 1000);
		}
	}

	private void stopAudio() {
		mediaPlayer.stop();
		btn_play.setEnabled(true);
		btn_pause.setEnabled(false);
		btn_stop.setEnabled(false);
		seekBar.setProgress(0);
	}

	private void pauseAudio() {
		mediaPlayer.pause();
		btn_play.setEnabled(true);
		btn_pause.setEnabled(false);
	}

	private void playAudio() {
		mediaPlayer.start();
		btn_play.setEnabled(false);
		btn_pause.setEnabled(true);
		btn_stop.setEnabled(true);
	}

	public void like() {
		if (liked == 0) {
			liked = 1;
			TextView tvId = (TextView) findViewById(R.id.num_likes);
			String s = (String) tvId.getText();
			int l = Integer.parseInt(s);
			l = l + 1;
			likes = likes + 1;
			tvId.setText("" + l);
			System.out.print(liked);
			btn_like.setBackgroundColor(Color.GREEN);
		}
		else if (liked == 1) {
			liked = 0;
			TextView tvId = (TextView) findViewById(R.id.num_likes);
			String s = (String) tvId.getText();
			int l = Integer.parseInt(s);
			l = l - 1;
			likes = likes - 1;
			tvId.setText("" + l);
			System.out.print(liked);
			btn_like.setBackgroundColor(Color.RED);
		}
		if (disliked == 1) {
			liked = 1;
			disliked = 0;
			TextView tvId2 = (TextView) findViewById(R.id.num_dislikes);
			String s2 = (String) tvId2.getText();
			int l2 = Integer.parseInt(s2);
			l2 = l2 - 1;
			dislikes = dislikes - 1;
			tvId2.setText("" + l2);
			System.out.print(liked);
			System.out.print(disliked);
			btn_dislike.setBackgroundColor(Color.RED);
		}
	}

	public void dislike() {
		if (disliked == 0) {
			disliked = 1;
			TextView tvId = (TextView) findViewById(R.id.num_dislikes);
			String s = (String) tvId.getText();
			int l = Integer.parseInt(s);
			l = l + 1;
			dislikes = dislikes + 1;
			tvId.setText("" + l);
			System.out.print(disliked);
			btn_dislike.setBackgroundColor(Color.GREEN);
		}
		else if (disliked == 1) {
			disliked = 0;
			TextView tvId = (TextView) findViewById(R.id.num_dislikes);
			String s = (String) tvId.getText();
			int l = Integer.parseInt(s);
			l = l - 1;
			dislikes = dislikes - 1;
			tvId.setText("" + l);
			System.out.print(disliked);
			btn_dislike.setBackgroundColor(Color.RED);
		}
		if (liked == 1) {
			disliked = 1;
			liked = 0;
			TextView tvId1 = (TextView) findViewById(R.id.num_likes);
			String s1 = (String) tvId1.getText();
			int l1 = Integer.parseInt(s1);
			l1 = l1 - 1;
			likes = likes - 1;
			tvId1.setText("" + l1);
			System.out.print(liked);
			System.out.print(disliked);
			btn_like.setBackgroundColor(Color.RED);
		}
	}
	
/*	void server() {
		try {
			httpclient= new DefaultHttpClient();
			httppost = new HttpPost("http://192.168.1.112/retrieve.php");
			nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("title", title));
			nameValuePairs.add(new BasicNameValuePair("views", ""+views));
			nameValuePairs.add(new BasicNameValuePair("likes", ""+likes));
			nameValuePairs.add(new BasicNameValuePair("dislikes", ""+dislikes));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			Log.e("sent request", "ys");
			Log.e("got request", "sdasd");
			ResponseHandler<String> responsehanlder=new BasicResponseHandler();
			final String response = httpclient.execute(httppost,responsehanlder);
			if(response.equalsIgnoreCase("Success")) {
				Toast.makeText(getApplicationContext(),"Success",Toast.LENGTH_SHORT).show();
			}
		}
		catch(Exception e){
			System.out.println("Exception : " + e.getMessage());
		}
	}*/
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.song_page, menu);
		return true;
	}

}