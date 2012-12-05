package com.example.musicplayer;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.example.downloader.HttpDownLoader;
import com.example.model.Mp3Info;
import com.example.musicplayer.service.DownloaderService;
import com.example.xml.Mp3ListContentHandler;
public class Mp3ListActivity extends ListActivity {
	private static final int UPDATE = 1;
	private static final int ABOUT = 2;
	List<Mp3Info> infos =  null;
	//点击menu会调用这个方法，可以自定义自己的控件
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mp3_list);
        updateListView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.activity_mp3_list, menu);
        menu.add(0, UPDATE,UPDATE, R.string.mp3list_update);
        menu.add(0, ABOUT, ABOUT, R.string.mp3list_about);
        return super.onCreateOptionsMenu(menu);
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if(item.getItemId() == UPDATE){
			updateListView();
		}
		else if(item.getItemId() == ABOUT){
			
		}
		return super.onOptionsItemSelected(item);
	}	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		Mp3Info mp3Info = infos.get(position);
		Intent intent =new Intent();
		intent.putExtra("mp3Info", mp3Info);
		intent.setClass(this, DownloaderService.class);
		startService(intent);		
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
		String xml = downLoadXMl("http://10.0.2.2:8080/mp3/resources.xml");
		infos = parseXML(xml);
		SimpleAdapter simpleAdapter = buildSimpleAdapter(infos);
		this.setListAdapter(simpleAdapter);
	}
	public String downLoadXMl(String url){
		HttpDownLoader hdl = new HttpDownLoader();
		String result = hdl.downLoad(url);
		return result;
	}
	public List<Mp3Info> parseXML(String xmlStr){
		SAXParserFactory saxParserFactory =SAXParserFactory.newInstance();
		List<Mp3Info> infos = new ArrayList<Mp3Info>();
		try{
			XMLReader xmlReader=saxParserFactory.newSAXParser().getXMLReader();			
			Mp3ListContentHandler mp3ListContentHandler = new Mp3ListContentHandler(infos);
			xmlReader.setContentHandler(mp3ListContentHandler);
			xmlReader.parse(new InputSource(new StringReader(xmlStr)));
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return infos;
	}
    
}
