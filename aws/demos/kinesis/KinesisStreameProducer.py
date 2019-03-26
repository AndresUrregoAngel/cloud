import boto3
from datetime import datetime
import base64

ts = datetime.now()
recordtime = '{}{:02d}{:02d}{}{}{}'.format(ts.year,ts.month,ts.day,ts.hour,ts.minute,ts.second)

client = boto3.client('kinesis')


def encoding_message(message):
    try:
        mencoded = base64.encodestring(message)
    except Exception as error:
        print(error)
    else:
        return  mencoded
        
def send_messages(client,recordtime,streamname):
    try:
        n = 1  
        while n > 0:
            
            message = "this is the message number {:02d} at {}".format(n,recordtime)
            payload = encoding_message(message)
            response = client.put_record(
                StreamName=streamname,
                Data=payload,
                PartitionKey='pkstreaming{}'.format(n)
                #ExplicitHashKey='string',
                #SequenceNumberForOrdering='string'
            )
            
           
            print('the message has been posted and the response code from the streaming is: {} with sequence number {}'.
            format(response['ResponseMetadata']['HTTPStatusCode'],response['SequenceNumber']))
            n+=1
            
            if n== 10:
                break
            
    except Exception as error:
        print(error)

if __name__ == '__main__':
    
    streamname = ''
    send_messages(client,recordtime,streamname)