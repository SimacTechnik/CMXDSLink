package cz.simac.cmxdslink.cmxdata;

import cz.simac.cmxdslink.CMXDSLink;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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
        JSONArray array = Parse(data);
        if(array == null) {
            CMXDSLink.LOGGER.debug("array == null");
            CMXDSLink.LOGGER.debug("leaving Encode method with null object");
            return null;
        }
        CMXDSLink.LOGGER.debug("enumerating jObjects");
        for(Object obj : array){
            JSONObject jObj = (JSONObject)obj;
            if(!jObj.containsKey("notificationType")) {
                CMXDSLink.LOGGER.debug("doesn't contain notificationType key");
                CMXDSLink.LOGGER.debug("leaving Encode method with null object");
                return null;
            }
            String type = (String) jObj.get("notificationType");
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
            FillFields(notification, useFields, jObj);
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

    private static void FillFields(Object obj, Field[] useFields, JSONObject jObj) {
        try {
            for(Field f : useFields){
                CMXDSLink.LOGGER.debug("field name: "+f.getName());
                Class<?> cls = f.getType();
                if(cls.isAssignableFrom(Number.class) || cls.isAssignableFrom(String.class) || cls.isAssignableFrom(Boolean.class)) {
                    CMXDSLink.LOGGER.debug("simple type");
                    f.set(obj, jObj.get(f.getName()));
                }
                else if(cls.isArray()) {
                    CMXDSLink.LOGGER.debug("array");
                    JSONArray arr = (JSONArray) jObj.get(f.getName());
                    CMXDSLink.LOGGER.debug("got JSONArray");
                    Object[] arrayObj = (Object[]) Array.newInstance(cls.getComponentType(), arr.size());
                    CMXDSLink.LOGGER.debug("created new array of objects");
                    for(int i = 0; i < arr.size(); i++) {
                        arrayObj[i] = arr.get(i);
                    }
                    CMXDSLink.LOGGER.debug("assigned to array");
                    f.set(obj, arrayObj);
                    CMXDSLink.LOGGER.debug("set the obj");
                }
                else {
                    CMXDSLink.LOGGER.debug("object: "+cls.getName());
                    try {
                        Object newObj = cls.getConstructor().newInstance();
                        Field[] fields = filterPublicFields(cls.getDeclaredFields());
                        FillFields(newObj, fields, (JSONObject)jObj.get(f.getName()));
                    } catch (NoSuchMethodException | InstantiationException | InvocationTargetException ignore) {
                        return;
                    }
                }
            }
        } catch(IllegalAccessException iae) { return; }
    }

    private static JSONArray Parse(String data){
        CMXDSLink.LOGGER.debug("In Parse(String data) method");
        JSONParser parser = new JSONParser();
        try {
            CMXDSLink.LOGGER.debug("parsing data");
            final JSONObject jsonObject = (JSONObject) parser.parse(data);
            CMXDSLink.LOGGER.debug("getting notifications");
            //CMX notifications are always in array with key "notifications"
            return (JSONArray) jsonObject.get("notifications");
        } catch (ParseException pe) {
            CMXDSLink.LOGGER.debug("returning null");
            return null;
        }
    }


}
