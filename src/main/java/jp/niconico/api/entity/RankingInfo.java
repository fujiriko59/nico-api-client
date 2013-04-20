package jp.niconico.api.entity;

import jp.niconico.api.exception.NiconicoException;

import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

public class RankingInfo {
    public int rank;

    public String rankKind;

    public String period;

    public String title;

    public String link;

    public String pubDate;

    public String date;

    public String description;

    public String thumbnailUrl;

    public String length;

    public long viewCounter;

    public long commentNum;

    public long mylistCounter;

    public long viewCounterPeriod;

    public long commentNumPeriod;

    public long mylistCounterPeriod;

    public static List<RankingInfo> parse(String period, String rankKind, String xml) throws NiconicoException {
        List<RankingInfo> list = new ArrayList<RankingInfo>();

        try {
            ByteArrayInputStream in = new ByteArrayInputStream(xml.getBytes("UTF-8"));
            Rss root = JAXB.unmarshal(in, Rss.class);

            for (int i = 0; i < root.channel.item.size(); i++) {
                Rss.Channel.Item item = root.channel.item.get(i);

                RankingInfo info = new RankingInfo();
                info.rank = i + 1;
                info.period = period;
                info.rankKind = rankKind;
                info.title = item.title.substring(item.title.indexOf("：") + 1);
                info.link = item.link;
                info.pubDate = item.pubDate;

                String desc = item.description;
                desc = desc.substring(desc.indexOf("src=\"") + "src=\"".length());
                info.thumbnailUrl = desc.substring(0, desc.indexOf("\""));

                desc = desc.substring(desc.indexOf("nico-description\">") + "nico-description\">".length());
                info.description = desc.substring(0, desc.indexOf("</p>"));

                desc = desc.substring(desc.indexOf("nico-info-length\">") + "nico-info-length\">".length());
                info.length = desc.substring(0, desc.indexOf("</strong>"));

                desc = desc.substring(desc.indexOf("nico-info-date\">") + "nico-info-date\">".length());
                info.date = desc.substring(0, desc.indexOf("</strong>"));

                desc = desc.substring(desc.indexOf("nico-info-total-view\">") + "nico-info-total-view\">".length());
                info.viewCounter = Long.parseLong(desc.substring(0, desc.indexOf("</strong>")).replace(",", ""));

                desc = desc.substring(desc.indexOf("nico-info-total-res\">") + "nico-info-total-res\">".length());
                info.commentNum = Long.parseLong(desc.substring(0, desc.indexOf("</strong>")).replace(",", ""));

                desc = desc.substring(desc.indexOf("nico-info-total-mylist\">") + "nico-info-total-mylist\">".length());
                info.mylistCounter = Long.parseLong(desc.substring(0, desc.indexOf("</strong>")).replace(",", ""));

                String tmpPeriod;
                if ("hourly".equals(period) || "daily".equals(period) || "weekly".equals(period) || "monthly".equals(period)) {
                    tmpPeriod = period;
                } else {
                    tmpPeriod = "daily";
                }

                String classname = "nico-info-" + tmpPeriod + "-view\">";
                desc = desc.substring(desc.indexOf(classname) + classname.length());
                info.viewCounterPeriod = Long.parseLong(desc.substring(0, desc.indexOf("</strong>")).replace(",", ""));

                classname = "nico-info-" + tmpPeriod + "-res\">";
                desc = desc.substring(desc.indexOf(classname) + classname.length());
                info.commentNumPeriod = Long.parseLong(desc.substring(0, desc.indexOf("</strong>")).replace(",", ""));

                classname = "nico-info-" + tmpPeriod + "-mylist\">";
                desc = desc.substring(desc.indexOf(classname) + classname.length());
                info.mylistCounterPeriod = Long.parseLong(desc.substring(0, desc.indexOf("</strong>")).replace(",", ""));

                list.add(info);
            }
        } catch (Exception e) {
            throw new NiconicoException(e.getMessage());
        }

        return list;
    }

    @XmlRootElement
    public static class Rss {
        public Channel channel;

        public static class Channel {
            public List<Item> item;

            public static class Item {
                public String title;

                public String link;

                public String pubDate;

                public String description;
            }
        }
    }

}
