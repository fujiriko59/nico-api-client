package jp.niconico.api.method;

import jp.niconico.api.entity.LoginInfo;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NicoLogin {
	private Logger logger = LoggerFactory.getLogger(NicoLogin.class);
	
	public LoginInfo excute(String mail, String password) {
		DefaultHttpClient httpClient = null;
		LoginInfo info = null;
		try {
			httpClient = new DefaultHttpClient();

			HttpPost httpPost = new HttpPost(
					"https://secure.nicovideo.jp:443/secure/login?site=niconico");
			Header[] headers = { new BasicHeader("Content-type",
					"application/x-www-form-urlencoded") };
			httpPost.setHeaders(headers);
			httpPost.setEntity(new StringEntity("mail=" + mail + "&password="
					+ password, "UTF-8"));

			logger.info("login: " + mail);

			HttpResponse response = httpClient.execute(httpPost);
			logger.info(response.getStatusLine().toString());
			
			info = new LoginInfo();
			info.cookie = httpClient.getCookieStore();
			info.mail = mail;
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
