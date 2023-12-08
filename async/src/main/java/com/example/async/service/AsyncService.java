package com.example.async.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class AsyncService {

    @Async("async-thread")
    public CompletableFuture run() {
        return new AsyncResult(test()).completable();
    }

    @Async
    public String test() {
        for (int i = 0, len = 10; i < len; i++) {
            try {
                Thread.sleep(1000);
                log.info("Thread sleep......");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return "async test method";
    }


}
