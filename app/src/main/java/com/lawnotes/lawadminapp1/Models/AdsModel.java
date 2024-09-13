package com.lawnotes.lawadminapp1.Models;

public class AdsModel {

    private String status;

    private String appId,bannerAds,interstitialAds;

    public AdsModel() {
    }

    public AdsModel(String status) {
        this.status = status;
    }

    public AdsModel(String appId, String bannerAds, String interstitialAds) {
        this.appId = appId;
        this.bannerAds = bannerAds;
        this.interstitialAds = interstitialAds;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getBannerAds() {
        return bannerAds;
    }

    public void setBannerAds(String bannerAds) {
        this.bannerAds = bannerAds;
    }

    public String getInterstitialAds() {
        return interstitialAds;
    }

    public void setInterstitialAds(String interstitialAds) {
        this.interstitialAds = interstitialAds;
    }
}
