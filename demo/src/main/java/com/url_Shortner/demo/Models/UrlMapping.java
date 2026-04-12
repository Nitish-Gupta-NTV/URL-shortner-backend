package com.url_Shortner.demo.Models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "Urlmapping")
public class UrlMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int clickcount =0;

    @Column(name = "org_url")
    private String orgUrl;

    @Column(name = "short_url")
    private String shortUrl;
    private LocalDateTime localDateTime;
    @ManyToOne
    @JoinColumn(name = "user_id") //owning side
    private User user;

    /*@OneToMany(mappedBy = "urlMapping")
    List<ClickEvent>clickEvents;*/
    // this is claud
    @OneToMany(mappedBy = "urlMapping", cascade = CascadeType.ALL, orphanRemoval = true)  // ✅ add this
            List<ClickEvent> clickEvents;


}
