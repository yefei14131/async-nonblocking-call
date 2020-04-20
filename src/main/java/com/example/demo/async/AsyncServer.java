package com.example.demo.async;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author yefei
 * @date: 2020/4/19
 */

@Slf4j
public class AsyncServer {

    private ExecutorService executor;

    AsyncServer(){
        executor = Executors.newFixedThreadPool(10);
    }

    public FutureResponse<Boolean> call() {

        FutureResponse<Boolean> futureResponse = new FutureResponse<>();

        executor.submit(() -> {
            try {
                Thread.sleep(2000);
                futureResponse.setResp(true);

                executor.shutdown();
            } catch (Exception e) {
                log.error("server run error", e);
            }

        });
        // io input

        return futureResponse;
    }
}
