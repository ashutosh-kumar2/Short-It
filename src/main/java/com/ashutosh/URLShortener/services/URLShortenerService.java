package com.ashutosh.URLShortener.services;

import com.ashutosh.URLShortener.models.ShortURL;

import java.text.ParseException;

public interface URLShortenerService {
    public ShortURL getShortURLFromList(String shortURL) throws Exception;
    public String getLongURL(String shortURL) throws Exception;
    public String createShortURL(String longURL, String expiry) throws Exception;
    public boolean updateLongURL(String shortURL, String newLongURL);
    public boolean updateExpiryDate(String shortURL, int daysToExpire) throws ParseException;
}
