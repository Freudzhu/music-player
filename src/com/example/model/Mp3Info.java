package com.example.model;
import android.os.Parcel;
import android.os.Parcelable;
public class Mp3Info implements Parcelable{

	private String id;
	private String mp3Name;
	private String mp3Size;
	private String lrcName;
	private String lrcSize;
	private String src;
	private String artist;
	private String albumArt;
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "mp3Name:" + mp3Name + "artist:" + artist;
	}
	public Mp3Info() {
		super();
	}
	public Mp3Info(String id, String mp3Name, String mp3Size, String lrcName,
			String lrcSize) {
		super();
		this.id = id;
		this.mp3Name = mp3Name;
		this.mp3Size = mp3Size;
		this.lrcName = lrcName;
		this.lrcSize = lrcSize;
	}
	public String getArtist() {
		return artist;
	}
	public void setArtist(String artist) {
		this.artist = artist;
	}
	public String getAlbumArt() {
		return albumArt;
	}
	public void setAlbumArt(String albumArt) {
		this.albumArt = albumArt;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMp3Name() {
		return mp3Name;
	}
	public void setMp3Name(String mp3Name) {
		this.mp3Name = mp3Name;
	}
	public String getMp3Size() {
		return mp3Size;
	}
	public void setMp3Size(String mp3Size) {
		this.mp3Size = mp3Size;
	}
	public String getLrcName() {
		return lrcName;
	}
	public void setLrcName(String lrcName) {
		this.lrcName = lrcName;
	}
	public String getLrcSize() {
		return lrcSize;
	}
	public void setLrcSize(String lrcSize) {
		this.lrcSize = lrcSize;
	}
	

	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	public void writeToParcel(Parcel arg0, int arg1) {
		// TODO Auto-generated method stub
		arg0.writeString(id);
		arg0.writeString(mp3Name);
		arg0.writeString(mp3Size);
		arg0.writeString(lrcName);
		arg0.writeString(lrcSize);
		arg0.writeString(src);
		arg0.writeString(artist);
		
	}
	public static final Parcelable.Creator<Mp3Info> CREATOR
    = new Parcelable.Creator<Mp3Info>() {

		//需要实现的Creator接口方法
		public Mp3Info createFromParcel(Parcel source) {
		
		    //获取数据
		
			Mp3Info myClass = new Mp3Info();
		
			myClass.id = source.readString();
			myClass.mp3Name = source.readString();
			myClass.mp3Size = source.readString();
			myClass.lrcName = source.readString();
			myClass.lrcSize = source.readString();
			myClass.src = source.readString();
			myClass.artist = source.readString();
		    return myClass;
		
		}
	
		//需要实现的Creator接口方法
		public Mp3Info[] newArray(int size) {
		    return new Mp3Info[size];
		}
	};
	public String getSrc() {
		return src;
	}
	public void setSrc(String src) {
		this.src = src;
	}

}
