package jp.niconico.api.method;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jp.niconico.api.entity.CommentInfo;
import jp.niconico.api.entity.FlvInfo;
import jp.niconico.api.entity.LoginInfo;
import jp.niconico.api.exception.NiconicoException;

public class NicoGetComment {
	private Logger logger = LoggerFactory.getLogger(NicoGetComment.class);

	private String threadKeyUrl = "http://flapi.nicovideo.jp/api/getthreadkey?thread=";

	private FlvInfo flvInfo = null;

	private LoginInfo loginInfo = null;

	public NicoGetComment(LoginInfo loginInfo, FlvInfo flvInfo) {
		this.loginInfo = loginInfo;
		this.flvInfo = flvInfo;
	}

	public List<CommentInfo> excute(String id, int page) throws NiconicoException {
		DefaultHttpClient httpClient = null;
		List<CommentInfo> list = new ArrayList<CommentInfo>();

		try {
			httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(threadKeyUrl + flvInfo.threadId);
			httpClient.setCookieStore(loginInfo.cookie);
			HttpResponse response = httpClient.execute(httpGet);

			String[] tmps = EntityUtils.toString(response.getEntity()).split(
					"&");
			String threadKey = null;
			String force_184 = null;
			for (String tmp : tmps) {
				String[] pair = tmp.split("=");
				if ("threadkey".equals(pair[0])) {
					if (pair.length < 2 || StringUtils.isBlank(pair[1])) {
						threadKey = null;
					} else {
						threadKey = pair[1];
					}
				} else if ("force_184".equals(pair[0])) {
					if (pair.length < 2 || StringUtils.isBlank(pair[1])) {
						force_184 = null;
					} else {
						force_184 = pair[1];
					}

				}
			}

			StringBuilder xml = new StringBuilder();
			if (StringUtils.isBlank(threadKey)) {
				xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
				xml.append("<packet>");
				xml.append("<thread ");
				xml.append("thread=\"" + flvInfo.threadId + "\" ");
				xml.append("version=\"20090904\" ");
				xml.append("user_id=\"" + flvInfo.userId + "\"");
				xml.append("/>");
				xml.append("<thread_leaves ");
				xml.append("thread=\"" + flvInfo.threadId + "\" ");
				xml.append("user_id=\"" + flvInfo.userId + "\"");
				xml.append(">");
				xml.append("0-9999:1,10000");
				xml.append("</thread_leaves>");
				xml.append("</packet>");
			} else {
				// TODO 動作確認
				xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
				xml.append("<packet>");
				xml.append("<thread ");
				xml.append("thread=\"" + flvInfo.threadId + "\" ");
				xml.append("version=\"20090904\" ");
				xml.append("user_id=\"" + flvInfo.userId + "\"　");
				xml.append("threadkey=\"" + threadKey + "\"　");
				if (StringUtils.isNotBlank(force_184)) {
					xml.append("force_184=\"" + force_184 + "\"");
				}
				xml.append("/>");
				xml.append("<thread_leaves ");
				xml.append("thread=\"" + flvInfo.threadId + "\" ");
				xml.append("user_id=\"" + flvInfo.userId + "\" ");
				xml.append("threadkey=\"" + threadKey + "\"　");
				if (StringUtils.isNotBlank(force_184)) {
					xml.append("force_184=\"" + force_184 + "\"");
				}
				xml.append(">");
				xml.append("0-9999:1,10000");
				xml.append("</thread_leaves>");
				xml.append("</packet>");
			}

			HttpPost httpPost = new HttpPost(flvInfo.ms);
			httpPost.setEntity(new StringEntity(xml.toString()));
			response = httpClient.execute(httpPost);

			String responseXml = EntityUtils.toString(response.getEntity(),
					"UTF-8");

			list = CommentInfo.parse(id, responseXml);

		} catch (Exception e) {
			throw new NiconicoException(e.getMessage());
		} finally {
			if (httpClient != null) {
				httpClient.getConnectionManager().shutdown();
			}
		}

		return list;
	}
}
