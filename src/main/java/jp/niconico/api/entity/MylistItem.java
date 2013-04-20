package jp.niconico.api.entity;

import net.arnx.jsonic.JSON;
import net.arnx.jsonic.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MylistItem {
    public int itemType;

    public String itemId;

    public String description;

    public String videoId;

    public String title;

    public String thumbnailUrl;

    public long firstRetrieve;

    public long updateTimeVideo;

    public long viewCounter;

    public long commentNum;

    public long mylistCounter;

    public String groupType;

    public long length;

    public boolean deleted;

    public String lastResBody;

    public String watchId;

    public boolean watch;

    public long createTime;

    public long updateTime;

    public static List<MylistItem> parse(String json) throws JSONException {
        Logger logger = LoggerFactory.getLogger(MylistItem.class);

        List<MylistItem> list = new ArrayList<MylistItem>();

        Pojo pojo = JSON.decode(json, Pojo.class);
        for (Item item : pojo.mylistitem) {
            if (item.itemType == 0) {
                MylistItem myitem = new MylistItem();

                myitem.itemType = item.itemType;
                myitem.itemId = item.itemId;
                myitem.description = item.description;
                Map<String, String> map = item.itemData;
                myitem.videoId = map.get("video_id");
                myitem.title = map.get("title");
                myitem.thumbnailUrl = map.get("thumbnail_url");
                myitem.firstRetrieve = Long.parseLong(map.get("first_retrieve"));
                myitem.updateTimeVideo = Long.parseLong(map.get("update_time"));
                myitem.viewCounter = Long.parseLong(map.get("view_counter"));
                myitem.commentNum = Long.parseLong(map.get("num_res"));
                myitem.mylistCounter = Long.parseLong(map.get("mylist_counter"));
                myitem.groupType = map.get("group_type");
                myitem.length = Long.parseLong(map.get("length_seconds"));
                myitem.deleted = "1".equals(map.get("deleted"));
                myitem.lastResBody = map.get("last_res_body");
                myitem.watchId = map.get("watch_id");
                myitem.watch = item.watch == 1;
                myitem.createTime = item.createTime;
                myitem.updateTime = item.updateTime;

                list.add(myitem);
            } else {
                logger.info("Unexpected item type:" + item.itemType);
            }
        }
        return list;
    }

    public static class Pojo {
        public List<Item> mylistitem;
    }

    public static class Item {
        public int itemType;

        public String itemId;

        public String description;

        public Map<String, String> itemData;

        public int watch;

        public long createTime;

        public long updateTime;
    }

}