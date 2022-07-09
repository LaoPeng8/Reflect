package org.pjj.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectDemo1 {

    //获取反射对象
    public static void demo01(){
        //获取反射对象(反射入口): Class  有三种方式获取
        // 1 Class.forName("全类名")
        try {
            Class<?> perClazz = Class.forName("org.pjj.reflect.Person");
            System.out.println(perClazz);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //2 类名.class
        Class preClazz2 = Person.class;
        System.out.println(preClazz2);

        //3 对象.getClass()
        Person person = new Person();
        Class preClazz3 = person.getClass();
        System.out.println(preClazz3);
    }

    //获取方法
    public static void demo02(){
        Class<?> perClazz = null;
        try {
            perClazz = Class.forName("org.pjj.reflect.Person");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //获取所有的公共的方法(1.本类 以及 父类、接口中的所有的public修饰的方法 2.符合访问修饰符规律)
        Method[] methods = perClazz.getMethods();
        for(Method method : methods) {
            System.out.println(method);
        }
        //获取本类所有的方法(1.只能是当前类,包括private 2.忽略访问修饰符限制)
        System.out.println("#############################");
        Method[] declaredMethods = perClazz.getDeclaredMethods();
        for(Method method : declaredMethods){
            System.out.println(method);
        }
    }

    //获取所有接口
    public static void demo03(){
        Class<?> perClazz = null;
        try {
            perClazz = Class.forName("org.pjj.reflect.Person");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Class<?>[] interfaces = perClazz.getInterfaces();
        for (Class clz: interfaces) {
            System.out.println(clz);
        }
    }

    //获取所有的父类
    public static void demo04(){
        Class<?> perClazz = null;
        try {
            perClazz = Class.forName("org.pjj.reflect.Person");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Class<?> superclass = perClazz.getSuperclass();
        System.out.println(superclass);
    }

    //获取所有构造方法
    public static void demo05(){
        Class<?> perClazz = null;
        try {
            perClazz = Class.forName("org.pjj.reflect.Person");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //获取所有的公共的构造方法(只能获取到public修饰的构造方法)
        Constructor<?>[] constructors = perClazz.getConstructors();
        for(Constructor constructor : constructors){
            System.out.println(constructor);
        }
        System.out.println("###############################");
        //获取所有的构造方法(包括private修饰的构造方法)
        Constructor<?>[] declaredConstructors = perClazz.getDeclaredConstructors();
        for(Constructor constructor : declaredConstructors){
            System.out.println(constructor);
        }
    }

    //获取属性
    public static void demo06(){
        Class<?> perClazz = null;
        try {
            perClazz = Class.forName("org.pjj.reflect.Person");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //获取所有的公共的属性(1.本类 以及 父类、接口中的所有的public修饰的属性)
        Field[] fields = perClazz.getFields();
        for(Field field:fields){
            System.out.println(field);
        }
        System.out.println("#########################");
        //获取本类所有的属性(1.本类中所有的属性,包括private)
        Field[] declaredFields = perClazz.getDeclaredFields();
        for(Field field : declaredFields){
            System.out.println(field);
        }
    }

    //获取当前反射所代表类(接口) 的对象(实例)
    public static void demo07(){
        Class<?> perClazz = null;
        try {
            perClazz = Class.forName("org.pjj.reflect.Person");

            Object obj = perClazz.newInstance();//jdk1.9前使用这种方式, 现在已经不推荐使用了
            Person person = (Person)obj;
            System.out.println(person);

            Object obj2 = perClazz.getConstructor().newInstance();//现在推荐使用这种方式
            Person person2 = (Person)obj;
            System.out.println(person2);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }



    public static void main(String[] args) {
//        demo01();
//        demo02();
//        demo03();
//        demo04();
//        demo05();
//        demo06();
        demo07();
    }
}
