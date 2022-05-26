package com.vertexinc.benchmarks.pubsub.pulsar;

import org.apache.pulsar.common.naming.TopicDomain;

public class PulsarClientConfig {
	
	 public String serviceUrl;

	    public String httpUrl;

	    public int ioThreads = 8;

	    public int connectionsPerBroker = 8;

	    public String namespacePrefix;

	    public String clusterName;

	    public TopicDomain topicType = TopicDomain.persistent;

	    public PersistenceConfiguration persistence = new PersistenceConfiguration();

	    public static class PersistenceConfiguration {
	        public int ensembleSize = 3;
	        public int writeQuorum = 3;
	        public int ackQuorum = 2;

	        public boolean deduplicationEnabled = false;
	    }

	    public boolean tlsAllowInsecureConnection = false;

	    public boolean tlsEnableHostnameVerification = false;

	    public String tlsTrustCertsFilePath;

	    public AuthenticationConfiguration authentication = new AuthenticationConfiguration();

	    public static class AuthenticationConfiguration {
	        public String plugin;
	        public String data;
	    }
}
