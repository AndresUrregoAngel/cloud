
import java.util.concurrent.CompletableFuture;

import software.amazon.awssdk.services.kinesis.KinesisAsyncClient;
import software.amazon.awssdk.services.kinesis.model.ShardIteratorType;
import software.amazon.awssdk.services.kinesis.model.SubscribeToShardEvent;
import software.amazon.awssdk.services.kinesis.model.SubscribeToShardRequest;
import software.amazon.awssdk.services.kinesis.model.SubscribeToShardResponseHandler;
import software.amazon.awssdk.services.kinesis.model.Record;
import java.util.List;




public class fanoutconsumer {


    private static final String CONSUMER_ARN = " ";
    private static final String SHARD_ID = "shardId-000000000000";

    public static void main(String[] args) {

        KinesisAsyncClient client = KinesisAsyncClient.create();

        SubscribeToShardRequest request = SubscribeToShardRequest.builder()
                .consumerARN(CONSUMER_ARN)
                .shardId(SHARD_ID)
                .startingPosition(s -> s.type(ShardIteratorType.LATEST)).build();

        // Call SubscribeToShard iteratively to renew the subscription periodically.
        while(true) {
            // Wait for the CompletableFuture to complete normally or exceptionally.
            callSubscribeToShardWithVisitor(client, request).join();
            System.out.println("Reding streaming records");
        }

        // Close the connection before exiting.
        // client.close();
    }


    /**
     * Subscribes to the stream of events by implementing the SubscribeToShardResponseHandler.Visitor interface.
     */
    private static CompletableFuture<Void> callSubscribeToShardWithVisitor(KinesisAsyncClient client, SubscribeToShardRequest request) {
        SubscribeToShardResponseHandler.Visitor visitor = new SubscribeToShardResponseHandler.Visitor() {
            @Override
            public void visit(SubscribeToShardEvent event) {
                System.out.println("Received subscribe to shard event " + event);

                List<Record> recordList = event.records();
                for (Record record : recordList) {
                    System.out.println(record.data().asUtf8String());
                }

            }
        };
        SubscribeToShardResponseHandler responseHandler = SubscribeToShardResponseHandler
                .builder()
                .onError(t -> System.err.println("Error during stream - " + t.getMessage()))
                .subscriber(visitor)
                .build();
        return client.subscribeToShard(request, responseHandler);
    }





}




