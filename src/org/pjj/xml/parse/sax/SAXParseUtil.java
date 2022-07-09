package org.pjj.xml.parse.sax;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * XML解析工具类(使用的是SAX解析方式)
 * @author PengJiaJun
 */
public class SAXParseUtil extends DefaultHandler {

    private List<Student> students = null;
    public List<Student> getStudents() {
        return students;
    }

    private String tagName;
    private Student student = null;

    /** 开始解析xml文件时 会执行该方法  (对于一个xml文件来说 只会执行一次, 开始解析xml文件当然就只有一次啦) */
    @Override
    public void startDocument() throws SAXException {
        System.out.println("开始执行SAX解析...");
        students = new ArrayList<>();
    }

    /** 结束解析xml文件时 会执行该方法 (对于一个xml文件来说 只会执行一次, 结束解析xml文件当然就只有一次啦) */
    @Override
    public void endDocument() throws SAXException {
        System.out.println("SAX解析结束...");
    }

    /**
     * 开始解析xml标签时 会执行该方法 (开始解析一个标签会执行一次 继续开始解析下一个标签也会执行一次 反复如此
     * 如<students>是一次,<student>又是一次,<id>又是一次....)
     * @param uri
     * @param localName
     * @param qName         代表当前解析标签的名字 如<student> 那么qName就是student
     * @param attributes    代表当前解析标签的属性 属性可以有多个所以是attributes 通过attributes.getValue(0)下标来区别每个属性
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        //解析到 标签名 为student 就new一个Student() 然后 获取 id属性   如果标签名不是student就不做任何处理
        if("student".equals(qName)){
            student = new Student();
            int id = Integer.parseInt(attributes.getValue(0));
            student.setId(id);
        }
        this.tagName = qName;
    }

    /** 结束解析xml标签时 会执行该方法 (结束解析一个标签会执行一次 继续结束解析下一个标签也会执行一次 反复如此
     * 如</students>是一次,</student>又是一次,</id>又是一次) */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if("student".equals(qName)){//判断一下是 哪个标签结束 如果是</student>标签结束 则将解析后的student放入list集合
            students.add(student);
        }
        this.tagName = null;//一对<student></student>解析完毕后 将tagName重置为null
    }

    /**
     * 在startElement与endElement 之间调用多次  如开始解析<student>标签 该标签内有多少子标签就会执行多少次该方法 直到结束解析</student>标签
     * 比如: <student id="1001">
     *     文字(\n),空格
     *     <name>张三</name>
     *     <age>23</age>
     *     <birthday>1966-06-06</birthday>
     *     </student>
     *     执行完startElement方法后就是解析完<student id="1001"了
     *     然后执行characters 此时获取的是<student id="1001">value</student>标签内的value,虽然表面上看起来没有value,只有三个子标签
     *     但是不要忘记了<student id="1001"> 与 子标签<name>之间是换了行的(\n)并且有空格 (所以此时获取到的<student>的值就是 "一堆空格")
     *     然后characters执行完后 执行 startElement开始解析<name> 然后执行 characters获取到<name>value</name>标签内的value
     *     然后执行endElement 然后执行startElement开始解析<age>然后执行 characters获取到<age>value</age>标签内的value
     *     然后执行endElement....
     *     最后当</birthday>执行完后 也就是 <birthday>的endElement执行完后   执行</student>也就是执行<student>endElement
     *     这样一对<student></student>就解析完毕了.
     *
     */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if(this.tagName != null){//判断是否开始解析   没开始解析tagName为null  开始解析后tagName为qName

            String data = new String(ch,start,length);

            if("name".equals(this.tagName)){
                student.setName(data);
            }
            if("age".equals(this.tagName)){
                student.setAge(Integer.parseInt(data));
            }
            if("birthday".equals(this.tagName)){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");//定义String转data时的格式
                Date birthday = null;
                try {
                    birthday = sdf.parse(data);//String -> data
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                student.setBirthday(birthday);
            }
        }
    }
}
