package com.arabian.lancul.UI.Object;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Chat {
    public String chat_id;
    public String chat_sender;
    public String chat_content;
    public String chat_type;
    @ServerTimestamp
    private Date timestamp;
    public String chat_status;

    public Chat(){

    }

    public Chat(String chat_id, String chat_sender, String chat_content, String chat_type, Date chat_created_date, String chat_status){
        this.chat_id = chat_id;
        this.chat_sender = chat_sender;
        this.chat_content = chat_content;
        this.chat_type = chat_type;
        this.timestamp = chat_created_date;
        this.chat_status = chat_status;
    }

    public String getChat_content() {
        return chat_content;
    }

    public Date getTimestamp() {
        return timestamp;
    }
    public String getChat_id() {
        return chat_id;
    }

    public String getChat_sender() {
        return chat_sender;
    }

    public String getChat_type() {
        return chat_type;
    }

    public String getChat_status() {
        return chat_status;
    }

    public void setChat_content(String chat_content) {
        this.chat_content = chat_content;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setChat_id(String chat_id) {
        this.chat_id = chat_id;
    }

    public void setChat_sender(String chat_sender) {
        this.chat_sender = chat_sender;
    }

    public void setChat_status(String chat_status) {
        this.chat_status = chat_status;
    }

    public void setChat_type(String chat_type) {
        this.chat_type = chat_type;
    }
}
