package com.example.musicplayer.playorder;
import java.util.ArrayList;
import com.example.model.Mp3Info;
public class PlayOrderSorted extends PlayOrderStrategy{
	
	ArrayList<Mp3Info> musicList = new ArrayList<Mp3Info>();
	
	public PlayOrderSorted(ArrayList<Mp3Info> musicList){
		this.musicList = musicList;
	}
	@Override
	public Mp3Info getNextMusic(Mp3Info currentMusic) {
		// TODO Auto-generated method stub
		int position = musicList.indexOf(currentMusic);
        //current not in list, so return first one
        if(position < 0){
            return musicList.get(0);
        }
		int nextPosition = (position + 1) % musicList.size();
			
		return musicList.get(nextPosition);
		
	}

	@Override
	public Mp3Info getPreviousMusic(Mp3Info currentMusic) {
		// TODO Auto-generated method stub
		int position = musicList.indexOf(currentMusic);
		//current not in list, so return first one
        if(position < 0){
            return musicList.get(0);
        }
		int previousPosition = (position - 1) >= 0 ? (position - 1) :(position -1 + musicList.size());
			
		return musicList.get(previousPosition);
	}

}
