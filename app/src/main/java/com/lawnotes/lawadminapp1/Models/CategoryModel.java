package com.lawnotes.lawadminapp1.Models;

public class CategoryModel {

    private String categoryName,categoryLogo,key;

    public CategoryModel(String categoryName, String categoryLogo) {
        this.categoryName = categoryName;
        this.categoryLogo = categoryLogo;
    }

    public CategoryModel() {
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryLogo() {
        return categoryLogo;
    }

    public void setCategoryLogo(String categoryLogo) {
        this.categoryLogo = categoryLogo;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}

