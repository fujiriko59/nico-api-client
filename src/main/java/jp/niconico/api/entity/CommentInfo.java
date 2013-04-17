package jp.niconico.api.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CommentInfo {

	public String id;

	public long no;

	public long vpos;

	public long date;

	public String mail;

	public String userId;

	public boolean anonymity = false;

	public boolean premium = false;

	public String msg;

	public static List<CommentInfo> parse(String id, String xml) {
		List<CommentInfo> list = new ArrayList<CommentInfo>();

		String tmp = xml;

		while (tmp.indexOf("<chat") > -1) {
			String elem = tmp.substring(tmp.indexOf("<chat"),
					tmp.indexOf("</chat>") + "</chat>".length());
			tmp = tmp.substring(tmp.indexOf("</chat>") + "</chat>".length());

			CommentInfo info = new CommentInfo();
			info.id = id;
			info.msg = elem.substring(elem.indexOf(">") + 1,
					elem.indexOf("</chat>"));

			String[] attributes = elem.substring(0, elem.indexOf(">"))
					.replace("\"", "").split(" ");

			for (String attribute : attributes) {
				String[] pair = attribute.split("=");

				if (pair.length == 2) {
					if ("no".equals(pair[0])) {
						info.no = Long.parseLong(pair[1]);
					} else if ("vpos".equals(pair[0])) {
						info.vpos = Long.parseLong(pair[1]);
					} else if ("date".equals(pair[0])) {
						info.date = Long.parseLong(pair[1]);
					} else if ("mail".equals(pair[0])) {
						info.mail = pair[1];
					} else if ("user_id".equals(pair[0])) {
						info.userId = pair[1];
					} else if ("anonymity".equals(pair[0])) {
						if ("1".equals(pair[1])) {
							info.anonymity = true;
						}
					} else if ("premium".equals(pair[0])) {
						if ("1".equals(pair[1])) {
							info.premium = true;
						}
					}
				}
			}
			list.add(info);
		}

		sort("date", "DESC", list);
		return list;
	}

	@SuppressWarnings("unchecked")
	public static void sort(String key, String order, List<CommentInfo> list) {
		Collections.sort(list, new CommentComparator(key, order));
	}

	@SuppressWarnings("rawtypes")
	public static class CommentComparator implements Comparator {
		public String key;

		public String order;

		public CommentComparator(String key, String order) {
			this.key = key;
			this.order = order;
		}

		@Override
		public int compare(Object o1, Object o2) {
			int ret = 0;

			CommentInfo info1 = (CommentInfo) o1;
			CommentInfo info2 = (CommentInfo) o2;

			long value1 = 0;
			long value2 = 0;

			if ("date".equals(key)) {
				value1 = info1.date;
				value2 = info2.date;
			} else if ("no".equals(key)) {
				value1 = info1.no;
				value2 = info2.no;
			} else if ("vpos".equals(key)) {
				value1 = info1.vpos;
				value2 = info2.vpos;
			}

			if (value1 > value2) {
				ret = 1;
			} else if (value1 < value2) {
				ret = -1;
			} else {
				ret = 0;
			}

			if ("DESC".equals(order)) {
				ret = ret * (-1);
			}

			return ret;
		}

	}
}
