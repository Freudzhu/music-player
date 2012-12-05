package com.example.musicplayer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.example.model.Mp3Info;
import com.example.util.FileUtil;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
public class LocalMp3ListActivity extends ListActivity{
	
		List<Mp3Info> infos =  null;
	 	@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.local_mp3list);
	        
	    }

		@Override
		protected void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			updateListView();
		}
		
		private SimpleAdapter buildSimpleAdapter(List<Mp3Info> mp3Info){
			List<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();
			for(Mp3Info m : mp3Info){
				HashMap<String,String> map = new HashMap<String, String>();
				map.put("mp3_name", m.getMp3Name());
				map.put("mp3_size", m.getMp3Size());
				list.add(map);
			}
			SimpleAdapter simpleAdapter = new SimpleAdapter(this, list, R.layout.mp3list_item, new String[]{"mp3_name","mp3_size"},new int[]{R.id.mp3_name,R.id.mp3_szie});
			return simpleAdapter;
		}
		private void updateListView(){
			
			FileUtil fileUtil =new FileUtil();
			infos = fileUtil.getMp3File("mp3");
			SimpleAdapter simpleAdapter = buildSimpleAdapter(infos);
			this.setListAdapter(simpleAdapter);
		}
		protected void onListItemClick(ListView l, View v, int position, long id) {
				Intent playMusic =new Intent();
				playMusic.putParcelableArrayListExtra("AllMp3Info", (ArrayList<? extends Parcelable>) infos);
				playMusic.putExtra("mp3Position", position);
				playMusic.setClass(this, MusicPlayerActivity.class);
				startActivity(playMusic);
		}
	 	
}
