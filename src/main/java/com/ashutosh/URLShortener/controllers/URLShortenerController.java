package com.ashutosh.URLShortener.controllers;

import com.ashutosh.URLShortener.models.ItemBody;
import com.ashutosh.URLShortener.models.RequestToBeSent;
import com.ashutosh.URLShortener.models.ShortURL;
import com.ashutosh.URLShortener.services.URLShortenerServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.text.ParseException;
import java.util.NoSuchElementException;

@RestController
public class URLShortenerController {
    @Autowired
    private URLShortenerServiceImpl urlShortenerService;

    @GetMapping(path = "/{shortURL}")
    public ResponseEntity<String> redirectToLongURL(HttpServletResponse response, @PathVariable String shortURL) {
        try {
            String longURL = urlShortenerService.getLongURL(shortURL);
            response.sendRedirect(longURL);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No such URL exists!", e);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.toString());
        }
        return ResponseEntity.ok("");
    }

    @GetMapping(path = "/info/{shortURL}")
    public ResponseEntity<ShortURL> getShortURLInfo(HttpServletResponse response, @PathVariable String shortURL) {
        try {
            return ResponseEntity.ok(urlShortenerService.getShortURLFromList(shortURL));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No such URL exists!", e);
        }
    }

    @PostMapping(path = "/create", consumes = "application/json")
    public ResponseEntity<String> createShortURL(HttpServletRequest request, HttpServletResponse response,
                                  @RequestBody RequestToBeSent requestToBeSent) throws Exception {

        String currentHostName = request.getScheme() + "://" + request.getHeader("Host") + "/";

        String shortURL = urlShortenerService.createShortURL(requestToBeSent.longURL, requestToBeSent.expiryDate);
        if (shortURL.isEmpty()) {
//            throw new Exception("Short URL is empty");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Short URL is empty");
        } else {
            response.setStatus(200);
            return ResponseEntity.ok(currentHostName + shortURL);
        }
    }

    @PatchMapping(path = "/updateURL")
    public ResponseEntity<String> updateNewLongURLofShortURL(HttpServletResponse response,
                                           @RequestBody ShortURL body) throws IOException {

        boolean updateStatus = urlShortenerService.updateLongURL(body.getShortUrl(), body.getLongUrl());

        if (updateStatus) {
            response.setStatus(200);
            return ResponseEntity.ok("Update success");
        } else
//            throw new IOException("Something went wrong");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
    }

    @PatchMapping(path = "/updateExpiryDate")
    public ResponseEntity<String> updateExpiryOfShortURL(HttpServletResponse response,
                                          @RequestBody ItemBody body) throws IOException, ParseException {
        boolean updateStatus = urlShortenerService.updateExpiryDate(body.shortURL, body.daysToAdd);

        if (updateStatus) {
            response.setStatus(200);
            return ResponseEntity.ok("updated expiry successfully");
        } else
//            throw new IOException("Something went wrong");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
    }
}
