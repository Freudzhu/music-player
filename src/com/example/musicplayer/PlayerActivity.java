package com.example.musicplayer;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.example.constant.ConstantMessage;
import com.example.musicplayer.service.MusicPlayerService;
import com.example.musicplayer.service.MusicPlayerService.LocalBinder;
import com.example.preference.Key;
import com.example.preference.Keys;
import com.example.preference.Preference;
import com.example.preference.Preference.PreferenceObserver;
import com.example.util.LrcProcessor;
import com.example.wiget.LrcText;
import com.markupartist.android.widget.ActionBar;
import com.markupartist.android.widget.ActionBar.AbstractAction;
import com.markupartist.android.widget.ActionBar.Action;
import com.markupartist.android.widget.ActionBar.IntentAction;
public class PlayerActivity extends Activity {
		static final int notifyId = 11;
		static final int ONGOING_NOTIFICATION = 12345678;
		NotificationManager manager;
		MusicCompletionBroadReceiver musicCompletion = new MusicCompletionBroadReceiver();
		
		Preference preferences;
		boolean isPlaying =  true;
				
		private RelativeLayout nowPlayingSlide;
		private RelativeLayout settingsSlide;
	
		private ImageView backButton;
		private ImageView playButton;
		private ImageView nextButton;
		private ImageView shuffleButton;
		private ImageView repeatButton;

		
		CheckBox shuffleCheckBox;
		CheckBox repeatCheckBox;

		private TextView author;
		private TextView title;
		private TextView duration;

		
		private Handler handler = new Handler();
		private Runnable progressBarRunnable;
		private SeekBar progressBar;
		
		
		
		String lanchMode;
		MusicPlayerService mService;
		boolean mBound = false;
	    private ServiceConnection mConnection = new ServiceConnection() {

	        public void onServiceConnected(ComponentName className,
	                IBinder service) {
	            // We've bound to LocalService, cast the IBinder and get LocalService instance
	            LocalBinder binder = (LocalBinder) service;
	            mService = binder.getService();
	            progressBar.setMax(mService.getMusicPlayLength());
	            mBound = true;
	    		//creatNotification();
	    		if(lanchMode.equals("activity")){
	    			play();
	    		}else{
	    			updatePlayingMusicInfo();
	    		}
	       	 lrcService  = new LrcProcessor(PlayerActivity.this);  
	    	 getLrcPath(mService.getCurrentMp3Info().getSrc());
	    	 Log.d("Music player","connect to service");   	
	    	
	    		
	
	            
	        }
	        public void onServiceDisconnected(ComponentName arg0) {
	            mBound = false;
	        }
	    };
	    
	    ActionBar actionBar;
	    Handler delayHandler =new Handler();
	    
	    
	    private ViewPager mPager;//Ò³¿¨ÄÚÈÝ
	    private List<View> listViews; // TabÒ³ÃæÁÐ±í
	    private ImageView cursor;// ¶¯»­Í¼Æ¬
	    private int offset = 0;// ¶¯»­Í¼Æ¬Æ«ÒÆÁ¿
	    private int currIndex = 1;// µ±Ç°Ò³¿¨±àºÅ
	    private int bmpW;// ¶¯»­Í¼Æ¬¿í¶È
	    View nowPlaying; 
	    View lrcDisplaying;
	    LrcText playlrcText;
	    LrcProcessor lrcService;
	    
