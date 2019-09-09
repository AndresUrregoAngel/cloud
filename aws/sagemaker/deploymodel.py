
# S3 prefix
prefix = 'mlproject'

import sagemaker
import os
from sagemaker import get_execution_role

sagemaker_session = sagemaker.Session()

# Get a SageMaker-compatible role used by this Notebook Instance.
role = get_execution_role()

# checking data folder
os.makedirs('./data', exist_ok=True)

######################### Training

from sagemaker.sklearn.estimator import SKLearn

script_path = 'mlproject_build.py'

sklearn = SKLearn(
    entry_point=script_path,
    train_instance_type="ml.m5.large",
#     framework_version='0.22.0',
    role=role,
    sagemaker_session=sagemaker_session)

# S3 prefix
prefix = 'Scikit'
WORK_DIRECTORY = 'data'

train_input = sagemaker_session.upload_data(WORK_DIRECTORY, key_prefix="{}/{}".format(prefix, WORK_DIRECTORY) )

sklearn.fit({'train' : train_input})


############################ Deploying model

import numpy
from sagemaker.sklearn.estimator import SKLearn,SKLearnModel

sklearn_model = SKLearnModel(model_data="s3://bucket/model.tar.gz", role=role,
    entry_point="mlproject_build.py")


endpoint_name = 'clf-scikit-endpoint'
sklearn_model.deploy(instance_type="ml.c4.xlarge", initial_instance_count=1,endpoint_name=endpoint_name)

############################ Predict

import pandas as pd

lfile = []
with open("./data/001.txt","r") as ifile:
    content = ifile.read()

ifile.close()

print(type(content))



import boto3
import json

client = boto3.client('sagemaker-runtime')

response = client.invoke_endpoint(
    EndpointName=endpoint_name, 
#     CustomAttributes=custom_attributes, 
    ContentType="text/csv",
#     Accept=accept,
    Body=content
    )

result = response['Body']
model_prediction = json.loads(result.read())
print(model_prediction)
print(model_prediction['instances'][0]['features'])
