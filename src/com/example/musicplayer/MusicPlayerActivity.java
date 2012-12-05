package com.example.musicplayer;
import java.util.ArrayList;
import com.example.model.Mp3Info;
import com.example.musicplayer.service.MusicPlayerService;
import com.example.musicplayer.service.MusicPlayerService.LocalBinder;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
public class MusicPlayerActivity extends Activity{
	ImageButton btnStart = null;
	ImageButton btnStop = null;
	ImageButton btnPause = null;
	TextView txtLrcMessage = null;
	TextView lrcMessage1 = null;
	TextView lrcMessage2 = null;
	TextView lrcMessage3 = null;
	TextView lrcMessage4 = null;
	SeekBar seekBarMusic = null;
	ArrayList<Mp3Info> mp3InfoArrayList = null;
	Mp3Info mp3Info;
	NotificationManager manager;
	LrcMessageBoardcast lrcMessageBoardcast = new LrcMessageBoardcast();	
	MusicPlayerService mService;
	boolean mBound = false;
	boolean isPlaying = true;
	static final int notifyId = 11;
    private ServiceConnection mConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className,
                IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            LocalBinder binder = (LocalBinder) service;
            mService = binder.getService();
            seekBarMusic.setMax(mService.getMusicPlayLength());
            mBound = true;
        }

        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
    
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		
	}
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	    if (mBound) {
	          unbindService(mConnection);
	          mBound = false;
	    }
	    Log.e("musicplayer", "activity destory");
	}


	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub		
		super.onRestart();

	}


	@Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService
 
		
    }
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		this.unregisterReceiver(lrcMessageBoardcast);
		
		super.onStop();	
		
	}
	private void creatNotification(){
		NotificationCompat.Builder mBuilder =
		        new NotificationCompat.Builder(this)
		        .setSmallIcon(R.drawable.ic_launcher)
		        .setContentTitle("My notification")
		        .setContentText("’˝‘⁄≤•∑≈“Ù¿÷");
		Intent notificationIntent = new Intent(this, MusicPlayerActivity.class);  
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		// Adds the back stack for the Intent (but not the Intent itself)
		stackBuilder.addParentStack(MainActivity.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(notificationIntent);
		PendingIntent resultPendingIntent =
		        stackBuilder.getPendingIntent(
		            0,
		            PendingIntent.FLAG_UPDATE_CURRENT
		        );
		mBuilder.setContentIntent(resultPendingIntent);
		manager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(notifyId, mBuilder.getNotification());
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.music_player_activity);
		btnStart = (ImageButton) findViewById(R.id.btnStart);
		btnStop = (ImageButton) findViewById(R.id.btnStop);
		btnPause = (ImageButton) findViewById(R.id.btnPause);
		txtLrcMessage = (TextView)findViewById(R.id.txtLrcMessage);
		lrcMessage1 = (TextView)findViewById(R.id.txtLrc1);
		lrcMessage2 = (TextView)findViewById(R.id.txtLrc2);
		lrcMessage3 = (TextView)findViewById(R.id.txtLrc3);
		lrcMessage4 = (TextView)findViewById(R.id.txtLrc4);
		seekBarMusic = (SeekBar)findViewById(R.id.seekBarMusic);
		seekBarMusic.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			public void onStopTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
			}
			
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
			}
			
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
				// TODO Auto-generated method stub
				if(arg1 == seekBarMusic.getMax() -1 ){
					seekBarMusic.setProgress(0);
					txtLrcMessage.setText("");
				}
				if(arg2 == true){
					mService.setProgress(arg1);
				}				
			}
		});
		int position = 0;
		Intent mp3InfoIntent = getIntent();
		mp3InfoArrayList = mp3InfoIntent.getParcelableArrayListExtra("AllMp3Info");	
		mp3InfoIntent.getIntExtra("mp3Position", 0);
		mp3Info = mp3InfoArrayList.get(position);
		IntentFilter  intentfilter = new IntentFilter(ConstantMessage.PlayerMessage.action_name);
		this.registerReceiver(lrcMessageBoardcast, intentfilter);	
	    Intent intent = new Intent(this, MusicPlayerService.class);
	    intent.putExtra("mp3Info", mp3Info);
	    bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
	    
	   
	}
	
	public void	startMusicPlay(View v){
		creatNotification();
		Intent intent =new Intent();
		intent.setClass(this, MusicPlayerService.class);
		intent.putExtra("mp3Info", mp3Info);
		intent.putExtra("message", ConstantMessage.PlayerMessage.PLAY_MSG);
		startService(intent);
		seekBarMusic.setMax(mService.getMusicPlayLength());
		
	}
	public void	pauseMusicPlay(View v){
		if(isPlaying && manager !=  null){
			manager.cancel(notifyId);
			isPlaying = false;
		}
		else{
			isPlaying = true;
			 creatNotification();
		}
		Intent intent =new Intent();
		intent.setClass(this, MusicPlayerService.class);
		intent.putExtra("mp3Info", mp3Info);
		intent.putExtra("message", ConstantMessage.PlayerMessage.PAUSE_MSG);
		startService(intent);
	}
	public void	stopMusicPlay(View v){
		manager.cancel(notifyId);
		Intent intent =new Intent();
		intent.setClass(this, MusicPlayerService.class);
		intent.putExtra("mp3Info", mp3Info);
		intent.putExtra("message", ConstantMessage.PlayerMessage.STOP_MSG);
		startService(intent);
		stopService(intent);

	}
	public class LrcMessageBoardcast extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String message = intent.getStringExtra("lrcMessage");
			String Message1 = intent.getStringExtra("lrc1");
			String Message2 = intent.getStringExtra("lrc2");
			String Message3 = intent.getStringExtra("lrc3");
			String Message4 = intent.getStringExtra("lrc4");
			String end = intent.getStringExtra("EndMessage");
			if(end != null && end.endsWith("end")){
				lrcMessage1.setText("");
				lrcMessage2.setText("");
				lrcMessage3.setText("");
				lrcMessage4.setText("");
				txtLrcMessage.setText("");
				seekBarMusic.setProgress(0);
			}
			lrcMessage1.setText(Message1);
			lrcMessage2.setText(Message2);
			lrcMessage3.setText(Message3);
			lrcMessage4.setText(Message4);
			txtLrcMessage.setText(message);
			seekBarMusic.setProgress(mService.getMusicPlayCurrentLength());
			
		}

	}
}