package com.reportai.www.reportapi.util;

import org.springframework.stereotype.Component;
import java.lang.reflect.Field;

@Component
public class Patcher {

    public static <T> void patch(T existingObject, T newObject) throws IllegalAccessException {
        Class<?> clazz = existingObject.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.get(newObject);
            if (value != null) {
                field.set(existingObject, value);
            }
            field.setAccessible(false);
        }
    }
}
