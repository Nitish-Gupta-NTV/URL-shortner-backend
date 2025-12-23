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

import java.time.LocalDate;
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
        String character="ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random=new Random(); // we can give the seed if we want
        StringBuilder shorturl=new StringBuilder(8);
        for (int i = 0; i <8; i++) {
            shorturl.append(character.charAt(random.nextInt(character.length())));
        }
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

    public Map<LocalDateTime, Long> gettotalclickbyuseranddate(User user, LocalDateTime start, LocalDateTime end) {
        List<UrlMapping> urlMappings=repo.findByUser(user);
        List<ClickEvent> clickevents=clickEventRepo.findByUrlMappingInAndLocalDateTimeBetween(urlMappings,start,end);
        return clickevents.stream().collect(Collectors.groupingBy(click->click.getLocalDateTime(),Collectors.counting()));
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
}
