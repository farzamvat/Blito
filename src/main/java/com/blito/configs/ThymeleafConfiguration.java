package com.blito.configs;

import org.apache.commons.lang3.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Configuration
public class ThymeleafConfiguration {

    @SuppressWarnings("unused")
    private final Logger log = LoggerFactory.getLogger(ThymeleafConfiguration.class);

    @Bean
    @Description("Thymeleaf template resolver serving HTML 5 emails")
    public ClassLoaderTemplateResolver emailTemplateResolver() {
        ClassLoaderTemplateResolver emailTemplateResolver = new ClassLoaderTemplateResolver();
        emailTemplateResolver.setPrefix("static/");
        emailTemplateResolver.setSuffix(".html");
        emailTemplateResolver.setCharacterEncoding(CharEncoding.UTF_8);
        emailTemplateResolver.setOrder(1);
        return emailTemplateResolver;
    }
    
   
//    @Bean
//    @Description("Thymeleaf template resolver serving HTML 5 emails")
//    public ViewResolver purchaseRequestTemplateResolver() {
//        ClassLoaderTemplateResolver purchaseRequest = new ClassLoaderTemplateResolver();
//        purchaseRequest.setPrefix("templates/");
//        purchaseRequest.setSuffix(".html");
//        purchaseRequest.setTemplateMode("LEGACYHTML5");
//        purchaseRequest.setCacheable(false);
//        purchaseRequest.setCharacterEncoding(CharEncoding.UTF_8);
//        purchaseRequest.setOrder(1);
//        SpringTemplateEngine engine = new SpringTemplateEngine();
//        engine.setTemplateResolver(purchaseRequest);
//        
//        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
//        viewResolver.setTemplateEngine(engine);
//        return viewResolver;
//    }
}

