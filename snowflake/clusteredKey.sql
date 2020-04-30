--clustering 

CREATE TABLE "DA_POC"."PUBLIC"."CATALOG_RETURNS"
AS
SELECT * FROM "SNOWFLAKE_SAMPLE_DATA"."TPCDS_SF100TCL"."CATALOG_RETURNS" limit 1000000;

SET USE_CACHED_RESULT = FALSE;

SELECT * FROM "DA_POC"."PUBLIC"."CATALOG_RETURNS";

SELECT CR_ITEM_SK,COUNT(*)
FROM "DA_POC"."PUBLIC"."CATALOG_RETURNS"
GROUP BY CR_ITEM_SK
ORDER BY 2 DESC;

SELECT *  FROM "DA_POC"."PUBLIC"."CATALOG_RETURNS"
WHERE CR_ITEM_SK =346459; --222 ms without clustered key

ALTER TABLE "DA_POC"."PUBLIC"."CATALOG_RETURNS" CLUSTER BY (CR_ITEM_SK);

USE DATABASE "DA_POC";
SELECT SYSTEM$CLUSTERING_INFORMATION ('CATALOG_RETURNS');

SELECT SYSTEM$CLUSTERING_DEPTH('CATALOG_RETURNS','CR_ITEM_SK');

SELECT *  FROM "DA_POC"."PUBLIC"."CATALOG_RETURNS"
WHERE CR_ITEM_SK =346459; -- 176 ms with the clustered key