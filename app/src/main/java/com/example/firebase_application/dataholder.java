package com.example.firebase_application;

public class dataholder {
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public dataholder(String course, String duration, String name, String img) {
        this.name = name;
        this.course = course;
        this.duration = duration;
        this.img=img;
    }

    String course;
    String duration;
    String img;

}
