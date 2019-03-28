package com.streaming.sparkdemo;


import org.apache.spark.*;
import org.apache.spark.streaming.*;
import org.apache.spark.streaming.api.java.*;
import org.apache.spark.storage.StorageLevel;
import org.apache.spark.streaming.kinesis.KinesisInputDStream;
import org.apache.spark.streaming.Seconds;
import org.apache.spark.streaming.StreamingContext;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.InitialPositionInStream;

public class counter {
	
	public static void main( String[] args )
    {
		SparkConf conf = new SparkConf().setAppName("mydemo").setMaster("local[*]");
		JavaStreamingContext ssc = new JavaStreamingContext(conf, Duration(1000));
		
		 /*KinesisInputDStream<byte[]> kinesisStream = KinesisInputDStream.builder
		         .streamingContext(streamingContext)
		         .endpointUrl([endpoint URL])
		         .regionName([region name])
		         .streamName([streamName])
		         .initialPositionInStream([initial position])
		         .checkpointAppName([Kinesis app name])
		         .checkpointInterval([checkpoint interval])
		         .storageLevel(StorageLevel.MEMORY_AND_DISK_2)
		         .build();*/
      
        System.out.println( "Hello World!" );
    }

}
