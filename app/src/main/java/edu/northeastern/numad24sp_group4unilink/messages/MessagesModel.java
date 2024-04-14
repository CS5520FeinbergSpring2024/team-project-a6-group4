package edu.northeastern.numad24sp_group4unilink.messages;


import com.google.firebase.Timestamp;

import java.util.List;

public class MessagesModel {
    String messagesId;
    List<String> userIds;
    Timestamp lastMessageTimestamp;
    String lastMessageSenderId;
    String lastMessage;

    public MessagesModel() {
    }

    public MessagesModel(String messagesId, List<String> userIds, Timestamp lastMessageTimestamp, String lastMessageSenderId) {
        this.messagesId = messagesId;
        this.userIds = userIds;
        this.lastMessageTimestamp = lastMessageTimestamp;
        this.lastMessageSenderId = lastMessageSenderId;
    }

    public String getMessagesId() {
        return messagesId;
    }

    public void setMessagesId(String messagesId) {
        this.messagesId = messagesId;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public Timestamp getLastMessageTimestamp() {
        return lastMessageTimestamp;
    }

    public void setLastMessageTimestamp(Timestamp lastMessageTimestamp) {
        this.lastMessageTimestamp = lastMessageTimestamp;
    }

    public String getLastMessageSenderId() {
        return lastMessageSenderId;
    }

    public void setLastMessageSenderId(String lastMessageSenderId) {
        this.lastMessageSenderId = lastMessageSenderId;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

}