package com.friendsocial.Backend.elasticsearch;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.erhlc.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.client.erhlc.RestClients;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.friendsocial.Backend.elasticsearch")
public class Config {

  @Bean
  public RestHighLevelClient client() {
    ClientConfiguration clientConfiguration
            = ClientConfiguration.builder()
            .connectedTo("localhost:9200")
            .build();

    return RestClients.create(clientConfiguration).rest();
  }

  @Bean
  public ElasticsearchOperations elasticsearchTemplate() {
    return new ElasticsearchRestTemplate(client());
  }
}

//  @Autowired
//  RestHighLevelClient highLevelClient;
//
//  RestClient lowLevelClient = highLevelClient.lowLevelClient();


