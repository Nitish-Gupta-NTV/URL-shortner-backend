package com.url_Shortner.demo.Dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UrlMappingDto {
    private Long id;
    private String original;
    private String shorturl;
    private int clickcount;
    private LocalDateTime cretedate;
    private String username;

}
