package com.edu.carportal;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class CarPortalApplicationTests {

    @Test
    public void contextLoads() {
        ListeningExecutorService executorService = MoreExecutors.listeningDecorator(Executors.newCachedThreadPool());
        ListenableFuture<Integer> listenableFuture = executorService.submit(() -> {
            log.info("123");
            return 7;
        });
        log.info("0");
        System.out.println("1");
        log.info("2");
        listenableFuture.addListener(() -> {
            try {
                log.info(listenableFuture.get() + "1234");
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace( );
            }
        }, executorService);
        log.info("3");
    }
}

