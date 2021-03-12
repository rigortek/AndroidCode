package com.cw.updateuifromchildthread.grideDemo;

/**
 * Create by robin On 21-3-11
 */
public class Book {

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getImageResource() {
        return imageResource;
    }

    public int getImageViewFavorite() {
        return imageViewFavorite;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public Book setImageResource(int imageResource) {
        this.imageResource = imageResource;
        return this;
    }

    public Book setImageViewFavorite(int imageViewFavorite) {
        this.imageViewFavorite = imageViewFavorite;
        return this;
    }

    public Book setName(String name) {
        this.name = name;
        return this;
    }

    public Book setAuthor(String author) {
        this.author = author;
        return this;
    }

    private String imageUrl;
    private int imageResource;
    private int imageViewFavorite;
    private String name;
    private String author;
}
