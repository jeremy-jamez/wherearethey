package com.wherearethey;

/**
 * Created by romar on 22/4/2017.
 */

public class MissingPerson {

    public String full_name;
    public String complexion;
    public String heights;
    public String img_path;
    public int age;
    public String description;
    public String last_seen;
    public String vicinity;

    public MissingPerson(String fullname, String comp, String height, String img, int age_, String descrip,
                         String seen, String location){
        full_name = fullname;
        complexion = comp;
        heights = height;
        img_path = img;
        age = age_;
        description = descrip;
        last_seen = seen;
        vicinity = location;
    }

    public String getLast_seen() {
        return last_seen;
    }

    public void setLast_seen(String last_seen) {
        this.last_seen = last_seen;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getComplexion() {
        return complexion;
    }

    public void setComplexion(String complexion) {
        this.complexion = complexion;
    }

    public String getHeights() {
        return heights;
    }

    public void setHeights(String heights) {
        this.heights = heights;
    }

    public String getImg_path() {
        return img_path;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
