package jp.niconico.api.client;

import java.util.List;

import jp.niconico.api.entity.CommentInfo;
import jp.niconico.api.entity.FlvInfo;
import jp.niconico.api.entity.LoginInfo;
import jp.niconico.api.entity.SearchResult;
import jp.niconico.api.entity.ThumbInfo;
import jp.niconico.api.exception.NiconicoException;
import jp.niconico.api.method.NicoGetComment;
import jp.niconico.api.method.NicoGetFlv;
import jp.niconico.api.method.NicoGetThumbInfo;
import jp.niconico.api.method.NicoLogin;
import jp.niconico.api.method.NicoSearch;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NiconicoApiClient {
	private Logger logger = LoggerFactory.getLogger(NiconicoApiClient.class);

	private LoginInfo loginInfo = null;

	public void login(String mail, String password) throws NiconicoException {		
		NicoLogin method = new NicoLogin();
		loginInfo = method.excute(mail, password);
	}

	public List<SearchResult> search(String query, String sort, int page,
			String order, boolean tagSearch) throws NiconicoException{
		if (loginInfo == null) {
			return null;
		}

		NicoSearch method = new NicoSearch(loginInfo);
		return method.excute(query, sort, page, order, tagSearch);
	}
	
	public FlvInfo getFlv(String id) throws NiconicoException{
		if(loginInfo == null) {
			return null;
		}
		
		NicoGetFlv method = new NicoGetFlv(loginInfo);
		return method.excute(id);
	}
	
	public List<CommentInfo> getComment(String id) throws NiconicoException{
		FlvInfo flvInfo = getFlv(id);
		
		NicoGetComment method = new NicoGetComment(loginInfo, flvInfo);
		return method.excute(id, 0);
	}
	
	public ThumbInfo getThumbInfo(String id) throws NiconicoException {
		NicoGetThumbInfo method = new NicoGetThumbInfo();
		return method.excute(id);
	}
	
}
