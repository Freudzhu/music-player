package com.example.musicplayer;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

public class MainActivity extends TabActivity{

	TabHost tabHost;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		tabHost = getTabHost();
					
		//tabspec����tabActivity�е�һҳ
		Intent remoteIntent = new Intent();
		remoteIntent.setClass(this, RemoteMp3ListActivity.class);
		TabHost.TabSpec remoteList = tabHost.newTabSpec("�����Ƽ�");
		TextView viewRemote = (TextView) getLayoutInflater().inflate(R.layout.textview, null);	
		viewRemote.setText("�����Ƽ�");
		viewRemote.setTextSize(20);
		remoteList.setIndicator(viewRemote);
		remoteList.setContent(remoteIntent);
		tabHost.addTab(remoteList);
		
		
		Intent localIntent = new Intent();
		localIntent.setClass(this, LocalMp3ListActivity.class);
		TabHost.TabSpec localList = tabHost.newTabSpec("��������");
		TextView viewLocal = (TextView) getLayoutInflater().inflate(R.layout.textview, null);	
		viewLocal.setText("��������");
		viewLocal.setTextSize(20);
		localList.setIndicator(viewLocal);
		localList.setContent(localIntent);
		tabHost.addTab(localList);
		
		tabHost.setCurrentTabByTag("��������");
		
		
	}

}
