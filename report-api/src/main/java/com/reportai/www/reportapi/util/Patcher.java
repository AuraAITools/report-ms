package com.reportai.www.reportapi.util;

import java.util.Collection;
import java.lang.reflect.Field;

public class Patcher {

    public static <T> void patch(T existingObject, T newObject) throws IllegalAccessException {
        Class<?> clazz = existingObject.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            Object newValue = field.get(newObject);
            if (newValue != null) {
                if (Collection.class.isAssignableFrom(field.getType())) {
                    mergeCollections(field, existingObject, newValue);
                } else {
                    field.set(existingObject, newValue);
                }
            }
            field.setAccessible(false);
        }
    }

    private static <T> void mergeCollections(Field field, T existingObject, Object newValue) throws IllegalAccessException {
        Collection<Object> existingCollection = (Collection<Object>) field.get(existingObject);
        Collection<?> newCollection = (Collection<?>) newValue;
        if (existingCollection != null) {
            existingCollection.addAll(newCollection);
        } else {
            field.set(existingObject, newCollection);
        }
    }
}


