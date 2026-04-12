package com.url_Shortner.demo.Controller;

import com.url_Shortner.demo.Dtos.ClickEventDto;
import com.url_Shortner.demo.Dtos.UrlMappingDto;
import com.url_Shortner.demo.Models.User;
import com.url_Shortner.demo.Service.UrlMapppingService;
import com.url_Shortner.demo.Service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/urls")
@AllArgsConstructor
public class UrlmappingController {
    private UrlMapppingService urlmapping;
    private UserService service;
    @PostMapping("/shorten")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UrlMappingDto> creteshorturl(@RequestBody Map<String ,String> request, Principal principal)
    {
        //{"longurl":"shorturl"}
        String originalurl=request.get("originalurl");
     User user= service.findByUsername(principal.getName());
           UrlMappingDto urldto= urlmapping.creteShortUrl(originalurl,user);
           return ResponseEntity.ok(urldto);

    }
    @GetMapping("/myurl")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<UrlMappingDto>> fetchallur(Principal principle)
    {
        User user=service.findByUsername(principle.getName());
        List<UrlMappingDto>url=urlmapping.getUrlByUser(user);
        return ResponseEntity.ok(url);

    }
    @GetMapping("/analytics/{shortUrl}")
    @PreAuthorize("hasRole('USER')")
   /* public ResponseEntity<List<ClickEventDto>> analyiscsrespnce(@PathVariable String shortUrl,
                                                                @RequestParam("startDate") String startdate,
                                                                @RequestParam("lastdate") String lastdate)
    {
        DateTimeFormatter formatter=DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime start=LocalDateTime.parse(startdate,formatter);
        LocalDateTime end=LocalDateTime.parse(lastdate,formatter);
       List<ClickEventDto>clickEventDtos= urlmapping.getclickeventbydate(shortUrl,start,end);
       return ResponseEntity.ok(clickEventDtos);
    }*/

    public ResponseEntity<List<ClickEventDto>> analyiscsrespnce(@PathVariable String shortUrl,
                                                                @RequestParam("startDate") String startdate,
                                                                @RequestParam("lastdate") String lastdate)
    {

        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("yyyy-MM-dd'T'HH:mm")
                .appendPattern(":ss")
                .optionalStart()
                .appendPattern(":ss")
                .optionalEnd()
                .toFormatter();

        LocalDateTime start = LocalDateTime.parse(startdate, formatter);
        LocalDateTime end = LocalDateTime.parse(lastdate, formatter);
        List<ClickEventDto> clickEventDtos = urlmapping.getclickeventbydate(shortUrl, start, end);
        return ResponseEntity.ok(clickEventDtos);
    }
    @PostMapping("/totalclick")
    @PreAuthorize("hasRole('USER')")
   /* public ResponseEntity<Map<LocalDateTime,Long>> totalclicking(Principal principal,
                                                             @RequestParam("startDate") String startdate,
                                                             @RequestParam("lastdate") String lastdate)
    {
        DateTimeFormatter formatter=DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        User user=service.findByUsername(principal.getName());
        LocalDateTime start=LocalDateTime.parse(startdate,formatter);
        LocalDateTime end=LocalDateTime.parse(lastdate,formatter);
         Map<LocalDateTime,Long> totalclick =urlmapping.gettotalclickbyuseranddate(user,start,end);
         return ResponseEntity.ok(totalclick);



    }*/
    public ResponseEntity<Map<LocalDate, Long>> totalclicking(Principal principal,
                                                              @RequestParam("startDate") String startdate,
                                                              @RequestParam("lastdate") String lastdate)
    {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("yyyy-MM-dd'T'HH:mm")
                .appendPattern(":ss")
                .optionalStart()
                .appendPattern(":ss")
                .optionalEnd()
                .toFormatter();
        User user = service.findByUsername(principal.getName());
        LocalDateTime start = LocalDateTime.parse(startdate, formatter);
        LocalDateTime end = LocalDateTime.parse(lastdate, formatter);
        Map<LocalDate, Long> totalclick = urlmapping.gettotalclickbyuseranddate(user, start, end); // ✅ LocalDate return
        return ResponseEntity.ok(totalclick);
    }
    @DeleteMapping("/{shortUrl}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteUrl(@PathVariable String shortUrl, Principal principal) {
        User user = service.findByUsername(principal.getName());
        urlmapping.deleteUrl(shortUrl, user);
        return ResponseEntity.ok(Map.of("message", "URL deleted successfully"));
    }

}
