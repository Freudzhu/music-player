package com.example.util;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import android.os.Environment;
import com.example.model.Mp3Info;
public class FileUtil {
	private String SDCardRoot;
	public FileUtil(){
		SDCardRoot = Environment.getExternalStorageDirectory().getAbsolutePath();
	}
	public File creatSDFile(String fileName,String dirName){
		File file = new File(SDCardRoot+File.separator+dirName+File.separator+fileName);
		try {
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return file;
	}
	public File creatSDDir(String dirName){
		File file = new File(SDCardRoot+File.separator+dirName);
		file.mkdir();
		return file;
	}
	public Boolean isFileExist(String fileName,String path){
		File file = new File(SDCardRoot +File.separator+path+File.separator+ fileName);
		return file.exists();
	}
	public File write2SDByInputStream(String path,String fileName,InputStream in){
		if(creatSDDir(path) == null){
			System.out.println("fail create dir");
		}
		File file = creatSDFile(fileName,path);
		OutputStream os =null;
		try {
			os = new FileOutputStream(file);
			byte[] bytes = new byte[256];
			while((in.read(bytes)) != -1){
				os.write(bytes);
			}			
			os.flush();
		}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch(NullPointerException e){
			e.printStackTrace();
		}	
		finally{		
			try {
				os.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		return file;
	}
	public List<Mp3Info> getMp3File(String path){
		List<Mp3Info> infos = new ArrayList<Mp3Info>();
		String mp3DirPath = SDCardRoot + File.separator + path;
		File mp3Dir = new File(mp3DirPath);
		File[] files = mp3Dir.listFiles();
		if(files != null){
			for(int i=0;i < files.length; i++ ){				
				if(files[i].getName().endsWith("mp3")){
					Mp3Info info = new Mp3Info();
					info.setMp3Name(files[i].getName());
					info.setMp3Size(files[i].length()+"");
					String lrcName = files[i].getName().substring(0, files[i].getName().length() - 4) + ".lrc";
					info.setLrcName(lrcName);	
					infos.add(info);
				}
				
			}
		}		
		return infos;	
	}
}
