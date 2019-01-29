import os
import pandas as pd
import pandas_gbq as pgbq
import pandasql as ps

os.environ["GOOGLE_APPLICATION_CREDENTIALS"]="~/.config/gcloud/application_default_credentials.json"


citydict = "SELECT iata, city,state_code,country_code,lon,lat FROM table"

projectid = " "
bucket_destination = 'gs://bucket/hstcompiled.csv'

dfcities = pgbq.read_gbq(citydict, project_id=projectid, dialect='standard')

def calculateweather(iata, city, lon, lat, projectid):
    years = [2015, 2016, 2017, 2018]

    meanquery = "select substr(measure_date,1,10) as measure_date , stats_name	 , " \
               " CASE " \
               " WHEN stats_name in ('TAVG','TMIN','TMAX','SNOW','SNWD','PRCP') THEN  ROUND(AVG(stats_value/10),2) " \
               " WHEN stats_name IN ( 'WSFG' ,'AWND')  THEN  ROUND(AVG(stats_value * 0.36),2) " \
               " ELSE ROUND(AVG(stats_value ),2) END as mean " \
               " FROM df  " \
               " group by measure_date, stats_name	" \
               " order by 1 asc"

    medianquery = " select distinct measure_date, stats_name ,  percentile_cont(stats_value, 0.5) over (partition by stats_name, measure_date) AS median  " \
                  " from `STG_Dimensions.tmp_clnweather` order by 1 desc "

    droptquery = "drop table `STG_Dimensions.tmp_clnweather`"

    frames = []
    for year in years:

        pullingquery = " select closest.id, closest.name, closest.latitude, closest.longitude, element as stats_name, value as stats_value, date as measure_date " \
                       " from `bigquery-public-data.ghcn_d.ghcnd_{}` as data right join ( " \
                       " select id, name, state, latitude, longitude, ST_DISTANCE(ST_GEOGPOINT(longitude, latitude),ST_GEOGPOINT( {}, {} )) / 1000.0 as distance " \
                       " from `bigquery-public-data.ghcn_d.ghcnd_stations` " \
                       " where longitude is not null and latitude is not null) as closest on data.id = closest.id" \
                       " where " \
                       " extract(year from data.date) =  {} " \
                       " and closest.distance <= 20 " \
                       " and element in ('TMAX','TMIN','TAVG','PRCP','SNOW','SNWD','WSFG','AWDR','AWND','TSUN')  " \
                       " OR element like 'WT%' OR element like 'WV%' " \
                       " order by closest.id, data.date ".format(year, lon, lat, year)

        df = pgbq.read_gbq(pullingquery, project_id=projectid, dialect='standard')
        pgbq.to_gbq(df, 'STG_Dimensions.tmp_clnweather', projectid, if_exists='replace')
        print("the initial df has been stored in BQ for city {}".format(city))

        """ Get the mean from the dataset """

        dfmean = ps.sqldf(meanquery, locals())

        """ Get the median from the dataset """
        dfmedian = pgbq.read_gbq(medianquery, project_id=projectid, dialect='standard')

        pgbq.read_gbq(droptquery, project_id=projectid, dialect='standard')
        print("the staging table has been dropped successfully")

        """ Get the final df outcome for median and mean """
        dfmean[['measure_date']] = dfmean[['measure_date']].astype(str)
        dfmedian[['measure_date']] = dfmedian[['measure_date']].astype(str)

        dfmerged = pd.merge(
            dfmean,
            dfmedian,
            how='left',
            left_on=['measure_date', 'stats_name'],
            right_on=['measure_date', 'stats_name']
        )

        """ Transposing the result DF """
        dfmerged = dfmerged.pivot(index='measure_date',columns='stats_name')
        dfmerged.reset_index(inplace=True)
        dfmerged.columns = ['_'.join(col).strip() for col in dfmerged.columns.values]
        dfmerged['iata'] = iata
        dfmerged.rename(columns={"measure_date_": "date"},inplace=True)
        frames.append(dfmerged)


    dfcompiled = pd.concat(frames, sort=False)


    return dfcompiled


compiledframes = []

for index, row in dfcities.iterrows():
    iata = row['iata']
    lon = row['lon']
    lat = row['lat']
    city = row['city']
    dfcompiled = calculateweather(iata, city,lon, lat, projectid)
    compiledframes.append(dfcompiled, sort=False)


dfhstweather = pd.concat(compiledframes)
destable = 'PRO_Dimensions.weather'
pgbq.to_gbq(dfhstweather, destable, projectid, if_exists='replace')
