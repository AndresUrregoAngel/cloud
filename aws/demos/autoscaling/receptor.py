import boto3
import socket
import sys

# Create a TCP/IP socket
sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)


# Bind the socket to the port
server_address = ('0.0.0.0', 3490)
print('starting up on {}'.format(server_address))
sock.bind(server_address)
sock.listen(1)


client = boto3.client('s3')

while True:
    # Wait for a connection
    print('waiting for a connection')
    connection, client_address = sock.accept()
    
    print('connection from', client_address)

    # Receive the data in small chunks and retransmit it
    while True:
        data = connection.recv(16)
        print('received {}'.format(data))
        response = client.put_object(
            Bucket='bucket',
            Body=data,
            Key='transfer/sample.txt',
            StorageClass='ONEZONE_IA'
            )
        print(response)