package com.example.instagramclone.Model;

public class User {

    private String Id;
    private String UserName;
    private String FullName;
    private String Email;
    private String ImageUrl;

    public User(String Id, String UserName, String FullName, String Email, String ImageUrl) {
        this.Id = Id;
        this.UserName = UserName;
        this.FullName = FullName;
        this.Email = Email;
        this.ImageUrl = ImageUrl;

    }

    public User() {
    }

    public String getId() {
        return Id;
    }

    public void setId(String Id) {
        this.Id = Id;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }




}
