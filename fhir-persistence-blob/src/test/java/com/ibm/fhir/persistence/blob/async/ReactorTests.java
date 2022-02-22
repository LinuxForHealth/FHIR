/*
 * (C) Copyright IBM Corp. 2022
 *
 * SPDX-License-Identifier: Apache-2.0
 */
 
package com.ibm.fhir.persistence.blob.async;

import static org.testng.Assert.fail;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import org.testng.annotations.Test;

import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Tests using Flux and Mono which are used in the Azure SDK for async
 * handling.
 */
public class ReactorTests {

    @Test
    public void test1() {
        Flux<String> flux = Flux.just("red", "white", "blue");
        Flux<String> upper = flux
                .log()
                .map(String::toUpperCase)
                ;
        upper.subscribe(new Subscriber<String>() {

            @Override
            public void onSubscribe(Subscription s) {
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(String t) {
                System.out.println(t);
            }

            @Override
            public void onError(Throwable t) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void onComplete() {
                System.out.println("all done");
            }
            
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException x) {
            //
        }
    }

    @Test
    public void test2() {
        final AtomicInteger counter = new AtomicInteger();
        Flux<Integer> flux = Flux.just("first", "second", "third")
                .flatMap(str -> Flux.just(counter.incrementAndGet()))
            ;
        flux.subscribe(System.out::println);
    }

    @Test
    public void test3() throws Exception {
        final AtomicInteger counter = new AtomicInteger();
        Mono<Integer> mono = Mono.just("one")
                .map(a -> {
                    // inject an exception (e.g. the service call failed)
                    if (a.equals("one")) {
                        throw new RuntimeException("call failed");
                    } else {
                        return a;
                    }
                })
                .flatMap(str -> Mono.just(counter.incrementAndGet()))
            ;

        // should result in an ExecutionException with our RuntimeException("call failed") as the cause
        try {
            mono.toFuture().get();
        } catch (ExecutionException x) {
            if (x.getCause() != null 
                    && x.getCause() instanceof RuntimeException 
                    && "call failed".equals(x.getCause().getMessage())) {
                // OK
            } else {
                fail();
            }
        }
    }

    @Test
    public void test4() throws Exception {
        final AtomicInteger counter = new AtomicInteger();
        Mono<Integer> mono = Mono.just("one")
                .flatMap(str -> Mono.just(counter.incrementAndGet()))
                .map(ctr -> {
                    // inject an exception from the second call
                    if (ctr == 1) {
                        throw new RuntimeException("second call failed");
                    } else {
                        return ctr;
                    }
                })
            ;

        // should result in an ExecutionException with our RuntimeException("call failed") as the cause
        try {
            mono.toFuture().get();
        } catch (ExecutionException x) {
            if (x.getCause() != null 
                    && x.getCause() instanceof RuntimeException 
                    && "second call failed".equals(x.getCause().getMessage())) {
                // OK
            } else {
                fail();
            }
        }
    }

    /**
     * example of cancelling a Flux
     */
    @Test
    public void testCancel() {
        Flux.range(0, 10)
                .subscribe(new BaseSubscriber<Integer>() {
                    @Override
                    protected void hookOnNext(Integer value) {
                    }
                });
        
    }
}
