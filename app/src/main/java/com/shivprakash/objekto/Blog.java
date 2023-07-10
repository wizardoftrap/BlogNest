package com.shivprakash.objekto;

public class Blog {
    String title;
    String author;
    String shortDescription;
    String longDescription;
    String Date;
    public Blog(String Title, String shortDescription, String longDescription,String date,String author){
        this.shortDescription=shortDescription;
        this.longDescription=longDescription;
        this.title=Title;
        this.author=author;
        this.Date=date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}
