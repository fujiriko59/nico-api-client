package jp.niconico.api.client;

import jp.niconico.api.entity.*;
import jp.niconico.api.exception.NiconicoException;
import junit.framework.TestCase;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.util.List;

public class NiconicoApiClientTest extends TestCase {
    private String mail = "";

    private String password = "";

    public NiconicoApiClientTest(String name) {
        super(name);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        //request interval...
        Thread.sleep(3000);
    }

    public void test_login_success() {
        NiconicoApiClient client = new NiconicoApiClient();
        try {
            assertFalse(client.isLogin());
            client.login(mail, password);
            assertTrue(client.isLogin());
        } catch (NiconicoException e) {
            fail(e.getMessage());
        }
    }

    public void test_login_fail() {
        NiconicoApiClient client = new NiconicoApiClient();
        try {
            client.login("invalidUser", "invalidPassword");
        } catch (NiconicoException e) {
            assertFalse(client.isLogin());
            return;
        }
        fail();
    }

    public void test_search_success() {
        NiconicoApiClient client = new NiconicoApiClient();
        try {
            client.login(mail, password);
            boolean tagsearch = false;
            for (int i = 0; i < 2; i++) {
                List<SearchResult> results = client.search("とある科学の超電磁砲", "m", 1, "d", tagsearch);
                assertTrue(results.size() > 0);
                for (SearchResult result : results) {
                    assertNotNull(result.id);
                    assertNotNull(result.descriptionShort);
                    assertNotNull(result.firstRetrieve);
                    assertNotNull(result.thumbnailUrl);
                    assertNotNull(result.title);
                    assertTrue(result.mylistCounter > 0);
                    assertTrue(result.viewCounter > 0);
                    assertTrue(result.numRes > 0);
                }
                tagsearch = true;
                Thread.sleep(3000);
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void test_get_comment_success() {
        NiconicoApiClient client = new NiconicoApiClient();
        try {
            client.login(mail, password);
            List<CommentInfo> comments = client.getComment("sm14027065");
            assertTrue(comments.size() > 1000);
            for (CommentInfo comment : comments) {
                assertTrue(StringUtils.isNotBlank(comment.msg));
                assertNotNull(comment.id);
                assertNotNull(comment.userId);
                assertTrue(comment.date > 0);
                assertTrue(comment.vpos >= 0);
            }
        } catch (NiconicoException e) {
            fail(e.getMessage());
        }
    }

    public void test_get_thumbinfo_success() {
        NiconicoApiClient client = new NiconicoApiClient();
        try {
            ThumbInfo info = client.getThumbInfo("sm20610463");
            assertTrue(StringUtils.isNotEmpty(info.title));
            assertNotNull(info.id);
            assertNotNull(info.lastResBody);
            assertNotNull(info.length);
            assertNotNull(info.userId);
            assertTrue(info.viewCounter > 0);
            assertTrue(info.commentNum > 0);
            assertTrue(info.mylistCounter > 0);
        } catch (NiconicoException e) {
            fail(e.getMessage());
        }
    }

    public void test_get_ranking_success() {
        NiconicoApiClient client = new NiconicoApiClient();
        try {
            String[] periods = {"hourly", "daily", "weekly", "monthly", "total"};
            String[] kinds = {"fav", "view", "res", "mylist"};
            for (String period : periods) {

                for (String kind : kinds) {
                    List<RankingInfo> results = client.getRanking(period, kind);
                    assertTrue(results.size() == 100);
                    long rank = 1;
                    for (RankingInfo result : results) {
                        assertTrue(StringUtils.isNotEmpty(result.title));
                        assertNotNull(result.link);
                        assertNotNull(result.date);
                        assertNotNull(result.description);
                        assertNotNull(result.period);
                        assertNotNull(result.thumbnailUrl);
                        assertTrue(result.viewCounter > 0);
                        assertTrue(result.viewCounter >= result.viewCounterPeriod);
                        assertTrue(result.commentNum > 0);
                        assertTrue(result.commentNum >= result.commentNumPeriod);
                        assertTrue(result.mylistCounter > 0);
                        assertTrue(result.mylistCounter >= result.mylistCounterPeriod);
                        assertEquals(result.rank, rank);
                        rank++;
                    }

                    Thread.sleep(3000);
                }
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void test_get_mylist_success() {
        NiconicoApiClient client = new NiconicoApiClient();
        try {
            client.login(mail, password);
            List<Mylist> mylists = client.getOwnerMylists();
            assertTrue(mylists.size() > 0);
            for (Mylist mylist : mylists) {
                assertTrue(StringUtils.isNotBlank(mylist.id));
                assertTrue(StringUtils.isNotEmpty(mylist.name));
                assertNotNull(mylist.description);
                assertNotNull(mylist.userId);
                assertNotNull(mylist.updateTime);

                Thread.sleep(1000);

                List<MylistItem> items = client.getMylistItems(mylist.id);
                //assertTrue(items.size()>0);
                for (MylistItem item : items) {
                    assertTrue(StringUtils.isNotEmpty(item.title));
                    assertTrue(StringUtils.isNotBlank(item.itemId));
                    assertTrue(item.viewCounter > 0);
                }
                Thread.sleep(3000);
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void test_get_とりあえずマイリスト_sccess() {
        NiconicoApiClient client = new NiconicoApiClient();
        try {
            client.login(mail, password);
            List<MylistItem> toriaezu = client.getToriaezuMylist();
            assertTrue(toriaezu.size() > 0);
            for (MylistItem item : toriaezu) {
                assertTrue(StringUtils.isNotEmpty(item.title));
                assertTrue(StringUtils.isNotBlank(item.itemId));
                assertTrue(item.viewCounter > 0);
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }

    }

    public void test_download_success() {
        NiconicoApiClient client = new NiconicoApiClient();
        File file = null;
        try {
            client.login(mail, password);
            file = client.downloadVideo("sm15039386", ".");
            assertTrue(file.isFile());
            assertTrue(file.length() > 1000 * 1000);
        } catch (Exception e) {
            fail(e.getMessage());
        } finally {
            if (file != null && file.isFile()) {
                file.delete();
            }
        }
    }
}
