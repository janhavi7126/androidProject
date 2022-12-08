package com.example.android.mymall.Activities;

public class SliderModel {
    private String banner;
    private String bannerBack;

    public SliderModel(String banner, String bannerBack) {
        this.banner = banner;
        this.bannerBack = bannerBack;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getBannerBack() {
        return bannerBack;
    }

    public void setBannerBack(String bannerBack) {
        this.bannerBack = bannerBack;
    }
}
