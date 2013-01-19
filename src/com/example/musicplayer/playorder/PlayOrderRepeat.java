package com.example.musicplayer.playorder;

import com.example.model.Mp3Info;

public class PlayOrderRepeat extends PlayOrderStrategy{

	@Override
	public Mp3Info getNextMusic(Mp3Info currentMusic) {
		// TODO Auto-generated method stub
		return currentMusic;
	}

	@Override
	public Mp3Info getPreviousMusic(Mp3Info currentMusic) {
		// TODO Auto-generated method stub
		return currentMusic;
	}

	
}
