package com.example.musicplayer.playlist;
import java.util.ArrayList;
import com.example.model.Mp3Info;
import com.example.musicplayer.playorder.PlayOrderRandom;
import com.example.musicplayer.playorder.PlayOrderRepeat;
import com.example.musicplayer.playorder.PlayOrderSorted;
import com.example.musicplayer.playorder.PlayOrderStrategy;
import com.example.preference.Key;
import com.example.preference.Keys;
import com.example.preference.Preference;
import com.example.preference.Preference.PreferenceObserver;
public class MusicPlayList {
		ArrayList<Mp3Info> musicList;
		Preference  preference;
		PlayOrderStrategy playOrder;
		public MusicPlayList(ArrayList<Mp3Info> musicList,Preference preference){
			this.musicList = musicList;
			this.preference = preference;
			init();
		}
		private void init(){
			preference.addObserver(new PreferenceObserver() {
				
				public void change(Key key) {
					// TODO Auto-generated method stub
					setPlayOrderStrategyAccordingPreferrentChange(key);
				}
			});
			setPlayOrderStrategy();
			
		}
		private void setPlayOrderStrategyAccordingPreferrentChange(Key key){
			if(preference.getRepeat()){
				playOrder =  new PlayOrderRepeat();
			}
			else if(preference.getShuffle()){
				playOrder =  new PlayOrderRandom(musicList);
			}
			else{
				playOrder =  new PlayOrderSorted(musicList);
			}
		}
		private void setPlayOrderStrategy(){
			playOrder = preference.getShuffle() ? new PlayOrderRandom(musicList) : new PlayOrderSorted(musicList);
		}
		public Mp3Info getNextMusic(Mp3Info currentMusic){
			return playOrder.getNextMusic(currentMusic);
		}
		public Mp3Info getPreviousMusic(Mp3Info currentMusic){
			return playOrder.getPreviousMusic(currentMusic);
		}
}
