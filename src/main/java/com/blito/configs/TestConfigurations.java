package com.blito.configs;

import com.blito.enums.ImageType;
import com.blito.models.Image;
import com.blito.repositories.ImageRepository;
import com.blito.services.ImageService;
import com.blito.services.MailService;
import com.blito.services.PaymentRequestServiceAsync;
import com.blito.services.SmsService;
import io.vavr.control.Try;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailSendException;

import javax.annotation.PostConstruct;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Matchers.*;

/*
    @author Farzam Vatanzadeh
*/
@Profile("test")
@Configuration
public class TestConfigurations {

    @Autowired
    private ImageRepository imageRepository;

    @PostConstruct
    public void init()
    {
        Image image = new Image();
        image.setImageType(ImageType.EVENT_PHOTO.name());
        image.setImageUUID(Constants.DEFAULT_EVENT_PHOTO);

        Image hostCoverPhoto = new Image();
        hostCoverPhoto.setImageType(ImageType.HOST_COVER_PHOTO.name());
        hostCoverPhoto.setImageUUID(Constants.DEFAULT_HOST_COVER_PHOTO_1);

        Image eventHostDefaultPhoto = new Image();
        eventHostDefaultPhoto.setImageType(ImageType.HOST_PHOTO.name());
        eventHostDefaultPhoto.setImageUUID(Constants.DEFAULT_HOST_PHOTO);

        Image exchangeBlit = new Image();
        exchangeBlit.setImageUUID(Constants.DEFAULT_EXCHANGEBLIT_PHOTO);
        exchangeBlit.setImageType(ImageType.EXCHANGEBLIT_PHOTO.name());

        imageRepository.save(image);
        imageRepository.save(hostCoverPhoto);
        imageRepository.save(eventHostDefaultPhoto);
        imageRepository.save(exchangeBlit);
    }

    @Bean
    @Primary
    public ImageService imageServiceSpy() {
        ImageService imageService = Mockito.spy(ImageService.class);
        Mockito.when(imageService.getFileEncodedBase64(Constants.DEFAULT_HOST_PHOTO)).thenReturn(Try.success("sampleHostPhoto"));
        Mockito.when(imageService.getFileEncodedBase64(Constants.DEFAULT_EXCHANGEBLIT_PHOTO)).thenReturn(Try.failure(new RuntimeException("")));
        return imageService;
    }

    @Bean
    @Primary
    public PaymentRequestServiceAsync paymentRequestServiceAsyncSpy()
    {
        PaymentRequestServiceAsync paymentRequestServiceAsync = Mockito.spy(PaymentRequestServiceAsync.class);
        Mockito.when(paymentRequestServiceAsync.zarinpalRequestToken(anyInt(),anyString(),anyString(),anyString())).thenReturn(CompletableFuture.completedFuture("testToken"));
        return  paymentRequestServiceAsync;
    }

    @Bean
    @Primary
    public MailService mailServiceSpy()
    {
        MailService mailService = Mockito.spy(MailService.class);
        Mockito.doThrow(new MailSendException("failed")).when(mailService).sendEmail(anyString(),anyString(),anyString());
        Mockito.doNothing().when(mailService).sendActivationEmail(anyObject());
        return mailService;
    }

    @Bean
    @Primary
    public SmsService smsService()
    {
        SmsService smsService = Mockito.mock(SmsService.class);
        Mockito.doNothing().when(smsService).sendBlitRecieptSms(anyString(),anyString());
        return smsService;
    }
}
