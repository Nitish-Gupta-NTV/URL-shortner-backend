package com.url_Shortner.demo.Repository;

import com.url_Shortner.demo.Models.ClickEvent;
import com.url_Shortner.demo.Models.UrlMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ClickEventRepo extends JpaRepository<ClickEvent, Long> {

    List<ClickEvent> findByUrlMappingAndLocalDateTimeBetween(
            UrlMapping urlMapping,
            LocalDateTime start,
            LocalDateTime end
    );

    List<ClickEvent> findByUrlMappingInAndLocalDateTimeBetween(
            List<UrlMapping> urlMappings,
            LocalDateTime start,
            LocalDateTime end
    );
}
