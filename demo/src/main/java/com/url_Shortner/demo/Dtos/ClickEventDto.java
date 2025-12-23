package com.url_Shortner.demo.Dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ClickEventDto {
    private LocalDate clickdate;
    private Long count;

}
