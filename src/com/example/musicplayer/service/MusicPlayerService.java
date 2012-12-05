package com.example.musicplayer.service;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import com.example.model.Mp3Info;
import com.example.musicplayer.ConstantMessage;
import com.example.util.LrcProcessor;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Binder;
import android.os.Environment;
import android.os.Handler;
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
	ArrayList<ArrayList> time_message = null;
	private MediaPlayer mediaPlayer = null; 
	Mp3Info mp3Info =null;
	boolean isPlaying = false;
	long beginMill = 0;
	long pauseMill = 0;
	long setMill = 0;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		mp3Info = (Mp3Info) intent.getParcelableExtra("mp3Info");
		String mp3Path = Environment.getExternalStorageDirectory() + File.separator+"mp3"+File.separator + mp3Info.getMp3Name();
		mediaPlayer =MediaPlayer.create(this, Uri.parse("file://"+mp3Path));
		mediaPlayer.setLooping(false);	
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {			
			public void onCompletion(MediaPlayer arg0) {
				// TODO Auto-generated method stub
				lrcHandler.removeCallbacks(updateLrcMessgae);
				Intent intent = new Intent();
				intent.setAction(ConstantMessage.PlayerMessage.action_name);
				intent.putExtra("EndMessage", "end");
				sendBroadcast(intent);
			}
		});
		return mBinder;
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		mp3Info = (Mp3Info) intent.getParcelableExtra("mp3Info");
		int MSG = (int)intent.getIntExtra("message", 0);
		if(mp3Info != null){
			if(MSG == ConstantMessage.PlayerMessage.PLAY_MSG)
			{
				play(mp3Info);
			}
			else if(MSG == ConstantMessage.PlayerMessage.PAUSE_MSG){
				pause();
			}
			else{
				stop();
			}
		}
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		mediaPlayer.release();
		Log.e("musicplayer", "service destory");
		super.onDestroy();
	}

	public void play(Mp3Info mp3Info){
		String mp3Path = Environment.getExternalStorageDirectory() + File.separator+"mp3"+File.separator + mp3Info.getMp3Name();
		try {
			prepareLrc();
			mediaPlayer.reset();
			mediaPlayer.setDataSource(mp3Path);
			updateLrcMessgae =new UpdateLrcMessage();
			beginMill = System.currentTimeMillis();
			lrcHandler.postDelayed(updateLrcMessgae, 5);
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
				lrcHandler.removeCallbacks(updateLrcMessgae);
				pauseMill = System.currentTimeMillis();					
			}else{			
				mediaPlayer.start();
				beginMill += (System.currentTimeMillis() - pauseMill);
				lrcHandler.postDelayed(updateLrcMessgae, 5);
			}			
		}

		isPlaying = isPlaying ? false :true;
	}
	public void stop(){
		beginMill = 0;
		pauseMill = 0;
		lrcHandler.removeCallbacks(updateLrcMessgae);
		mediaPlayer.stop();
	}
	private void prepareLrc(){
		LrcProcessor lrcProccessor = new LrcProcessor();
		String mp3Path = Environment.getExternalStorageDirectory() + File.separator+"mp3"+File.separator + mp3Info.getLrcName();
		File lrc = new File(mp3Path);
		try {
			time_message = lrcProccessor.process(new FileInputStream(lrc));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	Handler lrcHandler = new Handler();
	UpdateLrcMessage updateLrcMessgae;
	class UpdateLrcMessage implements Runnable{
		private long nextMill = 0;
		private String lrcMessage;
		private String lrc1 ="";
		private String lrc2 ="";
		private String lrc3 ="";
		private String lrc4 ="";
		ArrayList<Long> timeMill ;
		ArrayList<String> message ;
		
		public UpdateLrcMessage(){
			timeMill = time_message.get(0);
			message = time_message.get(1);
			nextMill = timeMill.get(0);
			lrcMessage = message.get(0);
		}
		public void run() {
			// TODO Auto-generated method stub			
			if(setMill != 0){
				beginMill -=  (setMill -(System.currentTimeMillis() - beginMill));
				setMill = 0;
			}
			long offset = System.currentTimeMillis() - beginMill;			
			Intent intent = new Intent();
			intent.setAction(ConstantMessage.PlayerMessage.action_name);
			intent.putExtra("lrcMessage", lrcMessage);
			intent.putExtra("lrc1", lrc1);
			intent.putExtra("lrc2", lrc2);
			intent.putExtra("lrc3", lrc3);
			intent.putExtra("lrc4", lrc4);
			sendBroadcast(intent);	
			for(int i=0;i < timeMill.size()-1;i++){
				long tempMill = timeMill.get(i);
				String tempMessage = message.get(i);

				if(tempMill - offset <= 65 && tempMill -offset >= 0){
					nextMill = tempMill;
					lrcMessage = tempMessage;
					if(i-2 >=0){
						lrc1 = message.get(i-2);
					}
					else{
						lrc1 = " ";
					}
					if(i-1 >=0){
						lrc2 = message.get(i-1);
					}
					else{
						lrc2 = " ";
					}
					if((i + 1) <= (message.size() -1)){
						lrc3 = message.get(i+1);
					}
					else{
						lrc3 = "  ";
					}
					if((i + 2) <= (message.size() -1)){
						lrc4 = message.get(i + 2);
					}
					else{
						lrc4 = "  ";
					}

					break;
				}
			}
	
			lrcHandler.postDelayed(updateLrcMessgae, 1);
		}
	}
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
}
