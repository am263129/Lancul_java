package com.arabian.lancul.UI.Object;

public class feedback {
    private String client;
    private String feedback;
    private Float rate;
    private String leave_feed_date;

    public feedback(){}

    public feedback(String client, String feedback, Float rate, String leave_feed_date){
        this.client = client;
        this.feedback = feedback;
        this.rate = rate;
        this.leave_feed_date = leave_feed_date;
    }

    public Float getRate() {
        return rate;
    }

    public String getClient() {
        return client;
    }

    public String getFeedback() {
        return feedback;
    }

    public String getLeave_feed_date() {
        return leave_feed_date;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public void setLeave_feed_date(String leave_feed_date) {
        this.leave_feed_date = leave_feed_date;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }
}
