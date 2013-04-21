package jp.niconico.api.client;

import java.io.File;
import java.util.List;

import jp.niconico.api.entity.*;
import jp.niconico.api.exception.NiconicoException;
import jp.niconico.api.method.*;

import org.apache.commons.lang.StringUtils;

public class NiconicoApiClient {
    private LoginInfo loginInfo = null;

    /**
     * niconicoのログイン情報を設定する。
     * @param mail niconicoアカウント
     * @param password niconicoパスワード
     * @throws NiconicoException
     */
    public void login(String mail, String password) throws NiconicoException {
        loginInfo = null;
        NicoLogin method = new NicoLogin();
        loginInfo = method.excute(mail, password);
    }

    /**
     * ログイン状態の取得。
     * @return true:ログインしている false:ログインしていない
     */
    public boolean isLogin() {
        boolean ret = true;
        if (loginInfo == null || StringUtils.isBlank(loginInfo.mail) || loginInfo.cookie == null) {
            ret = false;
        }
        return ret;
    }

    /**
     * 動画を検索。
     * @param query 検索クエリ
     * @param sort ソート種別 n:コメント日時 v:再生数 m:マイリスト r:コメント数 f:投稿日時 l:再生時間
     * @param page 取得ページ
     * @param order d:降順 a:昇順
     * @param tagSearch false:キーワード検索 true:タグ検索
     * @return 検索結果
     * @throws NiconicoException
     */
    public List<SearchResult> search(String query, String sort, int page,
                                     String order, boolean tagSearch) throws NiconicoException {
        if (loginInfo == null) {
            return null;
        }

        NicoSearch method = new NicoSearch(loginInfo);
        return method.excute(query, sort, page, order, tagSearch);
    }

    /**
     * Flv情報取得。
     * @param id 動画ID
     * @return Flv情報
     * @throws NiconicoException
     */
    public FlvInfo getFlv(String id) throws NiconicoException {
        NicoGetFlv method = new NicoGetFlv(loginInfo);
        return method.excute(id);
    }

    /**
     * コメント取得。動画上に表示されているコメントを取得する。
     * @param id 動画ID
     * @return コメント
     * @throws NiconicoException
     */
    public List<CommentInfo> getComment(String id) throws NiconicoException {
        FlvInfo flvInfo = getFlv(id);

        NicoGetComment method = new NicoGetComment(loginInfo, flvInfo);
        return method.excute(id, 0);
    }

    /**
     * 動画情報取得。
     * @param id 動画ID
     * @return 動画情報
     * @throws NiconicoException
     */
    public ThumbInfo getThumbInfo(String id) throws NiconicoException {
        NicoGetThumbInfo method = new NicoGetThumbInfo();
        return method.excute(id);
    }

    /**
     * ランキング取得
     * @param period 期間 hourly:1時間 daily:1日 weeky:週間 monthly:月間 total:全ての期間
     * @param rankKind ランキング種別 fav:総合 view:再生数 res:コメント数 mylist:マイリスト数
     * @return ランキング情報
     * @throws NiconicoException
     */
    public List<RankingInfo> getRanking(String period, String rankKind) throws NiconicoException {
        NicoGetRanking method = new NicoGetRanking();
        return method.execute(period, rankKind);
    }

    /**
     * ログインしているユーザのマイリストを取得。
     * @return マイリスト
     * @throws NiconicoException
     */
    public List<Mylist> getOwnerMylists() throws NiconicoException {
        NicoGetMylist method = new NicoGetMylist(loginInfo);
        return method.getOwnerMylists();
    }

    /**
     * マイリストに登録されているアイテムを取得。
     * @param mylistId マイリストID
     * @return マイリストのアイテム
     * @throws NiconicoException
     */
    public List<MylistItem> getMylistItems(String mylistId) throws NiconicoException {
        NicoGetMylist method = new NicoGetMylist(loginInfo);
        return method.getMylistItems(mylistId);
    }

    /**
     * ログインしているユーザーのとりあえずマイリストを取得。
     * @return とりあえずマイリストのアイテム
     * @throws NiconicoException
     */
    public List<MylistItem> getToriaezuMylist() throws NiconicoException {
        NicoGetMylist method = new NicoGetMylist(loginInfo);
        return method.getToriaezuMylist();
    }

    /**
     * 動画をダウンロード。
     * @param id 動画ID
     * @param destDir ダウンロード先ディレクトリ
     * @return ダウンロードされたファイル
     * @throws NiconicoException
     */
    public File downloadVideo(String id, String destDir) throws NiconicoException {
        FlvInfo flvInfo = getFlv(id);
        ThumbInfo thumbInfo = getThumbInfo(id);
        NicoDownloadVideo method = new NicoDownloadVideo(id, loginInfo, flvInfo, thumbInfo);
        return method.execute(destDir);
    }
}
