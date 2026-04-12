package com.url_Shortner.demo.Repository;

import com.url_Shortner.demo.Models.UrlMapping;
import com.url_Shortner.demo.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;
@Repository
@EnableJpaRepositories
public interface UrlMappingRepo extends JpaRepository<UrlMapping,Long> {
    UrlMapping findByShortUrl (String shorturl);
    List<UrlMapping> findByUser(User user);

}
