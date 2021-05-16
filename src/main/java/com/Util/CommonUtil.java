package com.Util;

import com.binance.client.model.event.SymbolTickerEvent;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CommonUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommonUtil.class);

    private JSONParser jsonParser;

    public CommonUtil() {
        this.jsonParser = new JSONParser();
    }

    public JSONParser jsonParser() {
        return this.jsonParser;
    }

    public String symbolTicker_to_FormattedKafkaProducerMessage(SymbolTickerEvent data) {
        String kafkaProducerMessage = "";

        try {
            String eventType = data.getEventType();
            String symbol = data.getSymbol(); //= ICPUSDT,
            String priceChange = data.getPriceChange().toString(); //= -46.97,
            String priceChangePercent = data.getPriceChangePercent().toString(); //= -15.655,
            String weightedAvgPrice = data.getWeightedAvgPrice().toString(); //= 264.99,
            String lastPrice = data.getLastPrice().toString(); //= 253.06,
            String lastQty = data.getLastQty().toString(); //= 0.9,
            String open = data.getOpen().toString(); //= 300.03,
            String high = data.getHigh().toString(); //= 300.03,
            String low = data.getLow().toString(); //= 238.69,
            String totalTradedBaseAssetVolume = data.getTotalTradedBaseAssetVolume().toString(); //= 564961.76,
            String totalTradedQuoteAssetVolume = data.getTotalTradedQuoteAssetVolume().toString(); //= 149707541.69,
            String eventTime = data.getEventTime().toString(); //= 1621136464002,
            String openTime = data.getOpenTime().toString(); //= 1621050060000,
            String closeTime = data.getCloseTime().toString(); //= 1621136463997,
            String firstId = data.getFirstId().toString(); //= 2847235,
            String lastId = data.getLastId().toString(); //= 3230024,
            String count = data.getCount().toString(); //= 382765

            //  총 18개
            kafkaProducerMessage = String.format(
                    "pipe|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s|%s",
                    eventType
                    , symbol
                    , priceChange
                    , priceChangePercent
                    , weightedAvgPrice
                    , lastPrice, lastQty
                    , open
                    , high
                    , low
                    , totalTradedBaseAssetVolume
                    , totalTradedQuoteAssetVolume
                    , eventTime
                    , openTime
                    , closeTime
                    , firstId
                    , lastId
                    , count
            );
        } catch (Exception e) {
            LOGGER.error("Data Format Exception");
            e.printStackTrace();
        }

        return kafkaProducerMessage;
    }


}
