package com.example.preference;

public class Key<T> {

	private String key;
	private T defaultValue;
	public Key(String key,T defaultValue){
		this.key = key;
		this.defaultValue = defaultValue;
	}
	public T getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(T defaultValue) {
		this.defaultValue = defaultValue;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getKey() {
		return key;
	}

}
