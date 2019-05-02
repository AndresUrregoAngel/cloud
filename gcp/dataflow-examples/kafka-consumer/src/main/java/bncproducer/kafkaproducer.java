package bncproducer;


import java.util.Properties;

import com.github.javafaker.*;
import com.google.gson.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;


public class kafkaproducer {


    public static Producer<Long, String> createProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, IKafkaParameters.KAFKA_BROKERS);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, IKafkaParameters.CLIENT_ID);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return new KafkaProducer<>(props);
    }



    public static String generate_payload(){

        Faker faker = new Faker();

        JsonObject person = new JsonObject();
        JsonObject address = new JsonObject();

        String personaid = faker.idNumber().valid();
        String name = faker.name().fullName();
        String street = faker.address().streetName();
        String zipcode = faker.address().zipCode();
        String phonenumber = faker.phoneNumber().cellPhone();
        String beer = faker.beer().name();
        String company =faker.company().name();
        String appId = "APP02";

        address.addProperty("street",street);
        address.addProperty("zipcode",zipcode);
        person.addProperty("appId", appId);
        person.addProperty("employeeId", personaid.toString());
        person.addProperty("name",name);
        person.addProperty("phonenumber",phonenumber.toString());
        person.addProperty("beer",beer.toString());
        person.addProperty("company", company.toString());


        person.addProperty("address", address.toString());

        return person.toString();


    }


}
