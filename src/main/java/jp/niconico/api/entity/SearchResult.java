package jp.niconico.api.entity;

import java.util.List;

import net.arnx.jsonic.JSON;

public class SearchResult {
	public String id;
	
	public String title;
	
	public String firstRetrieve;
	
	public long viewCounter;
	
	public long mylistCounter;
	
	public long numRes;
	
	public String length;
	
	public String thumbnailUrl;
	
	public String descriptionShort;
	
	public static List<SearchResult> parse(String json) {
		SearchPojo pojo = null;
		pojo = JSON.decode(json, SearchPojo.class);
		if(pojo == null) {
			return null;
		}
		return pojo.list;
	}
	
	public static class SearchPojo {
		public List<SearchResult> list;
	}
	
}
