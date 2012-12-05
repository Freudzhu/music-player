package com.example.model;
import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class Mp3Info implements Parcelable{

	private String id;
	private String mp3Name;
	private String mp3Size;
	private String lrcName;
	private String lrcSize;
	
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
	@Override
	public String toString() {
		return "mp3Info [id=" + id + ", mp3Name=" + mp3Name + ", mp3Size="
				+ mp3Size + ", lrcName=" + lrcName + ", lrcSize=" + lrcSize
				+ "]";
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
		
	}
	public static final Parcelable.Creator<Mp3Info> CREATOR
    = new Parcelable.Creator<Mp3Info>() {

		//��Ҫʵ�ֵ�Creator�ӿڷ���
		public Mp3Info createFromParcel(Parcel source) {
		
		    //��ȡ����
		
			Mp3Info myClass = new Mp3Info();
		
			myClass.id = source.readString();
			myClass.mp3Name = source.readString();
			myClass.mp3Size = source.readString();
			myClass.lrcName = source.readString();
			myClass.lrcSize = source.readString();
		    return myClass;
		
		}
	
		//��Ҫʵ�ֵ�Creator�ӿڷ���
		public Mp3Info[] newArray(int size) {
		    return new Mp3Info[size];
		}
	};

}
