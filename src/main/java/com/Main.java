package com;

import com.rwkKafka.producer.USDTProducer;

public class Main {

    public static void main(String[] args) {
        USDTProducer usdtProducer = new USDTProducer("rwkkube01:9092,rwkkube02:9092,rwkkube03:9092,rwkkube04:9092");

        usdtProducer.produce("USDT", 50);
    }
}
