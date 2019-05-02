package bnclabs;


import com.google.api.services.bigquery.model.*;
import org.apache.beam.sdk.*;
import org.apache.beam.sdk.io.gcp.bigquery.*;
import org.apache.beam.sdk.io.kafka.*;
import org.apache.beam.sdk.options.*;
//import org.apache.beam.runners.direct.DirectRunner;
import org.apache.beam.sdk.transforms.*;
import org.apache.beam.sdk.values.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.*;
import org.apache.kafka.common.utils.AppInfoParser;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO;


import bncingestion.kafkaconsumer;

import java.lang.reflect.*;
import java.util.*;

public class executor_consumer {

    public static void main (String[] args){


        //Define the table schema for BQ
        TableSchema tschema = kafkaconsumer.bqSchema();

        PipelineOptions options = PipelineOptionsFactory.fromArgs(args).create();
//        PipelineOptions options = PipelineOptionsFactory.create();
//        options.setRunner(DirectRunner.class);

        Pipeline p = Pipeline.create(options);

        PCollection<String>  kafkapayload =    p.apply("Reading the topic" ,KafkaIO.<Long,String>read().withBootstrapServers("35.203.108.239:9092")
                .withTopic("bnc").withKeyDeserializer(LongDeserializer.class)
                .withValueDeserializer(StringDeserializer.class)
                .withMaxNumRecords(10)
                .withoutMetadata())

                //Catching only the strings
                .apply(Values.<String>create());


        PCollection<TableRow> tableRows = kafkapayload.apply("Read the received payload", ParDo.of(new DoFn<String,TableRow>(){
            @ProcessElement
            public void processElement (ProcessContext processContext){
                List<String> headers = Arrays.asList("employeeId","name","phonenumber","beer","company","address_street","address_zipcode");
                List<String> rowValues = kafkaconsumer.validating_payload(processContext.element());
                TableRow  tbl =  new TableRow();
                for (int i=0;i<headers.size();i++){
                    tbl.put(headers.get(i),rowValues.get(i));
                }
                processContext.output(tbl);
            }
        })
          );

        tableRows.apply(
                "Ingestion in BIgQuery",
                BigQueryIO.writeTableRows().withSchema(tschema)
                .to("slalom-technical-solutions:poc.bnc_sample_employee")
                .withCustomGcsTempLocation(ValueProvider.StaticValueProvider.of("gs://gcplabs/tmp"))
                .withWriteDisposition(BigQueryIO.Write.WriteDisposition.WRITE_APPEND)
                .withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_IF_NEEDED)
        );



        p.run();


    }

}
