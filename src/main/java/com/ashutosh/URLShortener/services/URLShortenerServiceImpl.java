package com.ashutosh.URLShortener.services;

import com.ashutosh.URLShortener.models.ShortURL;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
@Component
public class URLShortenerServiceImpl implements URLShortenerService {
    List<ShortURL> allURLs;
    @Value("${csvFilePath:DefaultCSV.csv}")
    private String dbPath;
    @Override
    public ShortURL getShortURLFromList(String shortURL) throws Exception {
        Optional<ShortURL> shortURLResultSet = this.allURLs.stream().filter(u -> Objects.equals(u.getShortUrl(), shortURL)).findFirst();
        if (shortURLResultSet.isPresent())
            return shortURLResultSet.get();
        throw new Exception("URL not found");
    }
    @Override
    public String getLongURL(String shortURL) throws Exception {
        Date currentDate = new Date();
        Optional<ShortURL> result = this.allURLs.stream().filter(u -> Objects.equals(u.getShortUrl(), shortURL)).findFirst();
        if (result.isEmpty())
            throw new NoSuchElementException();
        String expiryDate = result.get().getExpiryDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        Date convertedExpiryDate = dateFormat.parse(expiryDate);
        if (currentDate.after(convertedExpiryDate))
            throw new Exception("URL has expired, please create a new short URL!");
        return result.get().getLongUrl();
    }

    @Override
    public String createShortURL(String longURL, String expiry) throws Exception {
        String shortUrl = givenUsingApache_whenGeneratingRandomStringBounded_thenCorrect();
        addNewURLToCSV(new ShortURL(longURL, shortUrl, expiry));
        return shortUrl;
    }

    @Override
    public boolean updateLongURL(String shortURL, String newLongURL) {
        AtomicBoolean updateSuccess = new AtomicBoolean(false);

        this.allURLs.forEach(u -> {
            if (Objects.equals(u.getShortUrl(), shortURL)) {
                u.setLongUrl(newLongURL);

                updateSuccess.set(true);
            }
        });

        writeToCSVFile(this.allURLs);

        return updateSuccess.get();
    }

    @Override
    public boolean updateExpiryDate(String shortURL, int daysToExpire) throws ParseException {
        Optional<ShortURL> result = this.allURLs.stream()
                .filter(u -> Objects.equals(u.getShortUrl(), shortURL))
                .findFirst();

        if (result.isEmpty())
            throw new NoSuchElementException();

        String expiryDate = result.get().getExpiryDate();

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");

        Date convertedExpiryDate = dateFormat.parse(expiryDate);

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(convertedExpiryDate);

        calendar.add(Calendar.DAY_OF_MONTH, daysToExpire);

        Date newDate = calendar.getTime();

        updateDate(shortURL, dateFormat.format(newDate));

        writeToCSVFile(this.allURLs);

        return true;
    }
    private void updateDate(String shortURL, String date) {
        this.allURLs.forEach(u -> {
            if (Objects.equals(u.getShortUrl(), shortURL)) {
                u.setExpiryDate(date);
            }
        });
    }
    public String givenUsingApache_whenGeneratingRandomStringBounded_thenCorrect() {
        int length = 10;
        boolean useLetters = true;
        boolean useNumbers = false;
        return RandomStringUtils.random(length, useLetters, useNumbers);
    }
    public void addNewURLToCSV(ShortURL newUrl) throws Exception {
        // Read the existing data from CSV file
        List<ShortURL> existingData = readFromCSVFile();

        // Add the new URL to the existing data
        existingData.add(newUrl);

        this.allURLs = existingData;

        // Write the updated data back to the CSV file
        writeToCSVFile(existingData);
    }
    private List<ShortURL> readFromCSVFile() throws Exception {
        List<ShortURL> allUrls = new ArrayList<>();

        try {
            Resource resource = new ClassPathResource(dbPath);

            File databaseFile = resource.getFile();

            FileReader reader = new FileReader(databaseFile);

            CsvToBean<ShortURL> csvToBean = new CsvToBeanBuilder<ShortURL>(reader)
                    .withType(ShortURL.class)
                    .build();

            allUrls = csvToBean.parse();

            reader.close();

        } catch (Exception e) {
            System.out.println(e);
        }

        return allUrls;
    }
    private void writeToCSVFile(List<ShortURL> newData) {
        try {
            Resource resource = new ClassPathResource(dbPath);

            File databaseFile = resource.getFile();

            FileWriter writer = new FileWriter(databaseFile);

            StatefulBeanToCsv<ShortURL> csvToBean = new StatefulBeanToCsvBuilder<ShortURL>(writer)
                    .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .withOrderedResults(false)
                    .build();

            csvToBean.write(newData);

            writer.close();

        } catch (Exception e) {
            System.out.println("write to CSV" + e);
        }
    }
}
