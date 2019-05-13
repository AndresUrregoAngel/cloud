package myproject;


import com.google.api.services.bigquery.model.*;
import org.apache.beam.sdk.*;
import org.apache.beam.sdk.io.*;
import org.apache.beam.sdk.io.jdbc.JdbcIO;
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
import sun.awt.image.*;

import java.lang.reflect.*;
import java.sql.*;
import java.util.*;
import java.util.stream.*;



public class gcstosql {


    public static void main(String [] args){

        PipelineOptions options = PipelineOptionsFactory.fromArgs(args).create();
 //       PipelineOptions options = PipelineOptionsFactory.create(); /*locally*/
//        options.setRunner(DirectRunner.class);
//        options.setTempLocation("gs://bucket/tmp");

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
                                TableRow tbl = new TableRow();
                                for (int i=0;i<headers.size();i++){
//                            System.out.println("once within the loop: "+row.get(i));
                                    tbl.put(headers.get(i),row.get(i));
                                }
                                pc.output(tbl);
                            }
                        }));


        tablerow.apply(
                "Write on MSSQL",
                JdbcIO.<TableRow>write().
                withDataSourceConfiguration(JdbcIO.DataSourceConfiguration.create(
                        "com.microsoft.sqlserver.jdbc.SQLServerDriver","jdbc:sqlserver://host;databaseName=mydb"
                ).withUsername("user").withPassword("password"))
                .withStatement("insert into tbl_citibike_trips values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ")
                .withPreparedStatementSetter(new JdbcIO.PreparedStatementSetter<TableRow>() {
                    @Override
                    public void setParameters(TableRow element, PreparedStatement query) throws Exception {

                        query.setInt(1 , Integer.parseInt( element.get("tripduration").toString()) );
                        query.setString(2,element.get("starttime").toString());
                        query.setString(3,element.get("stoptime").toString());
                        query.setInt(4,Integer.parseInt(element.get("start_station_id").toString()));
                        query.setString(5,element.get("start_station_name").toString());
                        query.setFloat(6, Float.parseFloat(element.get("start_station_latitude").toString()));
                        query.setFloat(7, Float.parseFloat(element.get("start_station_longitude").toString()));
                        query.setInt(8,Integer.parseInt(element.get("end_station_id").toString()));
                        query.setString(9,element.get("end_station_name").toString());
                        query.setFloat(10,Float.parseFloat(element.get("end_station_latitude").toString()));
                        query.setFloat(11,Float.parseFloat(element.get("end_station_longitude").toString()));
                        query.setInt(12,Integer.parseInt(element.get("bikeid").toString()));
                        query.setString(13,element.get("usertype").toString());
                        query.setInt(14, Integer.parseInt(element.get("birth_year").toString()));
                        query.setString(15,element.get("gender").toString());
                    }
                })
        )    ;

        p.run().waitUntilFinish();

    }


}
