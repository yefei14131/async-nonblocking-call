package com.example.demo.async;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executor;

/**
 * @author yefei
 * @date: 2020/4/19
 */
@Slf4j
public class AsyncClient {

    public static void main(String[] args) throws InterruptedException {
        asyncCall();
        log.info("=============");
        asyncUnblockingCall();
        log.info("=============");
        asyncCall();
    }


    private static void asyncCall() throws InterruptedException {

        AsyncServer server = new AsyncServer();
        log.info("blocking client -> before call");
        FutureResponse<Boolean> resp = server.call();
        log.info("blocking client -> after call");
        Boolean result = resp.getResp();
        log.info("blocking client -> after get resp, {}", result);

    }

    private static void asyncUnblockingCall() {

        AsyncServer server = new AsyncServer();
        log.info("unblocking client -> before call");
        FutureResponse<Boolean> resp = server.call();
        log.info("unblocking client -> after call");
        resp.listen(() -> {
            Boolean result = null;
            try {
                result = resp.getResp();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("unblocking client -> after get resp, {}", result);
        }, new Executor() {
            @Override
            public void execute(Runnable command) {
                command.run();
            }
        });
        log.info("unblocking asyncUnblockingCall end");
    }
}
