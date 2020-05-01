package com.arabian.lancul.UI.Object;

import java.util.ArrayList;
import java.util.List;

public class Client {
    public String name;
    public String status;
    public String email;
    public String photo;
    public List<String> linked_guiders;
    public List<Double> location;
    public Client(String name, String email, String status,String photo, List<String> linked_guiders, List<Double> location) {
        this.status = status;
        this.name = name;
        this.email = email;
        this.photo = photo;
        this.linked_guiders = linked_guiders;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getStatus() {
        return status;
    }

    public String getPhoto() {
        return photo;
    }

    public List<String> getLinked_guiders() {
        return linked_guiders;
    }


    public List<Double> getLocation() {
        return location;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setLinked_guiders(List<String> linked_guiders) {
        this.linked_guiders = linked_guiders;
    }

    public void setLocation(List<Double> location) {
        this.location = location;
    }

}
