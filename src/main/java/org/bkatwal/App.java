package org.bkatwal;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.Optional;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.common.cloud.ZkConfigManager;

/**
 * A utility demo for config upload script.
 */
public class App {

  public static void main(String[] args) throws URISyntaxException, IOException {

    // 1. create solr client
    SolrClient solrClient = new CloudSolrClient.Builder(Collections.singletonList("localhost:9983"),
        Optional.empty()).build();
    System.out.println("Solr Client initialized");
    //2. Get ZK config manager
    ZkConfigManager zkConfigManager = ((CloudSolrClient) solrClient).getZkStateReader()
        .getConfigManager();

    //3. push config example-conf from the resources folder to solrCloud
    Path confPath = Paths.get(ClassLoader.getSystemResource("search-conf").toURI());
    zkConfigManager.uploadConfigDir(confPath, "search-conf");
    System.out.println("Configs Uploaded");
    System.exit(0);
  }
}

