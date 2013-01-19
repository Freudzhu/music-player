package com.example.musicplayer;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.example.musicplayer.InfoApplication;
import com.example.constant.ViewHolder;
import com.example.model.Mp3Info;
import com.example.preference.Preference;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
public class LocalMp3ListActivity extends ListActivity{
	
		List<Mp3Info> infos =  null;
		InfoApplication applicationInfos;
		private String currentMusicDir = "";
	    private EditText inputSearch;
		private MusicListAdapter musicDataAdapter;
		LinearLayout emptyView ;
		LinearLayout fileList;
		Button addMusic = null;
	 	@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.local_mp3list);
	        init();
	    }
		@Override
		protected void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			updateListView(currentMusicDir);
			applicationInfos = (InfoApplication)getApplication();
			applicationInfos.setMp3Infos(infos);
			
		}
		void init(){
	        emptyView = (LinearLayout) findViewById(R.id.empty_view);
	        inputSearch = (EditText) findViewById(R.id.inputSearch);
	        fileList = (LinearLayout) findViewById(R.id.file_list);
	        fileList.setFocusable(true);
	        fileList.setFocusableInTouchMode(true);
	        fileList.requestFocus();
			addMusic = (Button) findViewById(R.id.addMusic);
			addMusic.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent addMusic =new Intent();
					addMusic.setClass(LocalMp3ListActivity.this, FileViewActivity.class);
					addMusic.putExtra("initialDir", currentMusicDir);
					startActivityForResult(addMusic,0);
					
				}
			});
			inputSearch.addTextChangedListener(new TextWatcher() {
				 
			    public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
			        // When user changed the Text
			        (LocalMp3ListActivity.this.musicDataAdapter.getFilter()).filter(cs);
			    }
			 
			    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			            int arg3) {
			        // TODO Auto-generated method stub
			 
			    }
			 
			    public void afterTextChanged(Editable arg0) {
			        // TODO Auto-generated method stub
			    	
			    }
			});
			Preference p = Preference.getInstance(this);
			currentMusicDir = p.getMusicDir();
			
		}
		public boolean onCreateOptionsMenu(Menu menu) {
		     getMenuInflater().inflate(R.menu.activity_player, menu);
		     return true;
		 }
		public boolean onOptionsItemSelected(MenuItem item) {
				// TODO Auto-generated method stub
			if(item.getItemId() == R.id.menu_chang_dir){
				Intent addMusic =new Intent();
				addMusic.setClass(LocalMp3ListActivity.this, FileViewActivity.class);
				addMusic.putExtra("initialDir", currentMusicDir);
				startActivityForResult(addMusic,0);
			}else{
				finish();
			}
			return super.onOptionsItemSelected(item);
	}
		private void updateListView(String dirName){
			infos = scanDirGetMp3Info(dirName);
			if(infos.size() == 0){
				showEmptyView();
			}else{
				showListView();
			}
			musicDataAdapter = new MusicListAdapter(infos); 
			musicDataAdapter.getFilter().filter(inputSearch.getText());
			this.setListAdapter(musicDataAdapter);
		}
		void showListView(){
			emptyView.setVisibility(View.INVISIBLE);
			fileList.setVisibility(View.VISIBLE);
		}
		void showEmptyView(){
			emptyView.setVisibility(View.VISIBLE);
			fileList.setVisibility(View.INVISIBLE);
		}
		private class MusicListAdapter extends BaseAdapter implements Filterable{
			
			public List<Mp3Info> musicData;
			private List<Mp3Info> backUpData;
			private LayoutInflater mInflater;
			
			public MusicListAdapter(List<Mp3Info> src){
				this.musicData = src;
				backUpData = src;
				mInflater = getLayoutInflater();
			}

			public int getCount() {
				// TODO Auto-generated method stub
				return musicData.size();
			}

			public Object getItem(int arg0) {
				// TODO Auto-generated method stub
				return musicData.get(arg0);
			}

			

			public View getView(final int position, View convertView, ViewGroup arg2) {
				// TODO Auto-generated method stub
				ViewHolder holder = null;
				 if (convertView == null) {
	                 
		                holder=new ViewHolder(); 		        
		                convertView = mInflater.inflate(R.layout.mp3list_item, null);
		                holder.tv_author =(TextView) convertView.findViewById(R.id.mp3_author);
		                holder.tv_title = (TextView) convertView.findViewById(R.id.mp3_name);
		                holder.btn_rmlist = (ImageButton)convertView.findViewById(R.id.btnList);		
		                convertView.setTag(holder);
		                 
		         }else {
		                holder = (ViewHolder)convertView.getTag();
		         }
				 
				 holder.tv_author.setText(musicData.get(position).getArtist());
	             holder.tv_title.setText(musicData.get(position).getMp3Name());
	             holder.btn_rmlist.setOnClickListener(new OnClickListener() {
					
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						musicData.remove(position);
						notifyDataSetChanged();
					}
				});	
	             holder.btn_rmlist.setFocusable(false);
	             holder.btn_rmlist.setFocusableInTouchMode(false);
				return convertView;
			}
			public long getItemId(int arg0) {
				// TODO Auto-generated method stub
				return 0;
			}

			public Filter getFilter() {
				// TODO Auto-generated method stub
				return new Filter() {
					boolean flagEmpty;
		            protected void publishResults(CharSequence constraint, FilterResults results) {

						musicData = (List<Mp3Info>) results.values;
		                MusicListAdapter.this.notifyDataSetChanged();
		            }
		            protected FilterResults performFiltering(CharSequence constraint) {		           
		                List<Mp3Info> filteredResults = getFilteredResults(constraint);

		                FilterResults results = new FilterResults();
		                results.values = filteredResults;

		                return results;
		            }
					private List<Mp3Info> getFilteredResults(
							CharSequence constraint) {
						// TODO Auto-generated method stub
						List<Mp3Info> result = new ArrayList<Mp3Info>();
						for(Mp3Info m : backUpData){
							if(m.getMp3Name().toLowerCase().contains(constraint)){
								result.add(m);
							}
						}
						return result;
					}
		        };
			}
			
		}
		private List<Mp3Info> scanDirGetMp3Info(String dirName){

			List<Mp3Info> mp3sInfo = new ArrayList<Mp3Info>();  

			String mSelectionClause =  MediaStore.Audio.Media.DATA + " like ? ";
			String[] selectionArgs = {  Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + dirName +"%"};
			//查询媒体数据库
			Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, mSelectionClause, selectionArgs, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
			//遍历媒体数据库
			if(cursor.moveToFirst()){
			 
			       while (!cursor.isAfterLast()) { 			       
				        //歌曲编号
				        int id = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));  
				        //歌曲标题
				        String tilte = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));  
				        //歌曲的专辑名：MediaStore.Audio.Media.ALBUM
				        String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));  
				        //歌曲的歌手名： MediaStore.Audio.Media.ARTIST
				        String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));  
				        //歌曲文件的路径 ：MediaStore.Audio.Media.DATA
				        String url = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));  			        
				        //歌曲的总播放时长 ：MediaStore.Audio.Media.DURATION
				        int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));    
				        //歌曲文件的大小 ：MediaStore.Audio.Media.SIZE
				        Long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));			       
				        Mp3Info m =new Mp3Info();
				        m.setMp3Name(tilte);
				        m.setArtist(artist);
				        m.setAlbumArt(album);
				        m.setSrc(url);
				        mp3sInfo.add(m);
				        cursor.moveToNext(); 
			       } 
			}
			return mp3sInfo;
		}
		protected void onListItemClick(ListView l, View v, int position, long id) {
				Intent playMusic =new Intent();
				Mp3Info m = musicDataAdapter.musicData.get(position);
				applicationInfos.setStartPosition(infos.indexOf(m));
				playMusic.putExtra("lanchMode", "activity");
				playMusic.setClass(this, PlayerActivity.class);
				startActivity(playMusic);
		}
		@Override
		protected void onActivityResult(int requestCode, int resultCode,
				Intent data) {
			// TODO Auto-generated method stub
			super.onActivityResult(requestCode, resultCode, data);
			if(requestCode == 0 && resultCode == RESULT_OK)
			{
				currentMusicDir = data.getStringExtra("choosedDir");
				Preference p = Preference.getInstance(this);
				p.setMusicDir(currentMusicDir);
				updateListView(currentMusicDir);
			}
		}
	 	
}
