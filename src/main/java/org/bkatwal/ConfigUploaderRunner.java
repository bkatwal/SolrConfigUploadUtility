package org.bkatwal;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.common.cloud.ZkConfigManager;

/**
 * A utility demo for config upload script. Use 3 params to run this program:
 * <p>
 * 1. Config name - It need to be same as the existing one.
 * <p>
 * 2. Zookeeper host
 * <p>
 * 3. Environment
 * <p>
 * Example program arguments: localhost:9983 search-conf qa
 */
public class ConfigUploaderRunner {

  public static void main(String[] args) throws URISyntaxException, IOException {

    if (args.length != 3) {
      System.out.println("Invalid number of args!");
      System.exit(1);
    }
    // comma separated zk hosts
    String zkHosts = args[0];
    String configName = args[1];
    String env = args[2];

    List<String> zkHostList = Arrays.asList(zkHosts.split(","));
    SolrClient solrClient = new CloudSolrClient.Builder(zkHostList, Optional.empty()).build();
    System.out.println("Solr Client initialized");

    //2. Get ZK config manager
    ZkConfigManager zkConfigManager = ((CloudSolrClient) solrClient).getZkStateReader()
        .getConfigManager();

    //3. push config example-conf from the resources folder to solrCloud
    Path confPath = Paths.get(ClassLoader.getSystemResource(env + "/" + configName).toURI());
    zkConfigManager.uploadConfigDir(confPath, configName);
    System.out.println("Configs Uploaded");
    System.exit(0);
  }
}

