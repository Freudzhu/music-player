package com.example.util;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import com.example.musicplayer.PlayerActivity;

import android.util.Log;
public class LrcProcessor {  
	    private static final String TAG = "LRCUtils";        
	    private static Vector<timelrc> lrclist;  
	    private boolean IsLyricExist = false;  
	    private int lastLine = 0;  
	    PlayerActivity mediaPlay;    
	    public LrcProcessor( PlayerActivity mediaPlay) {  
	    	this.mediaPlay = mediaPlay;
	    }        
	    public void ReadLRC(File f)  
	    {  
	        try  
	        {             
	            if (!f.exists())  
	            {  
	                Log.d(TAG,"not exit the lrc file");  
	                IsLyricExist = false;  
 
	            }  
	            else  
	            {  
	                lrclist = new Vector<timelrc>(); 
	               
	                IsLyricExist = true;  
	                InputStream is = new BufferedInputStream(new FileInputStream(f));  
	                BufferedReader br = new BufferedReader(new InputStreamReader(is, GetCharset(f)));  
	                String strTemp = "";  
	                while ((strTemp = br.readLine()) != null)  
	                {  

	                    strTemp = AnalyzeLRC(strTemp); 
	                }  
	                br.close();  
	                is.close();  
	                Collections.sort(lrclist, new Sort()); 
	                
	                for(int i=0;i<lrclist.size();i++){
	                    Log.d("Music player","" + lrclist.get(i).getLrcString());   
	                }
	            
        
	            }  
	        }  
	        catch (Exception e)  
	        {  
	            e.printStackTrace();  
	        }  
	    }
	    public Vector<timelrc> getLrcList(){
	    	return this.lrclist;
	    }
	      
	    private String AnalyzeLRC(String LRCText)  
	    {  
	        try  
	        {  
	        	Log.d("Music player","analyzeLRC");   
	            int pos1 = LRCText.indexOf("["); 
	            int pos2 = LRCText.indexOf("]");
	            Log.d("Music player",pos1 + "" + pos2); 
	            if (pos1 == 0 && pos2 != -1)  
	            {
	                Long time[] = new Long[GetPossiblyTagCount(LRCText)];
	                time[0] = TimeToLong(LRCText.substring(pos1 + 1, pos2)); 
	                if (time[0] == -1) 
	                    return ""; // LRCText  
	                String strLineRemaining = LRCText;  
	                 
	                int i = 1;  
	                while (pos1 == 0 && pos2 != -1)  
	                {  
	                      
	                    strLineRemaining = strLineRemaining.substring(pos2 + 1);
	                    pos1 = strLineRemaining.indexOf("[");  
	                    pos2 = strLineRemaining.indexOf("]");  
	                    if (pos2 != -1)  
	                    {  
	                        time[i] = TimeToLong(strLineRemaining.substring(pos1 + 1, pos2));  
	                        if (time[i] == -1) 
	                            return ""; // LRCText  
	                        i++;  
	                    }  
	                }  
	               
	                timelrc tl = new timelrc();  
	                Log.d(TAG,tl.getLrcString() + tl.getTimePoint());  
	                for (int j = 0; j < time.length; j++)  
	                {  
	                    if (time[j] != null)  
	                    {  

	                        tl.setTimePoint(time[j].intValue());  
	                        tl.setLrcString(strLineRemaining);  
	                        Log.d(TAG,strLineRemaining);   
	                        lrclist.add(tl);  
	                        tl = new timelrc();  

	                    }  
	                }  
	                return strLineRemaining;  
	            }  
	            else  
	                return "";  
	        }  
	        catch (Exception e)  
	        {  
	            return "";  
	        }  
	    }  

	    public void RefreshLRC(int current)  
	    {  
	        if (IsLyricExist){  
	            for(int i = 0; i < lrclist.size(); i++)  
	            {  
	                if(current < lrclist.get(i).getTimePoint())  
	                    if(i == 0 || current >= lrclist.get(i-1).getTimePoint())  
	                    {  
	                        Log.d(TAG,"string = "+lrclist.get(i-1).getLrcString());  
	                        mediaPlay.setLRCText(lrclist.get(i-1).getLrcString(),lastLine!=(i-1));  
	                        lastLine = i-1;  
	                    }  
	                  
	            }  
	        }  
	    }  
	    
	    private int GetPossiblyTagCount(String Line)  
	        {  
	    	Log.d("Music player","getPossibleTagCount");
	    	String strCount1[] = Line.split("\\Q[\\E");
	    	String strCount2[] = Line.split("\\Q]\\E");
	            if (strCount1.length == 0 && strCount2.length == 0)  
	                return 1;  
	            else if (strCount1.length > strCount2.length) {
	                return strCount1.length;  
	            }
	            else  {
	                return strCount2.length;  
	            }
	        }  
	          
