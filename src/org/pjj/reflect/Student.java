package org.pjj.reflect;

public class Student {

    private int stuNo;
    private String stuName;

    public void sayHi(){
        System.out.println("I am Student....");
    }

    @Override
    public String toString() {
        return "Student{" +
                "stuNo=" + stuNo +
                ", stuName='" + stuName + '\'' +
                '}';
    }
}
