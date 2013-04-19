package jp.niconico.api.method;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import jp.niconico.api.entity.ThumbInfo;

public class NicoGetThumbInfo {
	private String methodUrl = "http://ext.nicovideo.jp/api/getthumbinfo/";
	
	public ThumbInfo excute(String id) {
		HttpClient httpClient = null;
		ThumbInfo info = null;
		try {
			httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(methodUrl + id);
			
			HttpResponse response = httpClient.execute(httpGet);
			
			String xml = EntityUtils.toString(response.getEntity());
			info = ThumbInfo.parse(xml);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (httpClient != null) {
				httpClient.getConnectionManager().shutdown();
			}
		}
		
		return info;
	}
}
