package com.edu.caradmin;

import com.edu.car.uid.IdWorker;
import com.google.common.collect.Sets;
import io.reactivex.*;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;


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
        Set<Integer> set1 = Sets.newHashSet(1,2,3,4,5);
        Set<Integer> set2 = Sets.newHashSet(1,2,3,4);
        Sets.SetView difference = Sets.difference(set2, set1);
        System.out.println(difference);
    }

    @Test
    public void test2() {
        for(int i=0;i<10;i++){
            System.out.println(IdWorker.getId());
        }
    }

    @Test
    public void test1() {
        Observable.just(1,1,1,2,2,3,4,5).distinct().subscribe(integer -> log.info(String.valueOf(integer)));
    }

}