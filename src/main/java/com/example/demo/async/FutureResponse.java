package com.example.demo.async;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executor;

/**
 * @author yefei
 * @date: 2020/4/19
 */
@Slf4j
public class FutureResponse<T> {
    private volatile T resp = null;
    private Runnable command;
    private boolean executed;
    private Executor executor;

    public void setResp(T resp){
        this.resp = resp;
        synchronized (this) {
            notifyAll();
            this.exec();
            this.executed = true;
        }
    }

    public T getResp() throws InterruptedException {
        if (resp == null) {
            synchronized (this) {
                if (resp == null)
                    wait();
            }
        }

        return resp;
    }

    public void listen(Runnable command, Executor executor) {
        this.command = command;
        this.executor = executor;
        synchronized (this) {
            if (this.executed) {
                this.exec();
                return;
            }
        }
    }

    private void exec() {
        synchronized (this) {
            if (this.command == null || this.executor == null)
                return;
            executor.execute(command);
        }
    }
}
