package com.url_Shortner.demo.Controller;

import com.url_Shortner.demo.Models.UrlMapping;
import com.url_Shortner.demo.Service.UrlMapppingService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class RedirectController {
    private UrlMapppingService urlMapppingService;

    @GetMapping("/{shorturl}")
    public ResponseEntity<Void> redirect(@PathVariable String shorturl)
    {
        UrlMapping urlMapping=urlMapppingService.getoriginalurl(shorturl);
        if(urlMapping!=null)
        {
            HttpHeaders httpHeaders=new HttpHeaders();
            httpHeaders.add("Location",urlMapping.getOrgUrl());
            return ResponseEntity.status(302).headers(httpHeaders).build();
        }
        else
        {
            ResponseEntity.notFound().build();
        }
        return ResponseEntity.notFound().build();
    }

}
