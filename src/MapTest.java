import com.mongodb.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.sql.SQLDataException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vsfmqueen on 07.12.2014.
 */


public class MapTest {
    private static final Integer NEAREST_LOC_COUNT = 5;
    private static final String GEO_URL="http://maps.googleapis.com/maps/api/geocode/json?language=en&address=";
    private static final String DISTANCE_URL="http://maps.googleapis.com/maps/api/distancematrix/json?origins=%s&destinations=%s&language=en&mode=%s";


    public static void main(String...args) throws IOException {
        ArrayList testAray = new ArrayList();
        try{
            String testAddess =  URLEncoder.encode("5+проспект Дзержинского+Минск", "utf-8");
            Location location = getGoogleMapsLocation(testAddess);
            //saveLocation(location);
            List<Location> nearestLocations = getNearestLocations(location.getLocations());
            System.out.println(distance);
        }catch (UnsupportedEncodingException e){
            throw new IOException();
          //  e.printStackTrace();
        }
    }

    private static Location getGoogleMapsLocation(String officeAddress){
        Location location = new Location();
        String googleInfo = getGoogleMapInfo(GEO_URL+officeAddress);

        try{
            JSONObject response = new JSONObject(googleInfo.toString());
            JSONObject results = response.getJSONArray("results").getJSONObject(0);
            JSONObject jsonLocation = results.getJSONObject("geometry").getJSONObject("location");

            ArrayList<Double> coords = new ArrayList<Double>();
            coords.add(jsonLocation.getDouble("lng"));
            coords.add(jsonLocation.getDouble("lat"));

            String address = results.getString("formatted_address");

            location.setAddress(address);
            location.setLocations(coords);
        }catch(JSONException e){
            e.printStackTrace();
        }
        return location;
    }

    private static Distance getDistance(Location origin, Location destination, String mode){
        String origCoords = origin.getStringLocations();
        String destCoords = destination.getStringLocations();

        Distance distance = new Distance();

        String distanceUrl = String.format(DISTANCE_URL, origCoords, destCoords, mode);
        String googleInfo = getGoogleMapInfo(distanceUrl);

        try{
            JSONObject response = new JSONObject(googleInfo.toString());
            JSONObject results = response.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0);

            distance.setDistance(results.getJSONObject("distance").getString("text"));
            distance.setDuration(results.getJSONObject("duration").getString("text"));

        }catch(JSONException e){
            e.printStackTrace();
        }

        return distance;
    }

   // db.locations.ensureIndex({"location":"2d"});
    //db.locations.find({"location": {$near: [27.682972, 53.945858]}})

    private static List<Location> getNearestLocations(List<Double> array){
        DBCollection collection = getLocationCollection();
        DBObject cond = new BasicDBObject("$near", array);
        DBObject loc = new BasicDBObject("location", cond);
        DBCursor cursor = collection.find(loc).limit(NEAREST_LOC_COUNT);

        ArrayList<Location> locations = new ArrayList<Location>(NEAREST_LOC_COUNT);

        //hard porn

        while(cursor.hasNext()) {
            DBObject object = cursor.next();
            String address = (String) object.get("address");
            List<Double> coords = (List<Double>) object.get("location");
            Location location = new Location(address, coords);
            locations.add(location);
        }

        return locations;
    }

    private static DBCollection getLocationCollection() {
        DBCollection collection = null;
        try{
            Mongo mongo = new Mongo("localhost", 27017);
            DB school = mongo.getDB("test");
            collection = school.getCollection("locations");
        } catch(UnknownHostException e){
            e.printStackTrace();
        }
        return collection;
    }

    private static void saveLocation(Location location){
        BasicDBObject object = new BasicDBObject().append("address", location.getAddress()).append("location", location.getLocations());
        DBCollection collection = getLocationCollection();
        collection.save(object);
    }

    private static String getGoogleMapInfo(String stringUrl) {
        HttpURLConnection conn = null;
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        try {
            URL url = new URL(stringUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.connect();

            int status = conn.getResponseCode();

            if (status == 200) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conn.disconnect();
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    private static void exception(){
        throw new NullPointerException();
    }
}
