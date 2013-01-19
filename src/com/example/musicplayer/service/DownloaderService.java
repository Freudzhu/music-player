package com.example.musicplayer.service;
import com.example.downloader.HttpDownLoader;
import com.example.model.Mp3Info;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
public class DownloaderService extends Service{	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Mp3Info mp3Info = (Mp3Info) intent.getParcelableExtra("mp3Info");
		Thread t = new Thread(new DownMp3Tread(mp3Info));
		t.start();
		return super.onStartCommand(intent, flags, startId);
	}
	class DownMp3Tread implements Runnable{
		Mp3Info mp3Info = null;
		public DownMp3Tread(Mp3Info mp3Info) {
			super();	
			this.mp3Info = mp3Info;
		}

		public void run() {
			
			String mp3Url = "http://10.0.2.2:8080/mp3/" + mp3Info.getMp3Name();			
			HttpDownLoader hdl = new HttpDownLoader();
			int result = hdl.downLoadFile("mp3", mp3Info.getMp3Name(),mp3Url);
			String resultMessage = null;
			if(result == 1){
				resultMessage = "下载成功";
			}
			else if(result == -1){
				resultMessage="下载失败";
			}else{
				resultMessage="文件已经存在，不用重复 下载";
			}
			
			String lrc3Url = "http://10.0.2.2:8080/mp3/" + mp3Info.getLrcName();
			int lrcResult = hdl.downLoadFile("mp3", mp3Info.getLrcName(),lrc3Url);
			if(lrcResult == 1){
				resultMessage = "下载成功";
			}
			else if(lrcResult == -1){
				resultMessage="下载失败";
			}
			else{
				resultMessage="文件已经存在，不用重复 下载";
			}
			
		}
	}
}
