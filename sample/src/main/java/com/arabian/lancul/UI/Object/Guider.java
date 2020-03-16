package com.arabian.lancul.UI.Object;

import java.util.List;

public class Guider {
    public String bio;
    public String imageURL;
    public String name;
    public Float rate;
    public boolean available;
    public boolean verified;
    public List<String> languages;
    public String phone;
    public String email;

    public Guider(String bio, String imageURL, String name, Float rate, boolean available, boolean verified, List<String> languages,String phone,String  email){
        this.bio = bio;
        this.imageURL = imageURL;
        this.name = name;
        this.rate = rate;
        this.available = available;
        this.verified = verified;
        this.languages = languages;
        this.phone = phone;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public boolean getAvailable() {
        return available;
    }

    public String getBio() {
        return bio;
    }

    public String getImageURL() {
        return imageURL;
    }

    public Float getRate() {
        return rate;
    }

    public boolean getVerified() {
        return verified;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
