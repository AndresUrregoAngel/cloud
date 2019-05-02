package packagename;


import com.google.api.services.bigquery.model.*;
import com.google.gson.*;
import com.google.gson.JsonObject;
import org.apache.avro.data.*;
import bncingestion.bqfieldschemabuilder;


import java.util.*;

public class kafkaconsumer {


    public static List<String> validating_payload(String payload){


        Gson g = new Gson();
        List<String> headers = Arrays.asList("employeeId","name","phonenumber","beer","company","address");
        ArrayList content = new ArrayList();
        JsonElement jelem = g.fromJson(payload,JsonElement.class);
        JsonObject jobj = jelem.getAsJsonObject();


        for (int i = 0;i< headers.size();i++){

            if (headers.get(i).contains("address")){

                String address = jobj.get("address").getAsString();
                JsonElement jeaddress = g.fromJson(address, JsonElement.class);
                JsonObject jaddress = jeaddress.getAsJsonObject();
                content.add(jaddress.get("street").getAsString());
                content.add(jaddress.get("zipcode").getAsString());

            } else{
                content.add(jobj.get(headers.get(i)).getAsString());
            }


        }

        return content;

    }


    public static TableSchema bqSchema (){

        bqfieldschemabuilder tableSchemaBuilder = new bqfieldschemabuilder();


        tableSchemaBuilder.stringField("employeeId");
        tableSchemaBuilder.stringField("name");
        tableSchemaBuilder.stringField("phonenumber");
        tableSchemaBuilder.stringField("beer");
        tableSchemaBuilder.stringField("company");
        tableSchemaBuilder.stringField("address_street");
        tableSchemaBuilder.stringField("address_zipcode");

        return tableSchemaBuilder.schema();
    }


}
