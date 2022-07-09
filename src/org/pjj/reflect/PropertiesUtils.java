package org.pjj.reflect;

import java.lang.reflect.Field;

public class PropertiesUtils {

    //做一个万能的set方法
    // person.setXxx(value); private String name;
    public static void setProperty(Object obj,String propertyName,Object value){//obj 需要赋值的对象  proName 需要赋值的属性名  value需要赋值的值
        Class<?> clz = obj.getClass();
        try {
            Field declaredField = clz.getDeclaredField(propertyName);//获取到属性名
            declaredField.setAccessible(true);
            declaredField.set(obj,value);//给属性赋值
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }
}
