package com.example.musicplayer;
import java.io.File;
import com.example.musicplayer.R;
import com.example.musicplayer.dirselect.FileAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
public class FileViewActivity extends Activity{

	private ListView dirList;
	private TextView currDirLabel;
	private ImageView upButton;
	private ImageView chooseDir;
	LinearLayout emptyView ;
	String SDCardRoot = Environment.getExternalStorageDirectory().getAbsolutePath();
	File curFile;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 setContentView(R.layout.dir_chooser);
		 init();
	}
	private void init(){
		dirList = (ListView) findViewById(R.id.dirList);
		currDirLabel = (TextView) findViewById(R.id.currDirLabel);
		upButton = (ImageView) findViewById(R.id.up);
		chooseDir = (ImageView) findViewById(R.id.choose_dir);
		emptyView = (LinearLayout) findViewById(R.id.empty_chooser_view);
		final String initialDir = getIntent().getExtras().getString(
				"initialDir");
		setCurrDir(new File(SDCardRoot + File.separator + initialDir));

		initListeners();
		

	}
	private void setCurrDir(File file) {
		// TODO Auto-generated method stub
		curFile = file;
		currDirLabel.setText(file.getAbsolutePath());
		setAdapterForDir(file);
		toggleUpButton(curFile);
	}
	private void initListeners() {
		// TODO Auto-generated method stub
		dirList.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> parent,
											View view,
											int position,
											long id)
			{
				File selectedFile = (File) parent.getItemAtPosition(position);
				if (selectedFile.isDirectory() && selectedFile.canRead())
				{
					setCurrDir(selectedFile);
				}
			}
		});

		chooseDir.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{				
					Intent intent = getIntent();
					intent.putExtra("choosedDir",
					curFile.getAbsolutePath().substring(12));
					setResult(RESULT_OK, intent);
					finish();

			}
		});

		upButton.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				
				if(curFile.getAbsolutePath().equals(SDCardRoot)){
					upButton.setEnabled(false);
				}else{
					setCurrDir(curFile.getParentFile());
				}
					

			}
		});
	}
	private void toggleUpButton(File file)
	{
		if(file.getAbsolutePath().equals(SDCardRoot)){
			chooseDir.setVisibility(View.INVISIBLE);
		}
		else{
			chooseDir.setVisibility(View.VISIBLE);
		}
	}

	private void setAdapterForDir(File file)
	{
		
		File[] files = file.listFiles();
		if (files == null)
		{
			files = new File[0];
	
		}else if(files.length == 0){
			emptyView.setVisibility(View.VISIBLE);
			dirList.setVisibility(View.INVISIBLE);
		}
		else{
			ArrayAdapter<File> adapter = new FileAdapter(this, files);
			dirList.setAdapter(adapter);
			emptyView.setVisibility(View.INVISIBLE);
			dirList.setVisibility(View.VISIBLE);
		}
		
	}

	
	
}
