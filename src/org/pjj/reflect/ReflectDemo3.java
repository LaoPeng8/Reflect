package org.pjj.reflect;

import java.lang.reflect.Array;
import java.util.Scanner;

/**
 *     操作动态数组
 *     String[] str = new String[3];
 *
 */
public class ReflectDemo3 {

    //通过反射操作动态一维数组
    public static void array1() throws ClassNotFoundException {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入数组的类型");//需要这样写 java.lang.String 要不然Class.forName()找不到类
        String type = sc.next();
        System.out.println("请输入数组的长度");
        int num = sc.nextInt();

        Class clz = Class.forName(type);
        Object arr = Array.newInstance(clz, num);

        //给数组的元素赋值      三个参数分别对应于   给哪个数组赋值     给数组的下标为几的元素赋值      赋什么值
        Array.set(arr,0,"张三");
        Array.set(arr,1,"李四");
        Array.set(arr,2,"王五");

        //取值        三个参数分别对应于   从哪个数组取值     从数组的下标为几的地方取值
        System.out.println(Array.get(arr,0)+"---"+Array.get(arr,1)+"---"+Array.get(arr,2));

    }

    //通过反射操作动态二维数组
    public static void array2(){
        Class clz = Integer.TYPE;//数组类型为int
        //数组长度 3,3
        int dim[] = {3,3};
        Object arr = Array.newInstance(clz, dim);//Array.newInstance(clz,3,3) 也是一样的 第二个参数是可变参数 可以直接写数组 也可以用数组

        //从二维数组中获取一行(第二行)
        Object arr2 = Array.get(arr, 2);

        //给二维数组的  第二行 的 第一列 赋值为999
        Array.set(arr2,1,999);

        //取值
        System.out.println(Array.get(arr2,1));

    }

    public static void main(String[] args) throws Exception {
//        array1();
        array2();
    }
}
