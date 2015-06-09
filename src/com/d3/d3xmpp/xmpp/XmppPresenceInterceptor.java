package com.d3.d3xmpp.xmpp;

import org.jivesoftware.smack.PacketInterceptor;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.RosterPacket.ItemType;

import com.d3.d3xmpp.constant.Constants;
import com.d3.d3xmpp.constant.MyApplication;
import com.d3.d3xmpp.model.Friend;

import android.content.Intent;
import android.util.Log;

public class XmppPresenceInterceptor implements PacketInterceptor {

	@Override
	public void interceptPacket(Packet packet) {
		Presence presence = (Presence) packet;
		if(Constants.IS_DEBUG)
		Log.e("xmppchat send", presence.toXML());

		String from = presence.getFrom();// ���ͷ�
		String to = presence.getTo();// ���շ�
		// Presence.Type��7��״̬
		if (presence.getType().equals(Presence.Type.subscribe)) {// ��������
			for (Friend friend : XmppConnection.getInstance().getAllFriends()) {
				if (friend.username.equals(XmppConnection.getUsername(to)) && friend.type == ItemType.from) {// && friend.type == ItemType.from
					friend.type = ItemType.both;
					XmppConnection.friendListTemp.add(friend);
				}
			}
			MyApplication.getInstance().sendBroadcast(new Intent("friendChange"));
		} 
		else if (presence.getType().equals(Presence.Type.subscribed)) {// ͬ����Ӻ���
			if(Constants.IS_DEBUG)
	        Log.e("friend", to+"��ͬ��������");
			for (Friend friend : XmppConnection.getInstance().getAllFriends()) {
				if (friend.username.equals(XmppConnection.getUsername(to)) && friend.type == ItemType.from) {// && friend.type == ItemType.from
					friend.type = ItemType.both;
					XmppConnection.friendListTemp.add(friend);
				}
			}
			MyApplication.getInstance().sendBroadcast(new Intent("friendChange"));
		} 
		else if (presence.getType().equals(Presence.Type.unsubscribe)) {// �ܾ���Ӻ��� ɾ������
			if(Constants.IS_DEBUG)
		       Log.e("friend", to+"�Ҿܾ��������");
			for (Friend friend : XmppConnection.getInstance().getAllFriends()) {
				if (friend.username.equals(XmppConnection.getUsername(to))) {
					friend.type = ItemType.remove;
					XmppConnection.friendListTemp.add(friend);
				}
			}
			MyApplication.getInstance().sendBroadcast(new Intent("friendChange"));
		} else if (presence.getType().equals(Presence.Type.unsubscribed)) {
			if(Constants.IS_DEBUG)
	        Log.e("friend", to+"��ɾ������11");
			for (Friend friend : XmppConnection.getInstance().getAllFriends()) {
				if (friend.username.equals(XmppConnection.getUsername(to))) {
					friend.type = ItemType.remove;
					XmppConnection.friendListTemp.add(friend);
				}
			}
			MyApplication.getInstance().sendBroadcast(new Intent("friendChange"));
		}
	}
}
