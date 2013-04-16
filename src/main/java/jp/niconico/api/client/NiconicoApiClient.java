package jp.niconico.api.client;

import java.util.List;

import jp.niconico.api.entity.SearchResult;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NiconicoApiClient {
	private Logger logger = LoggerFactory.getLogger(NiconicoApiClient.class);

	private String mail;

	private String password;

	private CookieStore cookie = null;

	private final String SEARCH_URL = "http://ext.nicovideo.jp/api/search/";

	public void login(String mail, String password) {
		if (StringUtils.isBlank(mail) || StringUtils.isBlank(password)) {
			return;
		}
		this.mail = mail;
		this.password = password;
		cookie = null;

		DefaultHttpClient httpClient = null;
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
			cookie = httpClient.getCookieStore();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (httpClient != null) {
				httpClient.getConnectionManager().shutdown();
			}
		}

	}

	public List<SearchResult> search(String query, String sort, int page,
			String order, boolean tagSearch) {
		if (cookie == null) {
			return null;
		}

		List<SearchResult> results = null;
		DefaultHttpClient httpClient = null;
		try {
			StringBuilder url = new StringBuilder(SEARCH_URL);
			if (tagSearch) {
				url.append("tag");
			} else {
				url.append("search");
			}
			url.append("/" + query);

			url.append("?mode=watch&");
			url.append("order=" + order);
			url.append("&");
			url.append("page=" + page);
			url.append("&");
			url.append("sort=" + sort);

			httpClient = new DefaultHttpClient();
			httpClient.setCookieStore(cookie);
			HttpGet httpGet = new HttpGet(url.toString());
			HttpResponse response = httpClient.execute(httpGet);

			HttpEntity entity = response.getEntity();
			String json = EntityUtils.toString(entity);
			
			if(logger.isDebugEnabled()) {
				logger.info("response: " + json);
			}
			results = SearchResult.parse(json);

		} catch (Exception e) {

		} finally {
			if (httpClient != null) {
				httpClient.getConnectionManager().shutdown();
			}
		}

		return results;

	}

}
