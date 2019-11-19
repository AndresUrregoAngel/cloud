select V:city.country::string,extract(year from t),year(t) from "SNOWFLAKE_SAMPLE_DATA"."WEATHER"."DAILY_14_TOTAL" limit 5;

create or replace pipe "DA_POC".public.mypipe 
  auto_ingest=true as
  copy into "DA_POC"."PUBLIC"."citiepipe"
    from @personalaws
  file_format = (type = 'PARQUET');


select * from citiesf;

SELECT $1:continent::string,$1:country.city.bag[0].array_element::string from @personalaws/canada/cities.parquet
(FILE_FORMAT => PARQUETPARSER) limit 1;


CREATE OR REPLACE TABLE "PUBLIC"."citiepipe"
AS
SELECT city from "DA_POC".public.citiesf limit 1;

SELECT * FROM "PUBLIC"."citiepipe";

DESCRIBE PIPE mypipe;

undrop table "PUBLIC"."citiepipe";


-- Snowpipe transformation
CREATE OR REPLACE TABLE "DA_POC"."PUBLIC"."citiepipetr"
AS
SELECT $1:continent::string as continent,$1:country.city.bag[0].array_element::string as city from @personalaws/canada/cities.parquet
(FILE_FORMAT => PARQUETPARSER) limit 1;



create or replace pipe "DA_POC"."PUBLIC"."pipetransform"
    auto_ingest=true as
    copy into "DA_POC"."PUBLIC"."citiepipetr"(continent,city)
    from (SELECT t.$1:continent::string as continent,t.$1:country.city.bag[0].array_element::string as city from @personalaws t)
    file_format = (format_name = PARQUETPARSER );
    
SHOW PIPES;

select * from  "DA_POC"."PUBLIC"."citiepipetr";


--unloading 

copy into @personalaws/unload/
    FROM ( SELECT * FROM "SNOWFLAKE_SAMPLE_DATA"."TPCDS_SF100TCL"."CUSTOMER_DEMOGRAPHICS")
    FILE_FORMAT = (TYPE = 'CSV')
    OVERWRITE = TRUE
    MAX_FILE_SIZE = 10000000;
    
    
copy into @personalaws/unload/
    FROM ( select $1:continent::string from @personalaws/canada/cities.parquet
     (FILE_FORMAT=>'PARQUETPARSER'))
    FILE_FORMAT = (TYPE = 'PARQUET')
    OVERWRITE = TRUE
    MAX_FILE_SIZE = 10000000;


select $1:continent::string from @personalaws/canada/cities.parquet
     (FILE_FORMAT=>'PARQUETPARSER');
    
INSERT INTO "DA_POC"."PUBLIC"."CITIESF"
SELECT * FROM "DA_POC"."PUBLIC"."CITIESF" WHERE CITY IS NULL;

    
show global accounts;

select current_region;

show streams;
describe stream mystream;
show tasks;

SELECT * FROM mystream
    WHERE METADATA$ACTION = 'INSERT'
