package com.ashutosh.URLShortener.models;

import com.opencsv.bean.CsvBindByName;

public class ShortURL {
    @CsvBindByName(column = "longURL")
    private String longUrl;
    @CsvBindByName(column = "shortURL")
    private String shortUrl;
    @CsvBindByName(column = "expiryDate")
    private String expiryDate;
    public ShortURL() {
    }
    public ShortURL(String longUrl, String shortUrl, String expiryDate) {
        this.longUrl = longUrl;
        this.shortUrl = shortUrl;
        this.expiryDate = expiryDate;
    }
    public String getLongUrl() {
        return longUrl;
    }
    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }
    public String getShortUrl() {
        return shortUrl;
    }
    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }
    public String getExpiryDate() {
        return expiryDate;
    }
    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
}
