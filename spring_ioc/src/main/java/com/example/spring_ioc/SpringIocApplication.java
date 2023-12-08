package com.example.spring_ioc;

import com.fasterxml.jackson.databind.ser.Serializers;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
public class SpringIocApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringIocApplication.class, args);

        ApplicationContext context = ApplicationContextProvider.getContext();

//        Base64Encoder base64Encoder = context.getBean(Base64Encoder.class);
//        Encoder encoder = new Encoder(base64Encoder);
//
//        String url = "www.naver.com/books/it?page=10&size=20&name=spring-boot";
//        String result = encoder.encode(url);
//        System.out.println("Base64Encoder : " + result);
//
//        UrlEncoder urlEncoder = context.getBean(UrlEncoder.class);
//        encoder.setIEncoder(urlEncoder);
//        result = encoder.encode(url);
//        System.out.println("UrlEncoder : " + result);

//        Encoder encoder = context.getBean(Encoder.class);
//        String url = "www.naver.com/books/it?page=10&size=20&name=spring-boot";
//        String result = encoder.encode(url);
//        System.out.println(result);

        Encoder encoder = context.getBean("customBase64Encoder", Encoder.class);
        String url = "www.naver.com/books/it?page=10&size=20&name=spring-boot";
        String result = encoder.encode(url);
        System.out.println("Base64Encoder : " + result);

        encoder = context.getBean("customUrlEncoder", Encoder.class);
        result = encoder.encode(url);
        System.out.println("UrlEncoder : " + result);
    }

}

@Configuration
class AppConfig {

    @Bean("customBase64Encoder")
    public Encoder encoder(Base64Encoder base64Encoder) {
        return new Encoder(base64Encoder);
    }

    @Bean("customUrlEncoder")
    public Encoder encoder(UrlEncoder base64Encoder) {
        return new Encoder(base64Encoder);
    }

}