package com.company;

public class Main {

    public static void main(String[] args) {
        String url = "www.naver.com/books/it?page=10&size=20&name=spring-boot";

        IEncoder encoder = new Encoder(new Base64Encoder());
        String result = encoder.encode(url);
        System.out.println("Base64Encoder : " + result);

        IEncoder urlEncoder = new Encoder(new UrlEncoder());
        String urlResult = urlEncoder.encode(url);
        System.out.println("UrlEncoder : " + urlResult);
    }
}
