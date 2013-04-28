package jp.niconico.api.method;

import jp.niconico.api.entity.LoginInfo;
import jp.niconico.api.entity.Mylist;
import jp.niconico.api.entity.MylistItem;
import jp.niconico.api.exception.NiconicoException;
import jp.niconico.api.http.HttpClientSetting;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.util.List;

public class NicoGetMylist {
    private String deflistUrl = "http://nicovideo.jp/api/deflist/list";

    private String mylistgroupUrl = "http://nicovideo.jp/api/mylistgroup/list";

    private String mylistUrl = "http://nicovideo.jp/api/mylist/list?group_id=";

    private LoginInfo loginInfo;

    public NicoGetMylist(LoginInfo loginInfo) {
        this.loginInfo = loginInfo;
    }

    public List<MylistItem> getToriaezuMylist() throws NiconicoException {
        DefaultHttpClient httpClient = null;
        List<MylistItem> list = null;
        try {
            httpClient = HttpClientSetting.createHttpClient();
            httpClient.setCookieStore(loginInfo.cookie);
            HttpGet httpGet = new HttpGet(deflistUrl);

            HttpResponse response = httpClient.execute(httpGet);

            String json = EntityUtils.toString(response.getEntity());
            list = MylistItem.parse(json);
        } catch (Exception e) {
            throw new NiconicoException(e.getMessage());
        } finally {
            if (httpClient != null) {
                httpClient.getConnectionManager().shutdown();
            }
        }

        return list;
    }

    public List<Mylist> getOwnerMylists() throws NiconicoException {
        DefaultHttpClient httpClient = null;
        List<Mylist> list = null;
        try {
            httpClient = HttpClientSetting.createHttpClient();
            httpClient.setCookieStore(loginInfo.cookie);
            HttpGet httpGet = new HttpGet(mylistgroupUrl);
            HttpResponse response = httpClient.execute(httpGet);

            String json = EntityUtils.toString(response.getEntity());
            list = Mylist.parse(json);
        } catch (Exception e) {
            throw new NiconicoException(e);
        } finally {
            if (httpClient != null) {
                httpClient.getConnectionManager().shutdown();
            }
        }

        return list;
    }

    public List<MylistItem> getMylistItems(String mylistId) throws NiconicoException {
        DefaultHttpClient httpClient = null;
        List<MylistItem> list = null;
        try {
            httpClient = new DefaultHttpClient();
            httpClient.setCookieStore(loginInfo.cookie);
            HttpGet httpGet = new HttpGet(mylistUrl + mylistId);

            HttpResponse response = httpClient.execute(httpGet);

            String json = EntityUtils.toString(response.getEntity());
            list = MylistItem.parse(json);
        } catch (Exception e) {
            throw new NiconicoException(e);
        } finally {
            if (httpClient != null) {
                httpClient.getConnectionManager().shutdown();
            }
        }

        return list;
    }

}
