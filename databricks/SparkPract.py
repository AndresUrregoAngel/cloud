from pyspark.sql import SQLContext
from pyspark.sql.functions import *
from pyspark.sql.types import *

sqlContext = SQLContext(sc)

df = sqlContext.sql('select * from tbl_customer')
df.write.mode('overwrite').csv('customer')

df2 = df.groupBy('C_NATIONKEY').agg(sum('C_ACCTBAL').alias('total'))
#df2.show()

#df3 = spark.read.parquet('customer')
rawData  = [('5000016,Customer#005000016,dxhaIZNzZ m0tFDyx4C yK0lgWC0R,1,11-133-656-9198,258.94'), ('5000017,Customer#005000017,Ezb2kv6UD8k5kGFtrZBkn0ok7rw2HSq1,23,33-994-414-2239,4919.68')]
rdd = sc.parallelize(rawData)


fieldhdr = ['customerId','customerName','customerAddress','customerNation','customerPhone','customerBalance']
fieldtype = [LongType(),StringType(),StringType(),LongType(),StringType(),DecimalType(6,2)]


fields = [ StructField(f,StringType(),True) for f,t in zip(fieldhdr,fieldtype)  ]
schema = StructType(fields)

lines = rdd.map(lambda line: line.split(','))
lines.collect()

df3 = spark.createDataFrame(lines,schema)
df3.show()

df.groupBy('C_NAME').agg(sum('C_ACCTBAL').alias('totalAccount'),avg('C_ACCTBAL').alias('avgAccount')).sort(desc('totalAccount')).show()
