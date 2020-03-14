package com.arabian.lancul.UI.Object;

public class Guider {
    public String bio;
    public String imageURL;
    public String name;
    public String rate;
    public String available;
    public String verified;
    public String[] languages;

    public Guider(String bio, String imageURL, String name, String rate, String available, String verified, String[] languages){
        this.bio = bio;
        this.imageURL = imageURL;
        this.name = name;
        this.rate = rate;
        this.available = available;
        this.verified = verified;
        this.languages = languages;
    }

    public String getName() {
        return name;
    }

    public String getAvailable() {
        return available;
    }

    public String getBio() {
        return bio;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getRate() {
        return rate;
    }

    public String getVerified() {
        return verified;
    }

    public String[] getLanguages() {
        return languages;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    public void setLanguages(String[] languages) {
        this.languages = languages;
    }


}
