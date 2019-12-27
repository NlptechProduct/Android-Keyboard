package com.nlptech.function.theme.download_theme;

public class DownloadThemeApiItem {


    /**
     * theme_id : ios123
     * theme_name : iOS
     * theme_url : ssssss
     * theme_cover : vvvvvv
     * theme_cover_with_border : aaaaaa
     * mode : 0
     * md5 : ew5r4ew6r4ew8rwe4r86e
     * size : 100000
     * version : 2
     */

    private String theme_id;
    private String theme_name;
    private String theme_url;
    private String theme_cover;
    private String theme_cover_with_border;
    private String mode;
    private String md5;
    private int size;
    private int version;

    public String getTheme_id() {
        return theme_id;
    }

    public void setTheme_id(String theme_id) {
        this.theme_id = theme_id;
    }

    public String getTheme_name() {
        return theme_name;
    }

    public void setTheme_name(String theme_name) {
        this.theme_name = theme_name;
    }

    public String getTheme_url() {
        return theme_url;
    }

    public void setTheme_url(String theme_url) {
        this.theme_url = theme_url;
    }

    public String getTheme_cover() {
        return theme_cover;
    }

    public void setTheme_cover(String theme_cover) {
        this.theme_cover = theme_cover;
    }

    public String getTheme_cover_with_border() {
        return theme_cover_with_border;
    }

    public void setTheme_cover_with_border(String theme_cover_with_border) {
        this.theme_cover_with_border = theme_cover_with_border;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
