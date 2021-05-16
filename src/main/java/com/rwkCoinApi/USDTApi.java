package com.rwkCoinApi;

import com.binance.client.SubscriptionClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class USDTApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(USDTApi.class);

    public SubscriptionClient client;

    public USDTApi(){
        String apiKey = "8CHgXlQ0QNWkhq198IGL725jzNKcbgN6xBNHgg1QJumxu4AgeUO76I5yTnaN6J2k";
        String secretKey = "lD7NV7mwXjDS8Ud5lkuouT7q6Kxo5R3wYQ7zfkC7Q2bqqzuQtQqx96rsiF3nELYs";
        this.client = SubscriptionClient.create(apiKey, secretKey);
        LOGGER.debug("USDT API Clinet 생성 완료");
    }

    public USDTApi(String apiKey, String secretKey){
        this.client = SubscriptionClient.create(apiKey, secretKey);
        LOGGER.debug("USDT API Clinet 생성 완료");
    }

}
