package com.example.instagramclone.Model;

public class Posts {

    private String PostId;
    private String PostedImage;
    private String Description;
    private String Publisher;

    public Posts(String posId, String postedImage, String description, String publisher) {
        PostId = posId;
        PostedImage = postedImage;
        Description = description;
        Publisher = publisher;
    }

    public Posts() {
    }

    public String getPostId() {
        return PostId;
    }

    public void setPostId(String postId) {
        PostId = postId;
    }

    public String getPostedImage() {
        return PostedImage;
    }

    public void setPostedImage(String postedImage) {
        PostedImage = postedImage;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getPublisher() {
        return Publisher;
    }

    public void setPublisher(String publisher) {
        Publisher = publisher;
    }
}
