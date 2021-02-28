package com.elasticsearch.springdata.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.elasticsearch.springdata.repository.es")
public class TransportClientConfig extends AbstractElasticsearchConfiguration {

	@Value("${elasticsearch.cluster-name}")
	private String clusterName;
	
	@Value("${elasticsearch.cluster-nodes}")
	private String clusterAddress;
	
//	@Value("${elasticsearch.cluster-port}")
//	private int clusterPort;
	
	@Override
    @Bean
    public RestHighLevelClient elasticsearchClient() {

        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()  
            .connectedTo(clusterAddress)
            .build();

        return RestClients.create(clientConfiguration).rest();                         
    }
}