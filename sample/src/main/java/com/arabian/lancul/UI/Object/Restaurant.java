package com.arabian.lancul.UI.Object;

public class Restaurant {
    String name;
    String location;
    Float rating;
    String photo;

    public Restaurant(String name, String location, String photo, Float rating){
        this.name = name;
        this.location = location;
        this.photo = photo;
        this.rating = rating;
    }

    public Float getRating() {
        return rating;
    }

    public String getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }
}
