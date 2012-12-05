package com.example.xml;

import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import com.example.model.*;
public class Mp3ListContentHandler extends DefaultHandler{
	String tagName;
	List<Mp3Info> infos = null;
	Mp3Info info = null;
	public Mp3ListContentHandler(List<Mp3Info> infos){
		this.infos = infos;
	}	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		// TODO Auto-generated method stub		
		String content = new String(ch,start,length);
		if(tagName.equals("id")){
			info.setId(content);
		}
		if(tagName.equals("mp3.name")){
			info.setMp3Name(content);
		}
		if(tagName.equals("mp3.size")){
			info.setMp3Size(content);
		}
		if(tagName.equals("lrc.name")){
			info.setLrcName(content);
		}
		if(tagName.equals("lrc.size")){
			info.setLrcSize(content);
		}
	}
	@Override
	public void endDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.endDocument();	
	}
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		// TODO Auto-generated method stub
		
		if(qName.equals("resources")){
			infos.add(info);
		}
		tagName = "";
	}
	@Override
	public void startDocument() throws SAXException {
		// TODO Auto-generated method stub
		super.startDocument();		
	}
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		// TODO Auto-generated method stub		
		tagName = localName;
		if(tagName.equals("resources")){
			info = new Mp3Info();
		}
	}
	
}
