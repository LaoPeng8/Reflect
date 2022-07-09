package org.pjj.reflect;

public class Person implements MyInterface,MyInterface2 {

    private int id;
    private String name;
    private int age;
    public String hobby;

    public Person(){
    }
    public Person(int id) {
        this.id = id;
    }
    private Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
    public Person(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    @Override
    public void interfaceMethod() {
        System.out.println("interfaceMethod....");
    }

    @Override
    public void interfaceMethod2() {
        System.out.println("interfaceMethod2....");
    }

    public static void staticMethod(){
        System.out.println("static..method...");
    }

    private int privateMethod(){
        System.out.println("private..method...");
        return 66;
    }

    private int privateMethod2(String name,int age){
        System.out.println("private..method..."+"姓名:"+name+"  年龄:"+age);
        return 88;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", hobby='" + hobby + '\'' +
                '}';
    }
}
