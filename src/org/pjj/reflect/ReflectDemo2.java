package org.pjj.reflect;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Properties;

public class ReflectDemo2 {

    //获取对象的实例, 并操作对象
    public static void demo01(){
        Class<?> perClazz = null;
        try {
            perClazz = Class.forName("org.pjj.reflect.Person");

            Person person = (Person)perClazz.getConstructor().newInstance();
            person.setName("张三");
            person.setAge(23);
            System.out.println(person.getName()+","+person.getAge());

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

    //操作属性
    public static void demo02(){
        Class<?> perClazz = null;
        try {
            perClazz = Class.forName("org.pjj.reflect.Person");

            Person person = (Person)perClazz.getConstructor().newInstance();
            Field idField = perClazz.getDeclaredField("id");
            //访问的是private修饰的id, 但是private是私有的
            //所以 这个属性不需要在安全检查了,可以直接访问,,,,不加这个下面那条代码就会报异常因为不能操作private修饰的属性
            idField.setAccessible(true);
            idField.set(person,1); //person.setId(1)    第一个参数是 给哪个对象的idField代表的属性赋值  第二个参数是给属性赋什么值.
            System.out.println(person.getId());
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
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    //操作方法
    public static void demo03(){
        Class<?> perClazz = null;
        try {
            perClazz = Class.forName("org.pjj.reflect.Person");
            Person person = (Person)perClazz.getConstructor().newInstance();

            Method privateMethod = perClazz.getDeclaredMethod("privateMethod",null);//通过方法名和方法参数类型获取 方法
            privateMethod.setAccessible(true);//使其不用安全检查,可以直接访问, 不然 不能够直接使用private修饰的方法
            Object returnPrivateMethod = privateMethod.invoke(person, null);//通过调用invoke调用方法
            //第一个参数代表 是调用哪个对象的 privateMethod代表的方法, 第二个参数是方法参数
            System.out.println(returnPrivateMethod);//通过invoke调用的方法的 返回值

            System.out.println("=====================");

            Method privateMethod2 = perClazz.getDeclaredMethod("privateMethod2", String.class, int.class);//通过方法名和方法参数类型获取 方法
            privateMethod2.setAccessible(true);
            Object returnPrivateMethod2 = privateMethod2.invoke(person, "李四", 30);//通过调用invoke调用方法
            System.out.println(returnPrivateMethod2);//通过invoke调用的方法的 返回值


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

    //操作构造方法
    public static void demo04(){
        Class<?> perClazz = null;
        try {
            perClazz = Class.forName("org.pjj.reflect.Person");
            Person person = (Person)perClazz.getConstructor().newInstance();

            //获取指定的构造方法     参数为,需要获取的构造器的参数类型
            //在反射中, 根据类型获取方法时: 基本类型(int , char) 和 包装类(Integer Character) 是不同的类型
            Constructor<?> constructor = perClazz.getDeclaredConstructor(String.class,int.class);
            System.out.println(constructor);
            constructor.setAccessible(true);//因为获取到的构造方法是private 要想通过反射操作private的 就先需要设置该属性|方法|构造方法 不需要安全检查
            //因为是通过perClazz.getDeclaredConstructor(String.class,int.class);获取到的有参构造方法
            Person instance = (Person)constructor.newInstance("王五",55);//所以在newInstance时需要传值
            System.out.println(instance+"->"+instance.getName()+"-"+instance.getAge());



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

    //动态加载类名 和 方法 (通过配置文件(class.txt) 动态调用某个类的某个方法)(这样需要修改调用的类名和方法时,只需修改配置文件,就不用修改程序代码了)
    public static void demo05(){

        Properties prop = new Properties();//通过Properties加载配置文件
        Class clz = null;
        try {
            //new File时 写相对路径 也就是XXX.txt 其实是直接在项目下找,由于我的class.txt在src中所以需要这样 src/class.txt  当然也可以直接写绝对路径
            prop.load(new FileInputStream(new File("src/class.txt")));//将配置文件以流的形式 加载到Properties
            String className = (String)prop.get("className");//从配置文件(class.txt)获取类名
            String methodName = (String)prop.get("methodName");//从配置文件(class.txt)获取方法名

            clz = Class.forName(className);//通过配置文件的类名 生成该类 的反射类型
            Method method = clz.getMethod(methodName);//获取需要调用的方法
            method.invoke(clz.getConstructor().newInstance());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    //反射可以 越过泛型检查
    //虽然可以通过反射 访问private等访问修饰符不允许访问的属性/方法 , 也可以忽略忽略掉泛型的约束; 但实际开发不建议这样使用, 因为可以会造成程序的混乱.
    public static void demo06(){
        ArrayList<Integer> list = new ArrayList<>();
        list.add(123);
        list.add(3);
//        list.add("zs");

        Class listClazz = list.getClass();
        try {
            Method add = listClazz.getMethod("add", Object.class);
            add.invoke(list,"zs");  //调用add方法 用list对象, 参数为"zs"
            System.out.println(list);// [123, 3, zs]

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    //通过反射 整一个万能的set方法
    public static void demo07(){
        Person person = new Person();
        PropertiesUtils.setProperty(person,"id",1);
        PropertiesUtils.setProperty(person,"age",23);
        PropertiesUtils.setProperty(person,"name","张三");
        PropertiesUtils.setProperty(person,"hobby","左脚踩右脚————上天");
        System.out.println(person.toString());

        Student student = new Student();
        PropertiesUtils.setProperty(student,"stuNo",1);
        PropertiesUtils.setProperty(student,"stuName","王小学生");
        System.out.println(student.toString());
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
