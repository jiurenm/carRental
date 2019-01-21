package com.edu.caradmin;

import com.edu.car.uid.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class CarAdminApplicationTests {
//    private CompletableFuture future1 = CompletableFuture.supplyAsync(this::a);
//    private CompletableFuture future2 = CompletableFuture.supplyAsync(this::b);
//    private CompletableFuture future3 = CompletableFuture.supplyAsync(this::c);
//
//    public long a(){
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        log.info("a;");
//        return IdWorker.getId();
//    }
//    public long b(){
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        log.info("b:");
//        return IdWorker.getId();
//    }
//    public long c(){
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        log.info("c:");
//        return IdWorker.getId();
//    }
//
//    @Test
//    public void test() throws ExecutionException, InterruptedException {
//        System.out.println("a:"+future1.get());
//        System.out.println("b:"+future2.get());
//        System.out.println("c:"+future3.get());
//    }

    @Test
    public void test() {
    }
}

