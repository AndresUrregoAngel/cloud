import boto3


client = boto3.resource('dynamodb')
clients = boto3.client('dynamodbstreams')
table = client.Table('songs')


item = {"songname":"Africa",
        "year":"1986"}

response = table.put_item(
    Item= item
)

response = table.update_item(
    Key=item,
    UpdateExpression='SET band = :val',
    ExpressionAttributeValues={
        ':val': "NewToto"
    }
 )

response = clients.describe_stream(
    StreamArn='arn:aws:dynamodb:us-east-1:account:table/songs/stream/2018-09-04T00:03:49.742',
    Limit=3,
    #ExclusiveStartShardId='string'
)
print(response['StreamDescription']['Shards'])

response = clients.get_shard_iterator(
    ShardId='shardId-numeric',
    ShardIteratorType='TRIM_HORIZON',
    StreamArn='arn:aws:dynamodb:us-east-1:account:table/songs/stream/2018-09-04T00:03:49.742',
)
print(response)