	public boolean getPlayState(){
		return isPlaying;
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);
		LayoutInflater   inflater = getLayoutInflater();
		nowPlaying = inflater.inflate(R.layout.now_playing, null);
		lrcDisplaying = inflater.inflate(R.layout.lrclayout, null);
        init(); 
        lanchMode = getIntent().getStringExtra("lanchMode");	
    }
   
    @Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		IntentFilter musicCompletionFilter = new IntentFilter(ConstantMessage.PlayerMessage.music_completion);
		this.registerReceiver(musicCompletion, musicCompletionFilter);
						 
        Intent intent = new Intent(this, MusicPlayerService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
	}
	private boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("com.example.musicplayer.service.MusicPlayerService".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_player, menu);
        return true;
    }
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
    	compeleteQuit();
		return super.onOptionsItemSelected(item);
	}	
    
    private void compeleteQuit(){
    	if(manager != null){
			manager.cancel(notifyId);
			manager = null;
		}
		Intent intent =new Intent();
		intent.setClass(this, MusicPlayerService.class);
		stopService(intent);
		finish();
    }
    @Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	    if (mBound) {
	          unbindService(mConnection);
	          mBound = false;
	    }
	    
	    Log.e("musicplayer", "activity destory");
	}
    @Override
	protected void onStop() {
		// TODO Auto-generated method stub
    	if(musicCompletion != null)
    		this.unregisterReceiver(musicCompletion);
		super.onStop();	
		
	}
    
    private void init(){
    	setApplication();
    	InitImageView();
    	InitViewPager();
    	SetupButtons();
    	SetupButtonListeners();
    	SetupProgressBar();
    	setActionBar();
    	TogglePlayButton();
    }
    private void setApplication() {
		// TODO Auto-generated method stub
    	  preferences = Preference.getInstance(getApplicationContext());
    	  preferences.addObserver(new PreferenceObserver() {
			
			public void change(Key key) {
			
				if (key.equals(Keys.REPEAT))
				{
					//Update UI states
					runOnUiThread(new Runnable()
					{
						public void run()
						{
							boolean repeat = preferences.getRepeat();
							repeatButton.setImageDrawable(getResources().getDrawable(
									  repeat ? R.drawable.repeat48_active : R.drawable.repeat48));
							repeatCheckBox.setChecked(repeat);
						}
					});
				}else if (key.equals(Keys.SHUFFLE))
				{
					//Update UI states
					runOnUiThread(new Runnable()
					{
						public void run()
						{
							boolean shuffle = preferences.getShuffle();
							shuffleButton.setImageDrawable(getResources().getDrawable(
									  shuffle ? R.drawable.shuffle48_active : R.drawable.shuffle48));
							shuffleCheckBox.setChecked(shuffle);
						}
					});
				}	
			
			}
		});
    	

    	  
	}
    private void setActionBar(){
    	actionBar = (ActionBar) findViewById(R.id.actionbar);
    	// You can also assign the title programmatically by passing a
    	// CharSequence or resource id.
    	final Action shareAction = new FinishAction(R.drawable.list64);
        actionBar.setHomeAction(shareAction);
        final Action otherAction = new NoIntent(this, new Intent(), R.drawable.settings48,nowPlayingSlide,settingsSlide);
        actionBar.addAction(otherAction);
    }
    private class NoIntent extends IntentAction{

		private RelativeLayout nowPlayingSlide;
		private RelativeLayout settingsSlide;
		boolean flag = false;
		public NoIntent(Context arg0, Intent arg1, int arg2,RelativeLayout p,RelativeLayout s) {
			super(arg0, arg1, arg2);
			// TODO Auto-generated constructor stub
			this.nowPlayingSlide = p;
			this.settingsSlide = s;
		}
		public void performAction(View view) {
			if(flag){
				nowPlayingSlide.setVisibility(LinearLayout.VISIBLE);
				settingsSlide.setVisibility(LinearLayout.INVISIBLE);
			}else{
				nowPlayingSlide.setVisibility(LinearLayout.INVISIBLE);
				settingsSlide.setVisibility(LinearLayout.VISIBLE);
			}
			flag = !flag;
			
		}
	}
    private class FinishAction extends AbstractAction{

		public FinishAction(int arg0) {
			super(arg0);
			// TODO Auto-generated constructor stub
		}

		public void performAction(View view) {
			// TODO Auto-generated method stub
			finish();
		}
    	
    }
	private void SetupButtons()
	{

		// Now_Playing Footer Buttons
		backButton = (ImageView) findViewById(R.id.backButton);
		playButton = (ImageView) findViewById(R.id.playButton);
		nextButton = (ImageView) findViewById(R.id.nextButton);
		progressBar = (SeekBar)findViewById(R.id.progressBar);
		shuffleButton = (ImageView) findViewById(R.id.shuffleButton);
		shuffleButton.setImageDrawable(preferences.getShuffle()? getResources().getDrawable(R.drawable.shuffle48_active) : getResources().getDrawable(R.drawable.shuffle48));
		repeatButton = (ImageView) findViewById(R.id.repeatButton);
		repeatButton.setImageDrawable(preferences.getRepeat()? getResources().getDrawable(R.drawable.repeat48_active) : getResources().getDrawable(R.drawable.repeat48));
		// Settings Slide
		shuffleCheckBox = (CheckBox) findViewById(R.id.shuffleCheckBox);
		shuffleCheckBox.setChecked(preferences.getShuffle());
		repeatCheckBox = (CheckBox) findViewById(R.id.repeatCheckBox);	
		repeatCheckBox.setChecked(preferences.getRepeat());

		author = (TextView) nowPlaying.findViewById(R.id.trackArtist);
		title = (TextView) nowPlaying.findViewById(R.id.trackTitle);
		duration =(TextView)nowPlaying.findViewById(R.id.trackDuration);
		nowPlayingSlide =(RelativeLayout) findViewById(R.id.now_playing_slide);
		settingsSlide=(RelativeLayout) findViewById(R.id.settings_slide);
		playlrcText = (LrcText)lrcDisplaying.findViewById(R.id.lrctext);

	}
 	private void SetupButtonListeners()
 	{


 		backButton.setOnClickListener(new OnClickListener()
 		{
 			public void onClick(View v)
 			{
 				playBack();
 				updatePlayingMusicInfo();
 				
 			}
 		});

 		playButton.setOnClickListener(new OnClickListener()
 		{
 			public void onClick(View v)
 			{				
 				pause();
 				updatePlayingMusicInfo();
 				
 			}
 		});

 		nextButton.setOnClickListener(new OnClickListener()
 		{
 			public void onClick(View v)
 			{
 				playNext();			
 				updatePlayingMusicInfo();
 			}
 		});

 		shuffleButton.setOnClickListener(new OnClickListener()
 		{
 			public void onClick(View v)
 			{
 				preferences.setShuffle(!preferences.getShuffle());
 			}
 		});

 		shuffleCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener()
 		{
 			public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
 			{
 				preferences.setShuffle(isChecked);
 			}
 		});

 		repeatButton.setOnClickListener(new OnClickListener()
 		{
 			public void onClick(View v)
 			{
 				preferences.setRepeat(!preferences.getRepeat());
 			}
 		});

 		repeatCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener()
 		{
 			public void onCheckedChanged(CompoundButton buttonView,boolean isChecked)
 			{
 				preferences.setRepeat(isChecked);
 			}
 		});
 		


 	}
 	private void play(){
 		isPlaying = true;
		Intent intent =new Intent();
		intent.setClass(getApplicationContext(), MusicPlayerService.class);
		intent.putExtra("message", ConstantMessage.PlayerMessage.PLAY_MSG);
		startService(intent);
		delayHandler.postDelayed(new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				updatePlayingMusicInfo();
			}
		}, 100);
 	}
 	private void playNext(){
 		isPlaying = true;
		Intent intent =new Intent();
		intent.setClass(getApplicationContext(), MusicPlayerService.class);
		intent.putExtra("message", ConstantMessage.PlayerMessage.PLAY_NEXT_MSG);
		startService(intent);
		delayHandler.postDelayed(new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				updatePlayingMusicInfo();
			}
		}, 100);
 	}
 	private void playBack(){
 		isPlaying = true;
		Intent intent =new Intent();
		intent.setClass(getApplicationContext(), MusicPlayerService.class);
		intent.putExtra("message", ConstantMessage.PlayerMessage.PLAY_BACK_MSG);
		startService(intent);
		delayHandler.postDelayed(new Runnable() {
			
			public void run() {
				// TODO Auto-generated method stub
				updatePlayingMusicInfo();
			}
		}, 100);
		
 	}
 	private void pause(){
 		isPlaying = isPlaying ? false : true;
 		Intent intent =new Intent();
		intent.setClass(getApplicationContext(), MusicPlayerService.class);
		intent.putExtra("message", ConstantMessage.PlayerMessage.PAUSE_MSG);
		startService(intent);
 	}
 	public void TogglePlayButton()
	{
 		Log.e("state",getPlayState()  + "");
		if (!getPlayState())
		{
			playButton.setImageDrawable(getResources().getDrawable(R.drawable.play64));
		} else
		{
			playButton.setImageDrawable(getResources().getDrawable(R.drawable.pause64));
		}

	}
 	private void SetupProgressBar()
	{
		progressBar = (SeekBar) findViewById(R.id.progressBar);
		progressBarRunnable = new Runnable()
		{
			public void run()
			{
				if (getPlayState()&&mBound)
				{
					int current = mService.getMusicPlayCurrentLength();
					progressBar.setProgress(current);
					duration.setText(ConvertToMinutes(mService.getMusicPlayCurrentLength()) + " / " + ConvertToMinutes(mService.getMusicPlayLength()));
					
				}
				handler.postDelayed(this, 100);
				if (mBound)
				{
					progressBar.setMax(mService.getMusicPlayLength());
				}
			}
		};
		

		handler.post(progressBarRunnable);
		
		progressBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener()
		{

			public void onProgressChanged(SeekBar seekBar,int progress,boolean fromUser)
			{
				if(fromUser == true){
					mService.setProgress(progress);
				}
				
			}

			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
				
			}

			public void onStopTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
 	private String ConvertToMinutes(int time)
	{
		String duration;

		int milliseconds = time;
		int seconds = (int) (milliseconds / 1000) % 60;
		int minutes = (int) ((milliseconds / (1000 * 60)) % 60);

		if (seconds < 10)
		{
			duration = Integer.toString(minutes) + ":0" + Integer.toString(seconds);
		} else
		{
			duration = Integer.toString(minutes) + ":" + Integer.toString(seconds);
		}

		return duration;
	}
 	private void updatePlayingMusicInfo(){
 		if(isServiceRunning()){
 			actionBar.setTitle(mService.getCurrentMp3Info().getMp3Name());
 	 		author.setText(mService.getCurrentMp3Info().getArtist());
 	 		title.setText(mService.getCurrentMp3Info().getMp3Name());
 	 		TogglePlayButton();
 		}
 	}
 	/**
     * ³õÊ¼»¯ViewPager
*/
    private void InitViewPager() {
        mPager = (ViewPager) findViewById(R.id.vPager);
        listViews = new ArrayList<View>();
        LayoutInflater mInflater = getLayoutInflater();
        
        listViews.add(lrcDisplaying);
        listViews.add(nowPlaying);
        listViews.add(mInflater.inflate(R.layout.now_playing, null));
        mPager.setAdapter(new MyPagerAdapter(listViews));
        mPager.setCurrentItem(1);
        mPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }
    /**
     * ³õÊ¼»¯¶¯»­
*/
    private void InitImageView() {
        cursor = (ImageView) findViewById(R.id.cursor);
        bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.bg_progress_fill)
                .getWidth();// »ñÈ¡Í¼Æ¬¿í¶È
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;// »ñÈ¡·Ö±æÂÊ¿í¶È
        offset = (screenW / 3 - bmpW) / 2;// ¼ÆËãÆ«ÒÆÁ¿
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset , 0);
        cursor.setImageMatrix(matrix);// ÉèÖÃ¶¯»­³õÊ¼Î»ÖÃ
        Animation animation = new TranslateAnimation(offset, offset * 2 + bmpW, 0, 0);
        animation.setFillAfter(true);// True:Í¼Æ¬Í£ÔÚ¶¯»­½áÊøÎ»ÖÃ
        animation.setDuration(300);
        cursor.startAnimation(animation);
    
    }
    /**
     * Ò³¿¨ÇÐ»»¼àÌý
*/
    public class MyOnPageChangeListener implements OnPageChangeListener {

        int one = offset * 2 + bmpW;// Ò³¿¨1 -> Ò³¿¨2 Æ«ÒÆÁ¿
        int two = one * 2;// Ò³¿¨1 -> Ò³¿¨3 Æ«ÒÆÁ¿

        public void onPageSelected(int arg0) {
            Animation animation = null;
            switch (arg0) {
            case 0:
                if (currIndex == 1) {
                    animation = new TranslateAnimation(one, 0, 0, 0);
                } else if (currIndex == 2) {
                    animation = new TranslateAnimation(two, 0, 0, 0);
                }
                break;
            case 1:
                if (currIndex == 0) {
                    animation = new TranslateAnimation(offset,one, 0, 0);
                    Log.i("intridate move ?", one +"");
                } else if (currIndex == 2) {
                    animation = new TranslateAnimation(two, one, 0, 0);
                }
                break;
            case 2:
                if (currIndex == 0) {
                    animation = new TranslateAnimation(offset, two, 0, 0);
                } else if (currIndex == 1) {
                    animation = new TranslateAnimation(one, two, 0, 0);
                }
                break;
            }
            currIndex = arg0;
            animation.setFillAfter(true);// True:Í¼Æ¬Í£ÔÚ¶¯»­½áÊøÎ»ÖÃ
            animation.setDuration(300);
            cursor.startAnimation(animation);
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        public void onPageScrollStateChanged(int arg0) {
        }
    }
    public class MusicCompletionBroadReceiver extends BroadcastReceiver{
 		public void onReceive(Context context, Intent intent) {
 			playNext();
 		}
 	}
    public void setLRCText(String lrcString,boolean changeLine) {
//        if(changeLine){
//            flipperLrc.showNext();
//        }
        playlrcText.setText(lrcString);
       
    }
	/**
     * ViewPagerÊÊÅäÆ÷
*/
    public class MyPagerAdapter extends PagerAdapter {
        public List<View> mListViews;

        public MyPagerAdapter(List<View> mListViews) {
            this.mListViews = mListViews;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView(mListViews.get(arg1));
        }

        @Override
        public void finishUpdate(View arg0) {
        }

        @Override
        public int getCount() {
            return mListViews.size();
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(mListViews.get(arg1), 0);
            return mListViews.get(arg1);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == (arg1);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
        }
    }
    private void getLrcPath(String path) {          
        if (path != null) {  
            path = path.substring(0, path.lastIndexOf(".")).concat(".lrc");  
            File file = new File(path);          
            Log.d("Music player","getLrcPath"); 
            Log.d("Music player",file.getAbsoluteFile()+"");
            lrcService.ReadLRC(file);         
        }  
      
    }  
	

    
}
