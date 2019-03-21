package com.android.sdk.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.text.TextUtils;

/**
 * 反射工具类。
 * @author Synaric
 *
 */
public class ReflectUtil {
	
	/**
	 * 获取指定类的方法。
	 * @param classFullName
	 * @param methodName
	 * @param parameterTypes
	 * @return
	 * @author Synaric
	 */
	public static Method getMethod(String classFullName, String methodName, Class<?>[] parameterTypes) {		
		return getDeclaredMethod(classFullName, methodName, parameterTypes);
	}
	
    public static Method getDeclaredMethod(String classFullName, String methodName, Class<?> ... parameterTypes){
    	Class<?> clz = null;
		try {
			clz = Class.forName(classFullName);
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		
		if (clz == null) {
			SDKDebug.relog("ReflectUtil.getDeclaredMethod(): class not found " + classFullName);
			return null;
		}
    	
        return getDeclaredMethod(clz, methodName, parameterTypes);  
    } 
    
    public static Method getDeclaredMethod(Class<?> clz, String methodName, Class<?> ... parameterTypes){
        Method method = null ;  
          
        for(Class<?> clazz = clz ; clazz != Object.class ; clazz = clazz.getSuperclass()) {  
            try {  
                method = clazz.getMethod(methodName, parameterTypes);
                method.setAccessible(true);
                return method ;  
            } catch (Exception e) {  
                //忽略异常，继续向父类查找
            }  
        }  
          
        return null;  
    }
	
	/**
	 * 执行方法。
	 * @param receiver
	 * @param method
	 * @param params
	 * @author Synaric
	 */
	public static void invokeMethod(Object receiver, Method method, Object[] params) {
		if (method == null) {
			throw new IllegalArgumentException("ReflectUtil.invokeMethod(): method == null");
		}
		
		try {
			method.invoke(receiver, params);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取指定类的单例。
	 * @param classFullName 需要获取单例的类全名。
	 * @param singletonMethodName 获取单例的方法名。如果为空，则使用ClassName.getInstance()获取单例。
	 * @return
	 * @author Synaric
	 */
	public static Object getClassInstance(String classFullName, String singletonMethodName) {
		if (TextUtils.isEmpty(singletonMethodName)) {
			singletonMethodName = "getInstance";
		}
		
		Method method = getMethod(classFullName, singletonMethodName, null);
		if (method == null) return null;
		
		try {
			Object result = method.invoke(null);
			return result;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
