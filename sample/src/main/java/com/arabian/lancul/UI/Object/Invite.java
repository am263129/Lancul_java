package com.arabian.lancul.UI.Object;



public class Invite {
    public String invite_content;
    public String invite_date;
    public String invite_sender;
    public String invite_status;


    public Invite(){

    }

    public Invite(String invite_content, String invite_date, String invite_sender, String invite_status){
        this.invite_content = invite_content;
        this.invite_date = invite_date;
        this.invite_sender = invite_sender;
        this.invite_status  = invite_status;

    }

    public String getInvite_content() {
        return invite_content;
    }

    public String getInvite_date() {
        return invite_date;
    }

    public String getInvite_sender() {
        return invite_sender;
    }

    public String getInvite_status() {
        return invite_status;
    }

    public void setInvite_content(String invite_content) {
        this.invite_content = invite_content;
    }

    public void setInvite_date(String invite_date) {
        this.invite_date = invite_date;
    }

    public void setInvite_sender(String invite_sender) {
        this.invite_sender = invite_sender;
    }

    public void setInvite_status(String invite_status) {
        this.invite_status = invite_status;
    }
}

