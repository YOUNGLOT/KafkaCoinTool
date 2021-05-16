package com.helper;

import com.rwkCoinApi.USDTApi;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ParallelOperate {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParallelOperate.class);

    private int threadCount; // 사용할 스레드 갯수

    //  생성자 (사용할 스레드 갯수)
    public ParallelOperate(int threadCount) {
        this.threadCount = threadCount;
        LOGGER.debug("사용할 스래드 Count : {}", threadCount);
    }

    //  매개변수 (method) 를 다중 스레드 run start
    public void start(Runnable runnable) throws InterruptedException {

        //  스레드를 threadCount 만큼 생성 후, 담을 List
        List<Thread> threadList = new ArrayList<>();
        LOGGER.debug("스래드 run 시작");

        //  스레드 생성 / List add / start
        for (int i = 0; i < threadCount; i++) {
            Thread thread = new Thread(runnable);
            threadList.add(thread);
            thread.start();
        }

        //  작업 완료
        for (int i = 0; i < threadList.size(); i++) {
            threadList.get(i).join();
            LOGGER.debug("* {} 번 스레드 작업 완료", i);
        }
    }

}
