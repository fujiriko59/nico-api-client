package jp.niconico.api.entity;

import net.arnx.jsonic.JSON;
import net.arnx.jsonic.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Mylist {
    public String id = null;

    public String userId = null;

    public String name = null;

    public String description = null;

    public boolean pub = false;

    public int defaultSort = -1;

    public long createTime = -1;

    public long updateTime = -1;

    public int sortOrder = -1;

    public int iconId = -1;

    public static List<Mylist> parse(String json) throws JSONException {
        Pojo pojo = JSON.decode(json, Pojo.class);
        List<Mylist> list = new ArrayList<Mylist>();

        for (Map<String, String> map : pojo.mylistgroup) {
            Mylist mylist = new Mylist();
            mylist.id = map.get("id");
            mylist.userId = map.get("user_id");
            mylist.name = map.get("name");
            mylist.description = map.get("description");
            if ("1".equals(map.get("public"))) {
                mylist.pub = true;
            } else {
                mylist.pub = false;
            }
            mylist.defaultSort = Integer.parseInt(map.get("default_sort"));
            mylist.createTime = Long.parseLong(map.get("create_time"));
            mylist.updateTime = Long.parseLong(map.get("update_time"));
            mylist.sortOrder = Integer.parseInt(map.get("sort_order"));
            mylist.iconId = Integer.parseInt(map.get("icon_id"));

            list.add(mylist);
        }

        return list;
    }

    public static class Pojo {
        public List<Map<String, String>> mylistgroup;
    }
}
