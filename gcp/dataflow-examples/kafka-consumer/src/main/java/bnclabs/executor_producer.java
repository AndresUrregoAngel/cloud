package bnclabs;

import bncproducer.*;
import com.github.javafaker.*;
import com.google.gson.*;
import org.apache.kafka.clients.producer.*;

import java.util.concurrent.*;

public class executor_producer {


    public static void main (String[] args){

        runProducer();

    }


    static void runProducer() {
        Producer<Long, String> producer = kafkaproducer.createProducer();

        for (int index = 0; index < IKafkaParameters.MESSAGE_COUNT; index++) {

            String payload = bncproducer.kafkaproducer.generate_payload();
            final ProducerRecord<Long, String> record = new ProducerRecord<Long, String>(IKafkaParameters.TOPIC_NAME,
                    payload);
            try {
                RecordMetadata metadata = producer.send(record).get();
                System.out.println("Record sent with key " + index + " to partition " + metadata.partition()
                        + " with offset " + metadata.offset());
            } catch (ExecutionException e) {
                System.out.println("Error in sending record");
                System.out.println(e);
            } catch (InterruptedException e) {
                System.out.println("Error in sending record");
                System.out.println(e);
            }
        }
    }


}
