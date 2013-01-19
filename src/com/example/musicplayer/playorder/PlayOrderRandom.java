package com.example.musicplayer.playorder;

import java.util.ArrayList;
import java.util.Stack;
import com.example.model.Mp3Info;
public class PlayOrderRandom extends PlayOrderStrategy{
	ArrayList<Mp3Info> musicList = new ArrayList<Mp3Info>();
	final Stack<Mp3Info> history =new Stack<Mp3Info>();
	public PlayOrderRandom(ArrayList<Mp3Info> musicList) {
		super();
		this.musicList = musicList;
	}

	@Override
	public Mp3Info getNextMusic(Mp3Info currentMusic) {
		// TODO Auto-generated method stub
		int position =  (int) (Math.random()*100 % musicList.size());
		
		Mp3Info nextMusic = musicList.get(position);
		while(nextMusic.equals(currentMusic)){
			position =  (int) (Math.random()*100 % musicList.size());
			nextMusic = musicList.get(position);
		}
		history.push(currentMusic);
		return nextMusic;
	}

	@Override
	public Mp3Info getPreviousMusic(Mp3Info currentMusic) {
		// TODO Auto-generated method stub
		Mp3Info nextMusic = null;
		if(history.size() > 0){
			nextMusic = history.pop();
		}else{
			nextMusic = currentMusic;
		}
		return nextMusic;
	}

}
