/**
 * @author dingdj
 * Date:2013-9-26下午4:26:22
 *
 */
package com.ddj.commonkit;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.util.Log;

/**
 * 反射类
 */
public class ReflectUtil {
	
	private static final String TAG = "ReflectUtil";

	/**
	 * 根据实例获取属性值
	 * @param instance
	 * @param fieldName
	 * @return Object
	 */
	public static Object getFieldValueByFieldName(Object instance,
			String fieldName) {
		Field[] fields = instance.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (fieldName.equals(field.getName())) {
				try {
					return field.get(instance);
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
		}
		return null;
	}

	/**
	 * 调用对象的某个方法
	 * @param method
	 * @param obj
	 * @param args
	 * @return Object
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentExceptio
	 */
	public static Object invokeMethod(Method method, Object obj, Object... args)
			 throws IllegalArgumentException, IllegalAccessException,
			 InvocationTargetException {
		return method.invoke(obj, args);
	}

}
