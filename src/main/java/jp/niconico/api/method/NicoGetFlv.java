package jp.niconico.api.method;

import java.net.URLDecoder;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import jp.niconico.api.entity.FlvInfo;
import jp.niconico.api.entity.LoginInfo;
import jp.niconico.api.exception.NiconicoException;

public class NicoGetFlv {
    private LoginInfo loginInfo = null;

    private String methodUrl = "http://flapi.nicovideo.jp/api/getflv/";

    public NicoGetFlv(LoginInfo loginInfo) {
        this.loginInfo = loginInfo;
    }

    public FlvInfo excute(String id) throws NiconicoException {
        if (loginInfo == null) {
            throw new NiconicoException("Need to login.");
        }

        DefaultHttpClient httpClient = null;
        FlvInfo info = null;

        try {
            StringBuilder url = new StringBuilder(methodUrl);
            url.append(id);
            if (id.startsWith("nm")) {
                url.append("?as3=1");
            }

            httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url.toString());
            httpClient.setCookieStore(loginInfo.cookie);

            HttpResponse response = httpClient.execute(httpGet);

            HttpEntity entity = response.getEntity();
            String[] results = EntityUtils.toString(entity).split("&");
            info = new FlvInfo();
            for (String str : results) {
                String[] pair = str.split("=");
                pair[0] = URLDecoder.decode(pair[0], "UTF-8");
                if (pair.length == 2) {
                    pair[1] = URLDecoder.decode(pair[1], "UTF-8");
                }

                if ("thread_id".equals(pair[0])) {
                    info.threadId = pair[1];
                } else if ("l".equals(pair[0])) {
                    info.l = Integer.parseInt(pair[1]);
                } else if ("url".equals(pair[0])) {
                    info.url = pair[1];
                } else if ("link".equals(pair[0])) {
                    info.link = pair[1];
                } else if ("ms".equals(pair[0])) {
                    info.ms = pair[1];
                } else if ("ms_sub".equals(pair[0])) {
                    info.msSub = pair[1];
                } else if ("user_id".equals(pair[0])) {
                    info.userId = pair[1];
                } else if ("is_premium".equals(pair[0])) {
                    info.isPremium = pair[1];
                } else if ("time".equals(pair[0])) {
                    info.time = Long.parseLong(pair[1]);
                } else if ("done".equals(pair[0])) {
                    info.done = Boolean.parseBoolean(pair[1]);
                } else if ("hms".equals(pair[0])) {
                    info.hms = pair[1];
                } else if ("hmsp".equals(pair[0])) {
                    info.hmsp = pair[1];
                } else if ("hmst".equals(pair[0])) {
                    info.hmst = pair[1];
                } else if ("hmstk".equals(pair[0])) {
                    info.hmstk = pair[1];
                } else if ("rpu".equals(pair[0])) {
                    info.rpu = pair[1];
                }
            }
        } catch (Exception e) {
            throw new NiconicoException(e.getMessage());
        } finally {
            if (httpClient == null) {
                httpClient.getConnectionManager().shutdown();
            }
        }

        if (StringUtils.isBlank(info.threadId)) {
            throw new NiconicoException("Failed to get threadId");
        }

        return info;
    }
}
