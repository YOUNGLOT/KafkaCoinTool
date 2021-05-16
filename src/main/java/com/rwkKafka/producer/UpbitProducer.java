package com.rwkKafka.producer;

import com.Util.CommonUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.SneakyThrows;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.UUID;

public class UpbitProducer {
    private String accessKey = "LvTLyYxh16NcLv3PN5e5a8ekyDWyPibV7ms7V2I2";
    private String secretKey = "CvLYgmJkVqxMpXxcW1ZYUzDfUOXL3SsXR5eXkj34";

    private String baseUrl = "https://api.upbit.com/";

    private HttpClient client;
    private Algorithm algorithm;

    private CommonUtil util;


    public UpbitProducer() {
        this.client = HttpClientBuilder.create().build();
        this.algorithm = Algorithm.HMAC256(secretKey);
        this.util = new CommonUtil();
    }

    public static void main(String[] args) {
        UpbitProducer iuiu = new UpbitProducer();
        iuiu.solve();
    }

    @SneakyThrows
    public void solve() {
        JSONArray jsonArray = (JSONArray) util.jsonParser().parse(this.foo("v1/market/all"));

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = (JSONObject) jsonArray.get(i);

            String market = jsonObject.get("market").toString();
            String korean_name = jsonObject.get("korean_name").toString();
            String english_name = jsonObject.get("english_name").toString();

            System.out.println(market);
            String temp = this.foo(String.format("v1/trades/ticks?count=1&market=%s", market));
            System.out.println(temp);

            JSONArray jsonArray1 = (JSONArray) util.jsonParser().parse(temp);
            for (int j = 0; j < jsonArray1.size(); j++) {
                JSONObject jsonObject1 = (JSONObject) jsonArray1.get(j);
                System.out.println(jsonObject1);
            }

        }
    }

    public String foo(String query) {
        String result = "";

        String jwtToken = JWT.create()
                .withClaim("access_key", this.accessKey)
                .withClaim("nonce", UUID.randomUUID().toString())
                .sign(this.algorithm);

        String authenticationToken = "Bearer " + jwtToken;


        try {
            HttpGet request = new HttpGet(this.baseUrl + query);
            request.setHeader("Content-Type", "application/json");
            request.addHeader("Authorization", authenticationToken);

            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();

            result = EntityUtils.toString(entity, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
