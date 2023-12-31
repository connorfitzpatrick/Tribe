package com.friendsocial.Backend.elasticsearch;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {

  @Value("${elasticsearch.host}")
  private String host;

  @Value("${elasticsearch.port}")
  private int port;

  @Bean
  public RestHighLevelClient elasticsearchClient() {
    return new RestHighLevelClient(
            RestClient.builder(new HttpHost(host, port, "http"))
    );
  }

  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper();
  }
}