	    public long TimeToLong(String Time)  
	        {  
	            try  
	            {  
	                String[] s1 = Time.split(":");  
	                int min = Integer.parseInt(s1[0]);  
	                String[] s2 = s1[1].split("\\.");
	                int sec = Integer.parseInt(s2[0]);  
	                int mill = 0;  
	                if (s2.length > 1)  
	                    mill = Integer.parseInt(s2[1]);  
	                return min * 60 * 1000 + sec * 1000 + mill * 10;  
	            }  
	            catch (Exception e)  
	            {  
	                return -1;  
	            }  
	        }  
	    public String GetCharset(File file){  
	        String charset = "GBK";  
	        byte[] first3Bytes = new byte[3];  
	        try  
	        {  
	            boolean checked = false;  
	            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));  
	            bis.mark(3);  
	            int read = bis.read(first3Bytes, 0, 3);  
	            if (read == -1)  
	                return charset;  
	            if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE)  
	            {  
	                charset = "UTF-16LE";  
	                checked = true;  
	            }  
	            else if (first3Bytes[0] == (byte) 0xFE && first3Bytes[1] == (byte) 0xFF)  
	            {  
	                charset = "UTF-16BE";  
	                checked = true;  
	            }  
	            else if (first3Bytes[0] == (byte) 0xEF && first3Bytes[1] == (byte) 0xBB && first3Bytes[2] == (byte) 0xBF)  
	            {  
	                charset = "UTF-8";  
	                checked = true;  
	            }  
	            bis.reset();  
	            if (!checked)  
	            {  
	                int loc = 0;  
	                while ((read = bis.read()) != -1)  
	                {  
	                    loc++;  
	                    if (read >= 0xF0)  
	                        break;  
	                    if (0x80 <= read && read <= 0xBF) 
	                        break;  
	                    if (0xC0 <= read && read <= 0xDF)  
	                    {  
	                        read = bis.read();  
	                        if (0x80 <= read && read <= 0xBF)
	                            continue;  
	                        else  
	                            break;  
	                    }  
	                    else if (0xE0 <= read && read <= 0xEF)  
	                    {
	                        read = bis.read();  
	                        if (0x80 <= read && read <= 0xBF)  
	                        {  
	                            read = bis.read();  
	                            if (0x80 <= read && read <= 0xBF)  
	                            {  
	                                charset = "UTF-8";  
	                                break;  
	                            }  
	                            else  
	                                break;  
	                        }  
	                        else  
	                            break;  
	                    }  
	                }  
	            }  
	            bis.close();  
	        }  
	        catch (Exception e)  
	        {  
	            e.printStackTrace();  
	        }  
	        return charset;  
	    }  
	      
	    private class Sort implements Comparator<timelrc> {  
	        public Sort() {  
	        }  
	        public int compare(timelrc tl1, timelrc tl2) {  
	            return sortUp(tl1, tl2);  
	        }  
	        private int sortUp(timelrc tl1, timelrc tl2) {  
	            if (tl1.getTimePoint() < tl2.getTimePoint())  
	                return -1;  
	            else if (tl1.getTimePoint() > tl2.getTimePoint())  
	                return 1;  
	            else  
	                return 0;  
	        }  
	    }       
	
	    public static class timelrc {  
	        private String lrcString;  
	        private int sleepTime;  
	        private int timePoint;  
	        timelrc() {  
	            lrcString = null;  
	            sleepTime = 0;  
	            timePoint = 0;  
	        }  
	        public void setLrcString(String lrc) {  
	            lrcString = lrc;  
	        }  
	        public void setSleepTime(int time) {  
	            sleepTime = time;  
	        }  
	        public void setTimePoint(int tPoint) {  
	            timePoint = tPoint;  
	        }  
	        public String getLrcString() {  
	            return lrcString;  
	        }  
	        public int getSleepTime() {  
	            return sleepTime;  
	        }  
	        public int getTimePoint() {  
	            return timePoint;  
	        }  
	    };  
}  
	

//	private long StringToLong(String timeStr) {
//		if(timeStr!=null && timeStr.length() >= 9){
//			String preTimeStr = timeStr.substring(1, 9);
//			String[] s = preTimeStr.split(":");
//			if(s.length != 2){
//				int min = Integer.parseInt(s[0]);
//				String[] ss = s[1].split("\\.");
//				int sec = Integer.parseInt(ss[0]);
//				int mill = Integer.parseInt(ss[1]);
//				return min*60*1000+sec*1000+mill*10L;
//			}else{
//				return -1;
//			}
//		
//		}
//		return -1;
//		
//	}

