package naucna_centrala.nc.elasticsearch;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("elasticsearch")
public class Properties {
    public String hostname = "localhost";
    public String scheme = "http";
    public Integer port = 9200;
}
