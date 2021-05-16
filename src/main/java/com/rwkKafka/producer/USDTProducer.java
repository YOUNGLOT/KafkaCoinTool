package com.rwkKafka.producer;

import com.binance.client.SubscriptionClient;

import com.Util.CommonUtil;
import com.helper.ParallelOperate;
import com.rwkCoinApi.USDTApi;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class USDTProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(USDTProducer.class);

    private KafkaProducer<String, String> producer;
    private SubscriptionClient client;

    private CommonUtil util;

    private int currentDataIndex = 0; //    현재 처리하는 데이터의 index, Thread 중복 handle
    private int maxDataCount = 0;

    public USDTProducer(String kafkaServer) {

        //  Properties 정의 후 Client 생성
        Properties kafkaProperties = new Properties();
        kafkaProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        kafkaProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        kafkaProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        this.producer = new KafkaProducer<>(kafkaProperties);
        LOGGER.debug("Kafka Properties : {}", kafkaProperties);

        //  API 객체
        this.client = new USDTApi().client;

        this.util = new CommonUtil();
    }

    public void produce(String topicName, int threadCount) {
        LOGGER.info("TOPIC NAME : {}", topicName);

        //  Api 를 활용해 data get
        client.subscribeAllTickerEvent(
                ((data) -> {
                    LOGGER.info("받은 Data Count : {}", this.maxDataCount = data.size());
                    //  현재 받아온 Data 의 개수만큼 반복
                    while (this.currentDataIndex < this.maxDataCount) {

                        //  스레드 개수 10으로 병렬 처리 시작
                        ParallelOperate parallelOperate = new ParallelOperate(threadCount);

                        //region runable 구현 부 (아래에 Lamda 식으로 구현)
//                        parallelOperate.start(new Runnable() {
//                            @Override
//                            public void run() {
//                                synchronized (ParallelOperate.class) {
//                                    //  처리할 Data 가 없을 시 return
//                                    if (currentDataIndex == maxDataCount) {
//                                        return;
//                                    }
//
//                                    //  Kafka 에 보낼  Message 정재
//                                    String message = util.symbolTicker_to_FormattedKafkaProducerMessage(data.get(currentDataIndex++));
//                                    //  정재 된 Message 를 record 에 적재
//                                    ProducerRecord<String, String> record = new ProducerRecord<>(topicName, message);
//                                    //  전송
//                                    producer.send(record, (metadata, exception) -> {
//                                        if (exception != null) {
//                                            // some exception
//                                            LOGGER.error("------------------------------------ exception ------------------------------------");
//                                            LOGGER.error("error : {}", exception);
//                                            LOGGER.error("------------------------------------ exception ------------------------------------");
//                                        }
//                                    });
//                                }
//                            }
//                        });
                        //endregion

                        //region Lamda 식으로 표현한 parallel Operate : 더 깔끔하다
                        parallelOperate.start(()->{
                            synchronized (ParallelOperate.class) {
                                //  처리할 Data 가 없을 시 return
                                if (currentDataIndex == (data).size()) {
                                    return;
                                }

                                //  Kafka 에 보낼  Message 정재
                                String msg = util.symbolTicker_to_FormattedKafkaProducerMessage(data.get(currentDataIndex++));
                                //  정재 된 Message 를 record 에 적재
                                ProducerRecord<String, String> record = new ProducerRecord<>(
                                        topicName
                                        , msg
                                );

                                //  전송
                                producer.send(record, (metadata, exception) -> {
                                    if (exception != null) {
                                        // some exception
                                        LOGGER.error("------------------------------------ exception ------------------------------------");
                                        LOGGER.error("error : {}", exception);
                                        LOGGER.error("------------------------------------ exception ------------------------------------");
                                    }
                                });
                            }
                        });
                        //endregion

                    }

                    LOGGER.info("Data 전송 완료,    {} ", this.currentDataIndex);
                    //  데이터 인덱스 초기화
                    this.currentDataIndex = 0;
                }),
                ((exception) -> {
                    LOGGER.error("Data 전송 실패, {} / {} 번째 Data \n Error : {}\n ErrorType : {}"
                            , this.currentDataIndex
                            , this, maxDataCount
                            , exception
                            , exception.getErrType()
                    );
                })
        );
    }

}