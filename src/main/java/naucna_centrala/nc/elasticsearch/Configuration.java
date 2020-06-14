/*package naucna_centrala.nc.elasticsearch;

import org.apache.http.HttpHost;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
@EnableConfigurationProperties(Properties.class)
public class Configuration {

    @Autowired
    private Properties properties;

    @Bean
    public RestHighLevelClient elasticsearchClient() {
        return new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(properties.hostname, properties.port, properties.scheme)
                )
        );
    }

    @Bean
    public IndicesClient elasticsearchIndicesClient() {
        return elasticsearchClient().indices();
    }
}*/

