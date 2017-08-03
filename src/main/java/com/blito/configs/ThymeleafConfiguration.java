package com.blito.configs;

import org.apache.commons.lang3.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

@Configuration
public class ThymeleafConfiguration {

    @SuppressWarnings("unused")
    private final Logger log = LoggerFactory.getLogger(ThymeleafConfiguration.class);

//    @Bean
//    @Description("Thymeleaf template resolver serving HTML 5 emails")
//    public ClassLoaderTemplateResolver emailTemplateResolver() {
//        ClassLoaderTemplateResolver emailTemplateResolver = new ClassLoaderTemplateResolver();
//        emailTemplateResolver.setPrefix("templates/");
//        emailTemplateResolver.setSuffix(".html");
//        emailTemplateResolver.setTemplateMode("XHTML"); //added
//        emailTemplateResolver.setCharacterEncoding(CharEncoding.UTF_8);
//        emailTemplateResolver.setOrder(1);
//        return emailTemplateResolver;
//    }
    
    @Bean
    public ThymeleafViewResolver viewResolver(){
        final ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        return viewResolver;
    }
    
    @Bean
    public SpringTemplateEngine templateEngine()
    {
    	final SpringTemplateEngine engine = new SpringTemplateEngine();
    	engine.addTemplateResolver(mailTemplateResolver());
    	engine.addTemplateResolver(staticTemplateResolver());
    	return engine;
    }
    
    private ITemplateResolver mailTemplateResolver() {
        final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setOrder(Integer.valueOf(1));
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML5");
        templateResolver.setCharacterEncoding(CharEncoding.UTF_8);
        templateResolver.setCacheable(false);
        return templateResolver;
    }
    
    private ITemplateResolver staticTemplateResolver() {
        final ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setOrder(Integer.valueOf(1));
        templateResolver.setPrefix("static/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML5");
        templateResolver.setCharacterEncoding(CharEncoding.UTF_8);
        templateResolver.setCacheable(false);
        return templateResolver;
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

