## Short-It
<br />
<br />
This is an application designed to shorten URLs, similar to Tinuurl/Bitly, created using Java and SpringBoot. 
Url Shortener is a service that converts long URLs into short aliases to save space when sharing URLs in messages, twitter, presentations etc. When a user opens short URL, he/she will be automatically redirected to original (long) URL.
<br />
<br />
### How to use
<br />
<br />
```sh
$ git clone https://github.com/ashutosh-kumar2/Short-It.git
```

  - Open the project in an editor like IntelliJ IDEA.
  - Build Spring project.
  - The inbuilt Tomcat server will start at http://localhost:8080.
  - Use Postman to hit the endpoint http://localhost:8080/create to shorten a URL (pass the original URL and its expiry date as a JSON in the request body).
  - Use the browser to access endpoint http://localhost:8080/info/{shortURL} where shortURL is the shortened link you just created, you will be redirected to the original link.
  - You can modify the original URL OR the expiry date for a short link that you have already created.
  - Use Postman to hit the endpoint http://localhost:8080/updateURL to update the original URL (pass the shortened URL and the original URL as a JSON in the request body).
  - You can use the browser to access endpoint http://localhost:8080/info/{shortURL}, you will be redirected to the modified link now.
  - Use Postman to hit the endpoint http://localhost:8080/updateExpiryDate to update the expiry date of the shortened URL (pass the shortened URL and the number of days you want to add to the expiry date as a JSON in the request body).
  - You can use Postman to hit the endpoint http://localhost:8080/info/{shortURL}, where you will be able to see that the expiry date of the shortened URL has been updated.
    `
<br />
<br />
