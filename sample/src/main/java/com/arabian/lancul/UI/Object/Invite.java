package com.arabian.lancul.UI.Object;



public class Invite {
    public String invite_content;
    public String invite_date;
    public String invite_sender_name;
    public String invite_status;
    public String invite_sender_email;
    public String invite_require_time;


    public Invite(){

    }

    public Invite(String invite_content, String invite_date, String invite_sender_name, String invite_sender_email,String invite_status, String invite_require_time){
        this.invite_content = invite_content;
        this.invite_date = invite_date;
        this.invite_sender_name = invite_sender_name;
        this.invite_status  = invite_status;
        this.invite_sender_email = invite_sender_email;
        this.invite_require_time = invite_require_time;

    }

    public String getInvite_content() {
        return invite_content;
    }

    public String getInvite_date() {
        return invite_date;
    }

    public String getInvite_sender_name() {
        return invite_sender_name;
    }

    public String getInvite_status() {
        return invite_status;
    }

    public String getInvite_sender_email() {
        return invite_sender_email;
    }

    public String getInvite_require_time() {
        return invite_require_time;
    }

    public void setInvite_content(String invite_content) {
        this.invite_content = invite_content;
    }

    public void setInvite_date(String invite_date) {
        this.invite_date = invite_date;
    }

    public void setInvite_sender_name(String invite_sender_name) {
        this.invite_sender_name = invite_sender_name;
    }

    public void setInvite_status(String invite_status) {
        this.invite_status = invite_status;
    }

    public void setInvite_sender_email(String invite_sender_email) {
        this.invite_sender_email = invite_sender_email;
    }

    public void setInvite_require_time(String invite_require_time) {
        this.invite_require_time = invite_require_time;
    }
}

