package com.example.android.mymall.Activities;

public class CategoryModel {

    private String categoryIconLink;
    private String categoryNames;

    public CategoryModel(String categoryIconLink, String categoryNames) {
        this.categoryIconLink = categoryIconLink;
        this.categoryNames = categoryNames;
    }

    public String getCategoryIconLink() {
        return categoryIconLink;
    }

    public void setCategoryIconLink(String categoryIconLink) {
        this.categoryIconLink = categoryIconLink;
    }

    public String getCategoryNames() {
        return categoryNames;
    }

    public void setCategoryNames(String categoryNames) {
        this.categoryNames = categoryNames;
    }
}
