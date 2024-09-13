package com.lawnotes.lawadminapp1.Models;

public class ChapterModel {

    private String chapterName,pdfUrl,key;

    public ChapterModel(String chapterName, String logo, String pdfUrl) {
        this.chapterName = chapterName;
        this.pdfUrl = pdfUrl;
    }

    public ChapterModel() {
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }


    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
