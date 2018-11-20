package cz.simac.cmxdslink.cmxdata;

import cz.simac.cmxdslink.CMXDSLink;
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
        CMXDSLink.LOGGER.debug("In filterPublicFields(Field[] fields) method");
        return Arrays.asList(fields).stream().filter(f -> Modifier.isPublic(f.getModifiers())).toArray(Field[]::new);
    }

    public static CMXNotification[] Encode(String data){
        CMXDSLink.LOGGER.debug("In Encode(String data) method");
        List<CMXNotification> list = new ArrayList<>();
        JsonArray array = Parse(data);
        if(array == null) {
            CMXDSLink.LOGGER.debug("array == null");
            CMXDSLink.LOGGER.debug("leaving Encode method with null object");
            return null;
        }
        CMXDSLink.LOGGER.debug("enumerating jObjects");
        for(Object obj : array){
            JsonObject jObj = (JsonObject)obj;
            if(!jObj.contains("notificationType")) {
                CMXDSLink.LOGGER.debug("doesn't contain notificationType key");
                CMXDSLink.LOGGER.debug("leaving Encode method with null object");
                return null;
            }
            String type = jObj.get("notificationType");
            CMXNotification notification = null;
            Field[] useFields = null;
            CMXDSLink.LOGGER.debug("switch(type)");
            switch(type){
                case "movement":
                    CMXDSLink.LOGGER.debug("type == movement");
                    notification = new MovementNotification();
                    useFields = movementFields;
                    break;
                case "locationupdate":
                    CMXDSLink.LOGGER.debug("type == locationupdate");
                    notification = new LocationUpdateNotification();
                    useFields = locationUpdateFields;
                    break;
                case "association":
                    CMXDSLink.LOGGER.debug("type == association");
                    notification = new AssociationNotification();
                    useFields = associationFields;
                    break;
            }
            CMXDSLink.LOGGER.debug("setting fields of new object");
            try {
                for(Field f : useFields){
                        CMXDSLink.LOGGER.debug("field name: "+f.getName());
                        f.set(notification, jObj.get(f.getName()));
                }
            } catch(IllegalAccessException iae) { return null; }
            if(notification == null) {
                CMXDSLink.LOGGER.debug("notification == null");
                CMXDSLink.LOGGER.debug("leaving Encode method with null object");
                return null;
            }
            CMXDSLink.LOGGER.debug("adding new notification to list");
            list.add(notification);
        }
        CMXDSLink.LOGGER.debug("leaving Encode method with array of notifications");
        return list.toArray(new CMXNotification[0]);
    }

    private static JsonArray Parse(String data){
        CMXDSLink.LOGGER.debug("In Parse(String data) method");
        JSONParser parser = new JSONParser();
        try {
            CMXDSLink.LOGGER.debug("parsing data");
            final JsonObject jsonObject = (JsonObject) parser.parse(data);
            CMXDSLink.LOGGER.debug("getting notifications");
            //CMX notifications are always in array with key "notifications"
            return (JsonArray) jsonObject.get("notifications");
        } catch (Exception pe) {
            System.out.println(pe.getMessage());
            CMXDSLink.LOGGER.debug("returning null");
            return null;
        }
    }


}
