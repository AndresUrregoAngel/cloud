from google.cloud import bigquery
from google.cloud.bigquery import LoadJobConfig
from google.cloud.bigquery import SchemaField

client = bigquery.Client()
dataset_ref = client.dataset('bixi')


## Create the table

schema = [
    bigquery.SchemaField('start_date', 'DATETIME', mode='NULLABLE'),
    bigquery.SchemaField('start_station_code', 'INTEGER', mode='NULLABLE'),
    bigquery.SchemaField('end_date', 'DATETIME', mode='NULLABLE'),
    bigquery.SchemaField('end_station_code', 'INTEGER', mode='NULLABLE'),
    bigquery.SchemaField('duration_sec', 'INTEGER', mode='NULLABLE'),
    bigquery.SchemaField('is_member', 'INTEGER', mode='NULLABLE')
]
table_ref = dataset_ref.table('bixitrips_2017')
table = bigquery.Table(table_ref, schema=schema)
table = client.create_table(table)  # API request

## Loading data


SCHEMA = [
    SchemaField('start_date', 'DATETIME', mode='NULLABLE'),
    SchemaField('start_station_code', 'INTEGER', mode='NULLABLE'),
    SchemaField('end_date', 'DATETIME', mode='NULLABLE'),
    SchemaField('end_station_code', 'INTEGER', mode='NULLABLE'),
    SchemaField('duration_sec', 'INTEGER', mode='NULLABLE'),
    SchemaField('is_member', 'INTEGER', mode='NULLABLE')
]
#table_ref = client.dataset('dataset_name').table('table_name')

load_config = LoadJobConfig()
load_config.skip_leading_rows = 1
load_config.schema = SCHEMA
uri = 'gs://gcp-development/OD_2017-04.csv'

load_job = client.load_table_from_uri(
    uri,
    table_ref,
    job_config=load_config)

load_job.result()

destination_table = client.get_table(table_ref)
print('Loaded {} rows.'.format(destination_table.num_rows))