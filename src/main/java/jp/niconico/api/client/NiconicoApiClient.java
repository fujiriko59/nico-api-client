package jp.niconico.api.client;

import java.io.File;
import java.util.List;

import jp.niconico.api.entity.*;
import jp.niconico.api.exception.NiconicoException;
import jp.niconico.api.method.*;

import org.apache.commons.lang.StringUtils;

public class NiconicoApiClient {
    private LoginInfo loginInfo = null;

    public void login(String mail, String password) throws NiconicoException {
        loginInfo = null;
        NicoLogin method = new NicoLogin();
        loginInfo = method.excute(mail, password);
    }

    public boolean isLogin() {
        boolean ret = true;
        if (loginInfo == null || StringUtils.isBlank(loginInfo.mail) || loginInfo.cookie == null) {
            ret = false;
        }
        return ret;
    }

    public List<SearchResult> search(String query, String sort, int page,
                                     String order, boolean tagSearch) throws NiconicoException {
        if (loginInfo == null) {
            return null;
        }

        NicoSearch method = new NicoSearch(loginInfo);
        return method.excute(query, sort, page, order, tagSearch);
    }

    public FlvInfo getFlv(String id) throws NiconicoException {
        NicoGetFlv method = new NicoGetFlv(loginInfo);
        return method.excute(id);
    }

    public List<CommentInfo> getComment(String id) throws NiconicoException {
        FlvInfo flvInfo = getFlv(id);

        NicoGetComment method = new NicoGetComment(loginInfo, flvInfo);
        return method.excute(id, 0);
    }

    public ThumbInfo getThumbInfo(String id) throws NiconicoException {
        NicoGetThumbInfo method = new NicoGetThumbInfo();
        return method.excute(id);
    }

    public List<RankingInfo> getRanking(String period, String rankKind) throws NiconicoException {
        NicoGetRanking method = new NicoGetRanking();
        return method.execute(period, rankKind);
    }

    public List<Mylist> getOwnerMylists() throws NiconicoException {
        NicoGetMylist method = new NicoGetMylist(loginInfo);
        return method.getOwnerMylists();
    }

    public List<MylistItem> getMylistItems(String mylistId) throws NiconicoException {
        NicoGetMylist method = new NicoGetMylist(loginInfo);
        return method.getMylistItems(mylistId);
    }

    public List<MylistItem> getToriaezuMylist() throws NiconicoException {
        NicoGetMylist method = new NicoGetMylist(loginInfo);
        return method.getToriaezuMylist();
    }

    public File downloadVideo(String id, String destDir) throws NiconicoException {
        FlvInfo flvInfo = getFlv(id);
        ThumbInfo thumbInfo = getThumbInfo(id);
        NicoDownloadVideo method = new NicoDownloadVideo(id, loginInfo, flvInfo, thumbInfo);
        return method.execute(destDir);
    }
}
