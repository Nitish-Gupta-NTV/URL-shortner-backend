package com.url_Shortner.demo.Service;

import com.url_Shortner.demo.Dtos.ClickEventDto;
import com.url_Shortner.demo.Dtos.UrlMappingDto;
import com.url_Shortner.demo.Models.ClickEvent;
import com.url_Shortner.demo.Models.UrlMapping;
import com.url_Shortner.demo.Models.User;
import com.url_Shortner.demo.Repository.ClickEventRepo;
import com.url_Shortner.demo.Repository.UrlMappingRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.time.LocalDate;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UrlMapppingService {
    private UrlMappingRepo repo;
    private ClickEventRepo clickEventRepo;

    public UrlMappingDto creteShortUrl(String originalurl, User user) {
        validateUrl(originalurl);
        String short_url=generateurl();
        UrlMapping urlMapping=new UrlMapping();
        urlMapping.setOrgUrl(originalurl);
        urlMapping.setUser(user);
        urlMapping.setShortUrl(short_url);
        urlMapping.setLocalDateTime(LocalDateTime.now());
        UrlMapping  savedurl= repo.save(urlMapping);
        return convertTOUrlImpl(savedurl);
    }
    public UrlMappingDto convertTOUrlImpl(UrlMapping urlmapping)
    {
        UrlMappingDto urlMappingDto=new UrlMappingDto();
        urlMappingDto.setId(urlmapping.getId());
        urlMappingDto.setCretedate(urlmapping.getLocalDateTime());
        urlMappingDto.setUsername(urlmapping.getUser().getUserName());
        urlMappingDto.setOriginal(urlmapping.getOrgUrl());
        urlMappingDto.setShorturl(urlmapping.getShortUrl());
       urlMappingDto.setClickcount(urlmapping.getClickcount());
        return urlMappingDto;
    }

    private String generateurl() {
        String character="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890+-/*";
        Random random=new Random(); // we can give the seed if we want
        StringBuilder shorturl=new StringBuilder(8);
        for (int i = 0; i <8; i++) {
            shorturl.append(character.charAt(random.nextInt(character.length())));
        }
        System.out.println("your short url which is valid "+shorturl);
        return shorturl.toString();
    }

    public List<UrlMappingDto> getUrlByUser(User user) {
        return repo.findByUser(user).stream()
                .map(this::convertTOUrlImpl)
                .toList();
    }

    public List<ClickEventDto> getclickeventbydate(String shortUrl, LocalDateTime start,
                                                   LocalDateTime end){

        UrlMapping urlMapping=repo.findByShortUrl(shortUrl);
        if (urlMapping!=null){
            return clickEventRepo.findByUrlMappingAndLocalDateTimeBetween(urlMapping,start,end).stream()
                    .collect(Collectors.groupingBy(click->click.getLocalDateTime().toLocalDate(),Collectors.counting())).entrySet().stream()
                    .map(entry->
                    {
                        ClickEventDto clickEventDto=new ClickEventDto();
                        clickEventDto.setClickdate(entry.getKey());
                        clickEventDto.setCount(entry.getValue());
                        return clickEventDto;
                    })
                            .collect(Collectors.toList());
    }
        return null;
    }

   /* public Map<LocalDateTime, Long> gettotalclickbyuseranddate(User user, LocalDateTime start, LocalDateTime end) {
        List<UrlMapping> urlMappings=repo.findByUser(user);
        List<ClickEvent> clickevents=clickEventRepo.findByUrlMappingInAndLocalDateTimeBetween(urlMappings,start,end);
         return clickevents.stream().collect(Collectors.groupingBy(click->click.getLocalDateTime(),Collectors.counting()));

    }*/
   // ✅ params are LocalDateTime, return type is LocalDate
   public Map<LocalDate, Long> gettotalclickbyuseranddate(User user, LocalDateTime start, LocalDateTime end) {
       List<UrlMapping> urlMappings = repo.findByUser(user);
       System.out.println("Logged in user ID: " + user.getId());
       System.out.println("Total URL mappings found: " + urlMappings.size());
       List<ClickEvent> clickevents = clickEventRepo.findByUrlMappingInAndLocalDateTimeBetween(urlMappings, start, end);
       System.out.println("Total click events found: " + clickevents.size()); // ← debug
       System.out.println("Start date: " + start); // ← debug
       System.out.println("End date: " + end);
       return clickevents.stream()
               .collect(Collectors.groupingBy(
                       click -> click.getLocalDateTime().toLocalDate(),
                       Collectors.counting()
               ));
   }

    public UrlMapping getoriginalurl(String shorturl) {
        UrlMapping urlMapping=repo.findByShortUrl(shorturl);
        if (urlMapping!=null)
        {
            urlMapping.setClickcount(urlMapping.getClickcount()+1);
            repo.save(urlMapping);
            // maintaing the record
            ClickEvent clickEvent=new ClickEvent();
            clickEvent.setLocalDateTime(LocalDateTime.now());
            clickEvent.setUrlMapping(urlMapping);
            clickEventRepo.save(clickEvent);
        }
        return urlMapping;
    }
    // this is for the validation of the url
    private void validateUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            throw new RuntimeException("URL cannot be empty");
        }
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            throw new RuntimeException("URL must start with http:// or https://");
        }
        try {
            HttpURLConnection connection = (HttpURLConnection) URI.create(url)
                    .toURL()
                    .openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);
            connection.setInstanceFollowRedirects(true);  // ✅ follow redirects
            int responseCode = connection.getResponseCode();
            connection.disconnect();
            if (responseCode < 200 || responseCode >= 400) {
                throw new RuntimeException("URL is not reachable");
            }
        } catch (IOException e) {
            throw new RuntimeException("URL is not valid or not reachable: " + e.getMessage());
        }
    }
    public void deleteUrl(String shortUrl, User user) {
        UrlMapping url = repo.findByShortUrl(shortUrl);
        if (url == null) {
            throw new RuntimeException("URL not found");
        }
        if (!url.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }
        repo.delete(url);  // ✅ cascades to clickEvents automatically
    }
}
