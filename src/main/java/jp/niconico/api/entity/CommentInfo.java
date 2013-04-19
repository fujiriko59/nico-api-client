package jp.niconico.api.entity;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

import jp.niconico.api.entity.CommentInfo.Packet.Chat;

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
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(xml.getBytes("UTF-8"));
			Packet root = JAXB.unmarshal(in, Packet.class);
			for(Chat chat: root.chat) {
				CommentInfo info = new CommentInfo();
				info.id = id;
				info.no = chat.no;
				info.vpos = chat.vpos;
				info.date = chat.date;
				info.mail = chat.mail;
				info.userId = chat.user_id;
				if(chat.premium == 1) {
					info.premium = true;
				}
				if(chat.anonymity == 1) {
					info.anonymity = true;
				}
				info.msg = chat.msg;
				list.add(info);
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		sort("date", "DESC", list);
		return list;
	}
	
	@XmlRootElement
	public static class Packet {
		public List<Chat> chat;
	
		public static class Chat {
			@XmlAttribute
			public long no;
			
			@XmlAttribute
			public long vpos;
			
			@XmlAttribute
			public long date;
						
			@XmlAttribute
			public String mail;
			
			@XmlAttribute(name="user_id")
			public String user_id;
			
			@XmlAttribute
			public int premium = 0;
			
			@XmlAttribute
			public int anonymity = 0;
			
			@XmlValue
			public String msg;
		}
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
