package com.example.musicplayer;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class MainActivity extends TabActivity{

	TabHost tabHost;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		tabHost = getTabHost();
		//tabspec就是tabActivity中的一页
		Intent remoteIntent = new Intent();
		remoteIntent.setClass(this, RemoteMp3ListActivity.class);
		TabHost.TabSpec remoteList = tabHost.newTabSpec("remote");
		Resources resource = getResources();
		remoteList.setIndicator("remote", resource.getDrawable(android.R.drawable.stat_sys_download));
		remoteList.setContent(remoteIntent);
		tabHost.addTab(remoteList);
		
		
		Intent localIntent = new Intent();
		localIntent.setClass(this, LocalMp3ListActivity.class);
		TabHost.TabSpec localList = tabHost.newTabSpec("local");
		localList.setIndicator("local", resource.getDrawable(android.R.drawable.stat_sys_download_done));
		localList.setContent(localIntent);
		tabHost.addTab(localList);
		
		
	}

}
