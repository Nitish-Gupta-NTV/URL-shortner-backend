package com.url_Shortner.demo.Models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "clickcount")
public class ClickEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime localDateTime;
    @ManyToOne
    @JoinColumn(name="urlmappingid")
    private UrlMapping urlMapping;

}
