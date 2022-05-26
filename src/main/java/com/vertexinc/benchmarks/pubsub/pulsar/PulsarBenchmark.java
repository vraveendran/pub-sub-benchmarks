package com.vertexinc.benchmarks.pubsub.pulsar;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.bookkeeper.stats.NullStatsLogger;
import org.apache.bookkeeper.stats.Stats;
import org.apache.bookkeeper.stats.StatsProvider;
import org.apache.bookkeeper.stats.prometheus.PrometheusMetricsProvider;
import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.vertexinc.frameworks.pubsub.DistributedWorkersEnsemble;
import com.vertexinc.frameworks.pubsub.DriverConfiguration;
import com.vertexinc.frameworks.pubsub.TestResult;
import com.vertexinc.frameworks.pubsub.Worker;
import com.vertexinc.frameworks.pubsub.Workload;

public class PulsarBenchmark {

	static class Arguments {

		@Parameter(names = { "-h", "--help" }, description = "Help message", help = true)
		boolean help;

		@Parameter(names = { "-d",
				"--drivers" }, description = "Drivers list. eg.: pulsar/pulsar.yaml,kafka/kafka.yaml", required = true)
		public List<String> drivers;

		@Parameter(names = { "-w",
				"--workers" }, description = "List of worker nodes. eg: http://1.2.3.4:8080,http://4.5.6.7:8080")
		public List<String> workers;

		@Parameter(names = { "-wf",
				"--workers-file" }, description = "Path to a YAML file containing the list of workers addresses")
		public File workersFile;

		@Parameter(description = "Workloads", required = true)
		public List<String> workloads;

		@Parameter(names = { "-o", "--output" }, description = "Output", required = false)
		public String output;
	}

	private static final Logger log = LoggerFactory.getLogger(PulsarBenchmark.class);

	private static final ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();

	private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

	private static final ObjectMapper mapper = new ObjectMapper(new YAMLFactory())
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	static {
		mapper.enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE);
	}

	public static void main(String[] args) throws Exception {

		final Arguments arguments = new Arguments();
		JCommander jc = new JCommander(arguments);
		jc.setProgramName("messaging-benchmark");

		// Dump configuration variables
		log.info("Starting benchmark with config: {}", writer.writeValueAsString(arguments));

		List<String> workloadList = new ArrayList<>();
		workloadList.add("workloads/simple-workload.yaml");
		//workloadList.add("workloads/100-topics-1-partitions-1kb.yaml");
		

		List<String> driverList = new ArrayList<>();
		driverList.add("drivers/pulsar.yaml");

		Map<String, Workload> workloads = new TreeMap<>();
		for (String path : workloadList) {
			File file = new File(path);
			String name = file.getName().substring(0, file.getName().lastIndexOf('.'));

			workloads.put(name, mapper.readValue(file, Workload.class));
		}

		log.info("Workloads: {}", writer.writeValueAsString(workloads));

		Worker worker;

		/*
		 * Configuration conf = new CompositeConfiguration();
		 * conf.setProperty(Stats.STATS_PROVIDER_CLASS,
		 * PrometheusMetricsProvider.class.getName());
		 * conf.setProperty("prometheusStatsHttpPort", 8001);
		 * Stats.loadStatsProvider(conf); StatsProvider provider = Stats.get();
		 * provider.start(conf);
		 */

		if (arguments.workers != null && !arguments.workers.isEmpty()) {
			worker = new DistributedWorkersEnsemble(arguments.workers);
		} else {
			// Use local worker implementation
			worker = new PulsarBenchmarkWorker(NullStatsLogger.INSTANCE);
		}

		workloads.forEach((workloadName, workload) -> {

			try {
				workload.validate();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
				System.exit(-1);
			}

			driverList.forEach(driverConfig -> {
				try {
					File driverConfigFile = new File(driverConfig);
					DriverConfiguration driverConfiguration = mapper.readValue(driverConfigFile,
							DriverConfiguration.class);

					log.info("--------------- WORKLOAD : {} --- DRIVER : {}---------------", workload.name,
							driverConfiguration.name);

					// Stop any left over workload
					worker.stopAll();

					worker.initializeDriver(new File(driverConfig));

					PulsarWorkloadGenerator generator = new PulsarWorkloadGenerator(driverConfiguration.name, workload,
							worker);

					TestResult result = generator.run();

					String fileName = arguments.output.length() > 0 ? arguments.output
							: String.format("%s-%s-%s.json", workloadName, driverConfiguration.name,
									dateFormat.format(new Date()));

					log.info("Writing test result into {}", fileName);
					writer.writeValue(new File(fileName), result);

					generator.close();
				} catch (Exception e) {
					log.error("Failed to run the workload '{}' for driver '{}'", workload.name, driverConfig, e);
				} finally {
					try {
						worker.stopAll();
					} catch (IOException e) {
					}
				}
			});
		});

		worker.close();

	}

}
