import boto3
from datetime import datetime
import base64

ts = datetime.now()
recordtime = '{}{:02d}{:02d}{}{}{}'.format(ts.year,ts.month,ts.day,ts.hour,ts.minute,ts.second)


client = boto3.client('kinesis')

def get_shard(client,streamname):
    try:
    
        response = client.list_shards(
        StreamName=streamname
        # NextToken='string',
        # ExclusiveStartShardId='string',
        # MaxResults=123,
        # StreamCreationTimestamp=datetime(2015, 1, 1)
        )
    
        shardid = response['Shards'][0]['ShardId']
        print('the shardid for this shard is: {}'.format(response['Shards'][0]['ShardId']))
    
    except Except as error:
        print(error)
    else:
        return shardid
        
def get_sharditerator(client,streamname,shardid,sequencenumber):
    try:
        response = client.get_shard_iterator(
            StreamName=streamname,
            ShardId=shardid,
            ShardIteratorType= 'AT_SEQUENCE_NUMBER', #'AT_SEQUENCE_NUMBER'|'AFTER_SEQUENCE_NUMBER'|'TRIM_HORIZON'|'LATEST'|'AT_TIMESTAMP',
            StartingSequenceNumber=sequencenumber
            #Timestamp=datetime(2019, 3, 25)
        )
        
        sharditerator = response['ShardIterator']
        print('Use this shard iterator to pull data from the streaming service: {}'.
        format(response['ShardIterator']))
        
    except Exception as error:
        print(error)
        
    else:
        return sharditerator
    
def read_kinesis_streams(client,sharditerator):
    try:
        

        response = client.get_records(
            ShardIterator=sharditerator,
            Limit=50
            )
        
        for item in response['Records']:
            payload = base64.decodestring(item['Data'])
            print(payload)
        
    except Exception as error:
        print(error)
        
        
if __name__ == '__main__':
    
    streamname = ''
    sequencenumber = '49594181992117678557922090488457557123684639347849560066'
    shardid = get_shard(client,streamname)
    sharditerator = get_sharditerator(client,streamname,shardid,sequencenumber)
    read_kinesis_streams(client,sharditerator)