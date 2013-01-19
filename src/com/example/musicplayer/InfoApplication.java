package com.example.musicplayer;

import java.util.List;

import com.example.model.Mp3Info;

import android.app.Application;

public class InfoApplication extends Application{
	List<Mp3Info> allMp3Info;
	int startPosition;
	public int getStartPosition() {
		return startPosition;
	}
	public void setStartPosition(int startPosition) {
		this.startPosition = startPosition;
	}
	public List<Mp3Info> getMp3Infos(){
		return allMp3Info;
	}
	public void setMp3Infos(List<Mp3Info> mp3Infos){
		this.allMp3Info = mp3Infos;
	}
}
