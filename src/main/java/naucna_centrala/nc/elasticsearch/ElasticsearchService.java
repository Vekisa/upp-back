/*package naucna_centrala.nc.elasticsearch;

import naucna_centrala.nc.model.Labor;
import org.apache.commons.io.IOUtils;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.PutMappingRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class ElasticsearchService {

    @Autowired
    private RestHighLevelClient elasticsearchClient;

    @Autowired
    private IndicesClient elasticsearchIndicesClient;

    private final String RADOVI_INDEKS = "radovi";

    @PostConstruct
    public void kreirajIndeksRadova() {
        try {
            if(elasticsearchIndicesClient.exists(new GetIndexRequest(RADOVI_INDEKS), RequestOptions.DEFAULT))
                elasticsearchIndicesClient.delete(new DeleteIndexRequest(RADOVI_INDEKS), RequestOptions.DEFAULT);
            elasticsearchIndicesClient.create(
                    new CreateIndexRequest(RADOVI_INDEKS),
                    RequestOptions.DEFAULT
            );
            elasticsearchIndicesClient.putMapping(
                    podesavnja(),
                    RequestOptions.DEFAULT
            );
            Labor rad = new Labor();
            rad.setNaslov("Probni");
            dodajDokument(rad);
        } catch(IOException e) {
            // TODO : Log
        }
    }

    private PutMappingRequest podesavnja() throws IOException {
        PutMappingRequest zahtev = new PutMappingRequest(RADOVI_INDEKS);
        zahtev.source(
                IOUtils.toString(new FileInputStream(ResourceUtils.getFile("classpath:elasticsearch/settings.json"))),
                XContentType.JSON
        );
        return zahtev;
    }

    public void dodajDokument(Labor labor) throws IOException {
        IndexRequest zahtev = new IndexRequest(RADOVI_INDEKS);
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("naslov", labor.getNaslov());
        zahtev.source(jsonMap);
        zahtev.type("_doc");
        elasticsearchClient.index(zahtev, RequestOptions.DEFAULT);
    }
}
*/