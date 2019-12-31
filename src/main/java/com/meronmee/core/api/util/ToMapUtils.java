package com.meronmee.core.api.util;

import java.lang.reflect.Field;

import com.meronmee.core.api.annotation.ToMap;

public class ToMapUtils {
	public static boolean isToMap(Field field){
		return field.isAnnotationPresent(ToMap.class);
	}
		
	public static boolean ignore(Field field){
		if(!field.isAnnotationPresent(ToMap.class)){
			return false;
		}
		ToMap toMap = field.getAnnotation(ToMap.class);
		return toMap.ignore();
	}
	
	public static boolean longtime(Field field){
		if(!field.isAnnotationPresent(ToMap.class)){
			return false;
		}
		ToMap toMap = field.getAnnotation(ToMap.class);
		return toMap.longtime();
	}	
	public static boolean date(Field field){
		if(!field.isAnnotationPresent(ToMap.class)){
			return false;
		}
		ToMap toMap = field.getAnnotation(ToMap.class);
		return toMap.date();
	}
	public static boolean time(Field field){
		if(!field.isAnnotationPresent(ToMap.class)){
			return false;
		}
		ToMap toMap = field.getAnnotation(ToMap.class);
		return toMap.time();
	}
}