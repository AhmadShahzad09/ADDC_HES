//package com.minsait.oum.mdc;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.math.BigInteger;
//import java.util.Properties;
//
//import org.apache.kafka.clients.producer.KafkaProducer;
//import org.apache.kafka.clients.producer.ProducerConfig;
//import org.apache.kafka.clients.producer.ProducerRecord;
//import org.apache.kafka.common.serialization.StringSerializer;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.gson.Gson;
//import com.minsait.oum.mdc.input.object.TaskInput;
//
//public class ExampleInsertingKafka {
//
//	private static final Logger log = LoggerFactory.getLogger(ExampleInsertingKafka.class);
//
//	private static String url = "10.86.1.47:9095";
//
//	private static String token = "881474c293b44b4f99230b2e054875df";
//
//	private static String deviceTemplate = "colecta-client";
//
//	private static String prefix = "ONTOLOGY_";
//
//	private static String ontology = "request-topic";
//
//	private static Properties createConfig(String token, String clientPlatform) {
//
//		Properties config = new Properties();
//
//		config.put(ProducerConfig.CLIENT_ID_CONFIG, "localhost");
//
//		config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, url);
//
//		config.put("security.protocol", "SASL_PLAINTEXT");
//
//		config.put("sasl.mechanism", "PLAIN");
//
//		config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//
//		config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//
//		config.put("sasl.jaas.config", "org.apache.kafka.common.security.plain.PlainLoginModule required username=\""
//
//				+ clientPlatform + "\" password=\"" + token + "\";");
//		config.put("auto.offset.reset", "earliest");
//
//		return config;
//
//	}
//
//	private static JsonNode getInstances() throws IOException {
//
//		ClassLoader classLoader = ExampleInsertingKafka.class.getClassLoader();
//
//		File initialFile = new File("e:\\\\call-json.json");
//
//		final ObjectMapper mapper = new ObjectMapper();
//
//		JsonNode rootNode = mapper.readTree(new FileInputStream(initialFile));
//
//		return rootNode;
//
//	}
//
//	private static void insertInstance(KafkaProducer<String, String> producer, String ontology, String instance,
//			String prefix) {
//
//		try {
//			TaskInput input = new TaskInput();
//			input.setGroup(new BigInteger("1637495461271"));
//			input.setIdTask(1);
//			input.setIdOrder(1);
//
//			producer.send(new ProducerRecord(ontology, null, instance));
//
//			// metadata.get();
//
//		} catch (Exception e) {
//
//			throw new RuntimeException("Error inserting data with kafka", e);
//
//		}
//
//	}
//
//	public static void main(String... args) throws IOException, InterruptedException {
//
////		JsonFormat.parser().
//
//		log.info("Starting insert example...");
//
//		Properties config = createConfig(token, deviceTemplate);
//
//		log.info("Configuraion stablished");
//
//		KafkaProducer<String, String> producer = new KafkaProducer<>(config);
//
//		log.info("Kafka producer created");
//
//		JsonNode allData = getInstances();
//
//		log.info("Example instances loaded");
//
//		String instanceString = allData.toString();
//
//		insertInstance(producer, ontology, instanceString, prefix);
////
////        Consumer<JsonNode> insertInstance = new Consumer<JsonNode>() {
////
////            @Override
////
////            public void accept(JsonNode instance) {
////
////                String instanceString = instance.toString();
////
////                insertInstance(producer, ontology, instanceString, prefix);
////
////                log.info("instance inserted - {}: {}", new Date().getTime(), instanceString);
////
////            }
////
////        };
//
////        if (allData.isArray()) {
////
////            ArrayNode arrayInstances = (ArrayNode) allData;
////
////            arrayInstances.forEach(insertInstance);
////
////        }      
//
//		producer.flush();
//
//		producer.close();
//
//	}
//
//}