package com.example.musicplayer.service;
import java.io.IOException;
import java.util.ArrayList;
import com.example.constant.ConstantMessage;
import com.example.model.Mp3Info;
import com.example.musicplayer.InfoApplication;
import com.example.musicplayer.PlayerActivity;
import com.example.musicplayer.R;
import com.example.musicplayer.playlist.MusicPlayList;
import com.example.preference.Preference;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
public class MusicPlayerService extends Service{
	private final IBinder mBinder = new LocalBinder();
	public class LocalBinder extends Binder {
		public MusicPlayerService getService() {
            // Return this instance of LocalService so clients can call public methods
            return MusicPlayerService.this;
        }
    }	
	private MediaPlayer mediaPlayer = null; 
	Preference preferences;
	ArrayList<Mp3Info > mp3Infos;
	MusicPlayList musicPlayList;
	Mp3Info mp3Info =null;
	private static final int ONGOING_NOTIFICATION = 1;
	private Notification notification;
	NotificationManager mNotificationManager;
			
	InfoApplication applicationInfos;
	boolean isPlaying = false;
	long beginMill = 0;
	long pauseMill = 0;
	long setMill = 0;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
				
		mediaPlayer =MediaPlayer.create(this, Uri.parse("file://"+mp3Info.getSrc()));
		mediaPlayer.setLooping(false);
			
		isPlaying = true;	
		try {
			mediaPlayer.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mediaPlayer.start();
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {			
			public void onCompletion(MediaPlayer arg0) {
				// TODO Auto-generated method stub
				isPlaying = false;
				Intent music_end = new Intent();
				music_end.setAction(ConstantMessage.PlayerMessage.music_completion);
				sendBroadcast(music_end);
				
			}
		});
		return mBinder;
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		
		int MSG = (int)intent.getIntExtra("message", 0);
		if(MSG == ConstantMessage.PlayerMessage.PLAY_MSG){
			int position = applicationInfos.getStartPosition();
			mp3Info = mp3Infos.get(position);
 			if(mp3Info != null)
 				play(mp3Info);		
		}
		else if(MSG == ConstantMessage.PlayerMessage.PLAY_NEXT_MSG){
			if(musicPlayList != null){
				mp3Info = musicPlayList.getNextMusic(mp3Info);
 				if(mp3Info != null){
 					play(mp3Info);
 				}
 			}	
		}
		else if(MSG == ConstantMessage.PlayerMessage.PLAY_BACK_MSG){
			if(musicPlayList != null){
				mp3Info = musicPlayList.getPreviousMusic(mp3Info);
 				if(mp3Info != null){
 					play(mp3Info);
 				}
 			}	
		}
		else if(MSG == ConstantMessage.PlayerMessage.PAUSE_MSG){
			pause();
		}
		else{
			stop();
		}
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		applicationInfos = (InfoApplication)getApplication();
		mp3Infos = (ArrayList<Mp3Info>) applicationInfos.getMp3Infos();
		int position = applicationInfos.getStartPosition();
		mp3Info = mp3Infos.get(position);
		preferences = Preference.getInstance(getApplicationContext());
		musicPlayList = new MusicPlayList(mp3Infos,preferences);
		
		this.notification = new Notification(R.drawable.ic_launcher, getText(R.string.app_name),System.currentTimeMillis());
        Intent notificationIntent = new Intent(this, PlayerActivity.class);
        notificationIntent.putExtra("lanchMode", "notification");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        this.notification.setLatestEventInfo(this, getText(R.string.app_name), mp3Info.getMp3Name(), pendingIntent);
        
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        	// mId allows you to update the notification later on.
        mNotificationManager.notify(ONGOING_NOTIFICATION,this.notification);
        startForeground(ONGOING_NOTIFICATION, this.notification);
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		mediaPlayer.release();
		Log.e("musicplayer", "service destory");
		super.onDestroy();
	}

	public void play(Mp3Info mp3Info){
        Intent notificationIntent = new Intent(this, PlayerActivity.class);
        notificationIntent.putExtra("lanchMode", "notification");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
		this.notification.setLatestEventInfo(this, getText(R.string.app_name), mp3Info.getMp3Name(), pendingIntent);
		mNotificationManager.notify(ONGOING_NOTIFICATION,this.notification);
		try {			
			mediaPlayer.reset();
			mediaPlayer.setDataSource(mp3Info.getSrc());
			isPlaying = true;	
			mediaPlayer.prepare();
			mediaPlayer.start();
				
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	}
	public void pause(){
		if(mediaPlayer != null){
			if(isPlaying){
				mediaPlayer.pause();			
			}else{			
				mediaPlayer.start();	
			}			
		}
		isPlaying = isPlaying ? false :true;
	}
	public void stop(){
		mediaPlayer.stop();
	}

	//the interface to service
	public int getMusicPlayCurrentLength(){
		if(mediaPlayer != null){
			return mediaPlayer.getCurrentPosition();
		}
		return 0;	
	}
	public int getMusicPlayLength(){
		if(mediaPlayer != null){
			return mediaPlayer.getDuration();
		}
		return 0;
	}
	public void setProgress(int progress){
		mediaPlayer.seekTo(progress);
		setMill = progress;
		mediaPlayer.start();
		
	}
	public boolean getPlayState(){
		return isPlaying;
	}
	public Mp3Info getCurrentMp3Info(){
		return mp3Info;
	}
}
