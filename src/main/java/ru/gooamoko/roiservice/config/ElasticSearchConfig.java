package ru.gooamoko.roiservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "ru.gooamoko.roiservice.repository")
public class ElasticSearchConfig extends ElasticsearchConfiguration {
    @Value("${elasticsearch.address}")
    private String elasticAddress;

    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration
                .builder()
                .connectedTo(elasticAddress)
                .build();
    }
}
