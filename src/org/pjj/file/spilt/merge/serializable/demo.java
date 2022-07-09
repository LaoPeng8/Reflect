package org.pjj.file.spilt.merge.serializable;

import org.pjj.file.spilt.merge.serializable.entity.Person;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 序列化流与反序列化流
 * ObjectOutputStream与ObjectInputStream         被序列化和反序列化的类 必须实现了Serializable接口
 * 之前在MergeFile.java中 不是写文件合并嘛,然后有个配置文件,里面写了原文件名,被拆分文件的个数,还有一个使用次数
 * 然后用户使用 记事本 就是打开.properties的配置文件了, 不是很好. 我们想要用记事本打开里面是乱码或者字节码什么的,反正用户看不明白就完事
 * 现在我们可以使用 序列化流 将原本存放在.properties配置文件的属性 存到一个对象中, 然后将该对象 写入硬盘,这样用记事本打开就是不是直接的字符串了.
 * @author PengJiaJun
 */
public class demo {
    public static void main(String[] args) {
//        writeObject();
//        readObject();
        writeObjectListAndReadObjectList();
    }

    //测试 writeObject()方法  ObjectOutputStream序列化流    将对象从内存 写入 硬盘
    public static void writeObject(){
        Person person = new Person("张三",23);
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream("E:/IDEA/ideaProject/JavaCommonApplications/data/splitDir/per.obj"));
            oos.writeObject(person);//将对象写入硬盘
            oos.close();//close()内部调用了 flush() 的, 不用我们写flush()
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //测试 readObject()方法 ObjectInputStream反序列化流      将对象从硬盘 读到 内存
    public static void readObject(){
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream("E:/IDEA/ideaProject/JavaCommonApplications/data/splitDir/per.obj"));
            Person person = (Person)ois.readObject();
            System.out.println("姓名: "+person.getName()+"\r\n"+"年龄: "+person.getAge());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 序列化流写入 和 读取 都必须按照顺序来  比如写入时 是 A->b->c的顺序写入的  那么读取时 就必须也 A->B->C的顺序读取
     *  那么如果我们写入多个对象 然后读取 忘记了顺序怎么办?
     *      我们可以在写入时 将多个读写 放入一个List集合  然后将 List集合 写入硬盘  这样读取时也方便一些 直接读取List集合 不用担心顺序
     */
    public static void writeObjectListAndReadObjectList(){
        Person personA = new Person("张三",23);
        Person personB = new Person("李四",41);
        Person personC = new Person("王五",55);
        List<Person> list = new ArrayList<>();
        list.add(personA);
        list.add(personB);
        list.add(personC);
        ObjectOutputStream oos = null;
        try {
            oos = new ObjectOutputStream(new FileOutputStream("E:/IDEA/ideaProject/JavaCommonApplications/data/splitDir/listPer.obj"));
            oos.writeObject(list);
            oos.close();//close()内部调用了 flush() 的, 不用我们写flush()
        } catch (IOException e) {
            e.printStackTrace();
        }

        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream("E:/IDEA/ideaProject/JavaCommonApplications/data/splitDir/listPer.obj"));
            List<Person> listResult = (List<Person>)ois.readObject();
            for (Person temp : listResult) {
                System.out.println("姓名: "+temp.getName()+",年龄: "+temp.getAge());
            }
            ois.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}


