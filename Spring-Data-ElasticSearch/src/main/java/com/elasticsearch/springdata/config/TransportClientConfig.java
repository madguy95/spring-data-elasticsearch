//package com.elasticsearch.springdata.config;
//
//import java.net.InetSocketAddress;
//import java.net.UnknownHostException;
//
//import org.elasticsearch.client.Client;
//import org.elasticsearch.client.transport.TransportClient;
//import org.elasticsearch.common.settings.Settings;
//import org.elasticsearch.common.transport.TransportAddress;
//import org.elasticsearch.transport.client.PreBuiltTransportClient;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.elasticsearch.config.ElasticsearchConfigurationSupport;
//import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
//import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
//
//@Configuration
//@EnableElasticsearchRepositories(basePackages = "com.elasticsearch.springdata.repository.es")
//public class TransportClientConfig extends ElasticsearchConfigurationSupport {
//
//	@Value("${elasticsearch.cluster-name}")
//	private String clusterName;
//	
//	@Value("${elasticsearch.cluster-nodes}")
//	private String clusterAddress;
//	
//	@Value("${elasticsearch.cluster-port}")
//	private int clusterPort;
//	
//	@Bean
//    public Client client() throws UnknownHostException {
//        Settings esSettings = Settings.builder()
//                .put("cluster.name", clusterName)
//                .build();
//        TransportClient client = new PreBuiltTransportClient(esSettings);
//        client.addTransportAddress(new TransportAddress(new InetSocketAddress("192.168.1.9", clusterPort)));
//        return client;
//    }
//	
////	@Bean(name = { "elasticsearchOperations", "elasticsearchTemplate" })
////	public ElasticsearchTemplate elasticsearchTemplate() throws UnknownHostException {
////		return new ElasticsearchTemplate(client());
////	}
//}