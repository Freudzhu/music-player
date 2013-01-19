package com.example.preference;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
public class Preference {
	private static Preference instance = null;
	final Context context;
	private Preference(Context context){
		this.context = context;
	}
	public static Preference getInstance(Context context){
		if(instance == null){
			 synchronized (Preference.class) {
				 if (instance == null) { 
					 instance =  new Preference(context);
				 }
			 }
		}
		return instance;
	}
	public void setShuffle(boolean shuffle){
		SharedPreferencesAcess.putValue(context, Keys.SHUFFLE, shuffle);
		notifyObserver(Keys.SHUFFLE);
	}
	public boolean getShuffle(){
		return SharedPreferencesAcess.getValue(context, Keys.SHUFFLE);
	}
	public boolean getRepeat()
	{
		return SharedPreferencesAcess.getValue(context, Keys.REPEAT);
	}

	public void setRepeat(boolean nRepeat)
	{
		SharedPreferencesAcess.putValue(context, Keys.REPEAT, nRepeat);
		notifyObserver(Keys.REPEAT);
	}
	public void setMusicDir(String dirName){
		SharedPreferencesAcess.putValue(context, Keys.MUSICDIR, dirName);
		notifyObserver(Keys.MUSICDIR);
	}
	public String getMusicDir(){		
		notifyObserver(Keys.REPEAT);
		return SharedPreferencesAcess.getValue(context, Keys.MUSICDIR);
	}
	private void notifyObserver(Key key){
		for(PreferenceObserver o :listObserver){
			o.change(key);
		}
	}
	List<PreferenceObserver> listObserver = new ArrayList<PreferenceObserver>();
	public interface PreferenceObserver{
		public void change(Key key);
	}
	public void addObserver(PreferenceObserver o){
		if(o != null)
			listObserver.add(o);
	}
	public void removeObserver(PreferenceObserver o){
		if(o != null)
			listObserver.remove(o);
	}
}
