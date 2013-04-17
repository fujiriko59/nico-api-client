package jp.niconico.api.client;

import java.util.List;

import jp.niconico.api.entity.LoginInfo;
import jp.niconico.api.entity.SearchResult;
import jp.niconico.api.method.NicoLogin;
import jp.niconico.api.method.NicoSearch;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NiconicoApiClient {
	private Logger logger = LoggerFactory.getLogger(NiconicoApiClient.class);

	private LoginInfo loginInfo = null;

	public void login(String mail, String password) {
		if (StringUtils.isBlank(mail) || StringUtils.isBlank(password)) {
			return;
		}
		
		NicoLogin method = new NicoLogin();
		loginInfo = method.excute(mail, password);
	}

	public List<SearchResult> search(String query, String sort, int page,
			String order, boolean tagSearch) {
		if (loginInfo == null) {
			return null;
		}

		NicoSearch method = new NicoSearch(loginInfo);
		return method.excute(query, sort, page, order, tagSearch);
	}

}
