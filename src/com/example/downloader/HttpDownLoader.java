package com.example.downloader;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.example.util.FileUtil;
public class HttpDownLoader {

	URL url = null;
	HttpURLConnection urlConn=null;
	
	//下载文本文件，返回文件的内容
	public String downLoad(String urlStr){
		StringBuffer sb = new StringBuffer();
		String line = null;
		BufferedReader bf = null;
	
		try {
			url = new URL(urlStr);
			urlConn = (HttpURLConnection) url.openConnection();
			urlConn.setConnectTimeout(3000);
			bf = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
			while( (line = bf.readLine()) != null){
				sb.append(line);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			try {
				urlConn.disconnect();
				if(bf != null)
					bf.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
	public int downLoadFile(String path,String filename,String urlStr){
		InputStream is = null;
		FileUtil fileUtil = new FileUtil();
		if(fileUtil.isFileExist(filename,path)){
			return 0;
		}
		else{
			is = getInputStreamByUrl(urlStr);
			File file = fileUtil.write2SDByInputStream(path, filename, is);
			if(file.length() == 0){
				return -1;
			}
		}
		return 1;
	}
	
	
	public InputStream getInputStreamByUrl(String urlStr){
		try {
			url = new URL(urlStr);
			urlConn = (HttpURLConnection) url.openConnection();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		InputStream is = null;
		try {
			is = urlConn.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return is;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
