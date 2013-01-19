package com.example.musicplayer.playorder;

import com.example.model.Mp3Info;

public abstract class PlayOrderStrategy {
	 public abstract Mp3Info getNextMusic(Mp3Info currentMusic);
	 public abstract Mp3Info getPreviousMusic(Mp3Info currentMusic);
}
