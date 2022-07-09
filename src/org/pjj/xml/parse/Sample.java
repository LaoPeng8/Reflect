package org.pjj.xml.parse;

import org.pjj.xml.parse.dom.DOMParseUtil;
import org.pjj.xml.parse.dom.Dog;
import org.pjj.xml.parse.sax.SAXParseUtil;
import org.pjj.xml.parse.sax.Student;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * 测试XML解析
 *      测试了DOM解析方式 与 SAX解析方式
 * @author PengJiaJun
 */
public class Sample {
    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {
        //DOM解析
//        List<Dog> dogs = DOMParseUtil.parseXmlToList("src/org/pjj/xml/parse/dom/dogs.xml");
//        System.out.println(dogs);


        //SAX解析
        //通过SAXParserFactory.newInstance()获取到工厂对象,再用工厂对象.newSAXParser()获取到SAXParser
        SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
        InputStream in = Sample.class.getClassLoader().getResourceAsStream("org/pjj/xml/parse/sax/student.xml");
        SAXParseUtil saxParse = new SAXParseUtil();
        parser.parse(in,saxParse);//第一个参数 需要解析的xml文件,  第二个参数 我们之前写的 解析器(SAXParseUtil)
        List<Student> students = saxParse.getStudents();
        System.out.println(students);
    }
}
