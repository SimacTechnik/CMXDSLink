package cz.simac.cmxdslink.cmxdata;

import org.dsa.iot.dslink.util.json.JsonArray;
import org.dsa.iot.dslink.util.json.JsonObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CMXNotificationParser {
    private static Field[] associationFields = filterPublicFields(AssociationNotification.class.getDeclaredFields());
    private static Field[] locationUpdateFields = filterPublicFields(LocationUpdateNotification.class.getDeclaredFields());
    private static Field[] movementFields = filterPublicFields(MovementNotification.class.getDeclaredFields());

    private static Field[] filterPublicFields(Field[] fields){
        return Arrays.asList(fields).stream().filter(f -> Modifier.isPublic(f.getModifiers())).toArray(Field[]::new);
    }

    public static CMXNotification[] Encode(String data){
        List<CMXNotification> list = new ArrayList<>();
        JsonArray array = Parse(data);
        if(array == null)
            return null;
        for(Object obj : array){
            JsonObject jObj = (JsonObject)obj;
            if(!jObj.contains("notificationType"))
                return null;
            String type = jObj.get("notificationType");
            CMXNotification notification = null;
            Field[] useFields = null;
            switch(type){
                case "movement":
                    notification = new MovementNotification();
                    useFields = movementFields;
                    break;
                case "locationupdate":
                    notification = new LocationUpdateNotification();
                    useFields = locationUpdateFields;
                    break;
                case "association":
                    notification = new AssociationNotification();
                    useFields = associationFields;
                    break;
            }
            try {
                for(Field f : useFields){
                        f.set(notification, jObj.get(f.getName()));
                }
            } catch(IllegalAccessException iae) { return null; }
            if(notification == null)
                return null;
            list.add(notification);
        }
        return list.toArray(new CMXNotification[0]);
    }

    private static JsonArray Parse(String data){
        JSONParser parser = new JSONParser();
        try {
            final JsonObject jsonObject = (JsonObject) parser.parse(data);
            //CMX notifications are always in array with key "notifications"
            return (JsonArray) jsonObject.get("notifications");
        } catch (ParseException pe) {
            return null;
        }
    }


}
