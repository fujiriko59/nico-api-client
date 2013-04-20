package jp.niconico.api;

import java.util.List;

import jp.niconico.api.client.NiconicoApiClient;
import jp.niconico.api.entity.*;
import jp.niconico.api.exception.NiconicoException;
import jp.niconico.api.method.NicoGetMylist;

/**
 * Hello world!
 */
public class Sample {
    public static void main(String[] args) {
        System.out.println("Hello World!");

        try {
            NiconicoApiClient client = new NiconicoApiClient();
            client.login("", "");

            client.downloadVideo("sm15039386", ".");
            /* mylist test
            List<Mylist> list = client.getOwnerMylists();
            for (Mylist mylist : list) {
                System.out.println("mylistName: " + mylist.name);
                List<MylistItem> items = client.getMylistItems(mylist.id);
                for (MylistItem item : items) {
                    System.out.println(item.title);
                }
                System.out.println("");
            }
            */

            /* ranking test
            List<RankingInfo> list = client.getRanking("daily", "fav");
            for (RankingInfo info : list) {
                System.out.println(info.title);
            }
            */

			/*
             * test thumbinfo ThumbInfo info =
			 * client.getThumbInfo("sm20610463");
			 * System.out.println(info.title);
			 */

            /* test ranking
            List<CommentInfo> list = client.getComment("sm14027065");
            for (CommentInfo info : list) {
                System.out.println(info.msg);
            }
            */

            /*
             * search test List<SearchResult> results =
			 * client.search("とある科学の超電磁砲", "m", 1, "d", true); int counter = 0;
			 * for(SearchResult result: results) { System.out.println("id:" +
			 * result.id); System.out.println("title:" + result.title);
			 * System.out.println("view:" + result.viewCounter);
			 * System.out.println("comment:" + result.numRes);
			 * System.out.println("mylist:" + result.mylistCounter);
			 * System.out.println(result.descriptionShort);
			 * System.out.println(""); counter++; } System.out.println("total:"
			 * + counter);
			 */
        } catch (NiconicoException e) {
            e.printStackTrace();
        }
    }
}
