import json

def lambda_handler(event, context):
    # TODO implement
    
    StartEpoch ="message"
    message = 'Hello from Lambda executed by API GW! with StartEpoch:',StartEpoch
    return {
        'statusCode': 200,
        'body': json.dumps( message)
    }
