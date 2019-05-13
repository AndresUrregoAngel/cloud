package myproject;

import com.google.api.services.bigquery.model.*;
import org.apache.beam.sdk.*;
import org.apache.beam.sdk.io.*;
import org.apache.beam.sdk.io.gcp.bigquery.*;
import org.apache.beam.sdk.io.kafka.*;
import org.apache.beam.sdk.options.*;
import org.apache.beam.runners.direct.DirectRunner;
import org.apache.beam.sdk.transforms.*;
import org.apache.beam.sdk.values.*;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.*;
import org.apache.kafka.common.utils.AppInfoParser;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO;

import pkgingestion.bqfieldschemabuilder;
import pkgingestion.kafkaconsumer;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.*;

public class gcstobqpipeline {


    public static void main(String [] args){

        PipelineOptions options = PipelineOptionsFactory.create();
        options.setRunner(DirectRunner.class);
        options.setTempLocation("gs://bucket/tmp");

        Pipeline p = Pipeline.create(options);

        PCollection<TableRow> tablerow =
                p.apply("Reading file",TextIO.read().from("gs://bucket/bqoutput/citibike_trips.csv"))
                .apply(ParDo.of(new DoFn<String,TableRow>(){
                    @ProcessElement
                    public void processElement (ProcessContext pc){
                        List<String> headers = Arrays.asList("tripduration","starttime","stoptime","start_station_id","start_station_name",
                                                "start_station_latitude","start_station_longitude","end_station_id","end_station_name",
                                                "end_station_latitude","end_station_longitude","bikeid","usertype","birth_year","gender");


                        String payload = pc.element();
                        List<String> row = Arrays.asList(payload.split(","));
                        System.out.println("before the loop: "+pc.element());
                        TableRow tbl = new TableRow();
                        for (int i=0;i<headers.size();i++){
//                            System.out.println("once within the loop: "+row.get(i));
                            tbl.put(headers.get(i),row.get(i));
                        }
                        pc.output(tbl);
                        }
                     }));



        tablerow.apply("Write on BQ",
                BigQueryIO.writeTableRows()
                .withSchema(buildbqschema())
                .to("project:dataset.tbl_citibike_trips")
                .withCustomGcsTempLocation(ValueProvider.StaticValueProvider.of("gs://bucket/tmp"))
                .withWriteDisposition(BigQueryIO.Write.WriteDisposition.WRITE_APPEND)
                .withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_IF_NEEDED));



        p.run().waitUntilFinish();

    }


    public static TableSchema buildbqschema (){


        bqfieldschemabuilder bqschema = new bqfieldschemabuilder();

        bqschema.intField("tripduration");
        bqschema.timestampField("starttime");
        bqschema.timestampField("stoptime");
        bqschema.intField("start_station_id");
        bqschema.stringField("start_station_name");
        bqschema.floatField("start_station_latitude");
        bqschema.floatField("start_station_longitude");
        bqschema.intField("end_station_id");
        bqschema.stringField("end_station_name");
        bqschema.floatField("end_station_latitude");
        bqschema.floatField("end_station_longitude");
        bqschema.intField("bikeid");
        bqschema.stringField("usertype");
        bqschema.intField("birth_year");
        bqschema.stringField("gender");

        return bqschema.schema();
    }

}
