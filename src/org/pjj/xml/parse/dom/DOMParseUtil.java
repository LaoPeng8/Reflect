package org.pjj.xml.parse.dom;

import org.pjj.xml.parse.dom.Dog;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * XML解析工具类(使用的是DOM解析方式)
 * @author PengJiaJun
 */
public class DOMParseUtil {

    /** DOM解析   输入一个xml文件的路径(dogs.xml)  输出一个List<Dog> */
    public static List<Dog> parseXmlToList(String xmlPath) throws ParserConfigurationException, IOException, SAXException {

        List<Dog> dogs = new ArrayList<>();//将Xml解析后的一个个dog对象 放入该集合 返回

        //DOM方式解析: 入口
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();//产生一个DOM工厂实例
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();//通过工厂获取到DOM解析需要的对象

        //将一个XML解析为一个 可以用java处理的document对象    (需要一个输入流 将xml传入程序)
        Document document = documentBuilder.parse(new FileInputStream(xmlPath));

        //获取文档的所有节点
        Element element = document.getDocumentElement();

        //获取文档中所有的dog节点
        NodeList nodeList = element.getElementsByTagName("dog");
        //遍历所有的dog节点
        for(int i=0;i<nodeList.getLength();i++){
            Dog dog = new Dog();//一个dog对象 代表 XML中的一对<dog></dog>
            //根据下标获取 单个dog节点
            Element dogElement = (Element)nodeList.item(i);//本来该方法是返回一个Node类型的,但是Node类型提供的方法太少了,Element是Node的子接口,我们可以使用Element

            //获取<dog>的id属性
            int id = Integer.parseInt(dogElement.getAttribute("id"));
            dog.setId(id);

            //获取<dog>的子节点
            NodeList dogChildNodes = dogElement.getChildNodes();
            //遍历<dog>的每一个子节点
            for(int j=0;j<dogChildNodes.getLength();j++){

                /**
                 * 获取单个子节点 如<name>、<score>...(也有可能是文字、空格)
                 * 比如: <dog id="1">
                 *          文字,空格
                 *          <name>YAYA</name>
                 *          <score>100</score>
                 *          <level>10</level>
                 *      </dog>
                 * 遍历这个<dog>时 它把 这些文字和空格也当成了一个子节点 所以我们dogChildNodes.item(j);获取子节点时
                 * 其实获取到的第一个子节点(dogChildNodes.item(0)) 根本就不是子节点 而是 文字、空格
                 * 有人可能会说 一般不是没有文字嘛 <dog>下面直接就是<name> <score>等标签,虽然确实没有文字
                 * 但是有换行啊, 换行了不就相当于 <dog>\n <name></name> </dog> 吗  \n也是文字或者说字符  也是属于一个子节点的.
                 */
                Node dogChildNode = dogChildNodes.item(j);//获取单个子节点

                //只拿<xxx>这种形式的子节点   Node.ELEMENT_NODE即代表<xxx>这种类型的子节点  这样就排除了上面那个空格文字问题
                if(dogChildNode.getNodeType() == Node.ELEMENT_NODE){//获取节点的类型 如与 Node.ELEMENT_NODE类型一致 则获取value值 并进行处理.

                    /**
                     * 这样拿标签是value是错误写法:
                     *      String nodeValue = dogChildNode.getNodeValue();//获取子节点的value值  也就是 <标签>value</标签>
                     * 因为:
                     *      <标签>value值<其他标签><其他标签/></标签>
                     *      理想状态下是<标签>value</标签> 但是 <标签>内,可能也不止这一个value值,可能还有其他标签
                     *      所以要dogChildNode.getFirstChild().getNodeValue() 获取当前标签的第一个子节点的getNodeValue()
                     */
                    String nodeValue = dogChildNode.getFirstChild().getNodeValue();//获取子节点的value值

                    /** 由于<dog>的子节点有  <name>YAYA</name>、<score>100</score>、<level>10</level>
                     *  所以 需要 先用标签名与之判断 该value值是哪个标签的value值 从而好对dog对象进行正确的赋值. */
                    if ("name".equals(dogChildNode.getNodeName())){//<name></name>
                        dog.setName(nodeValue);
                    }else if ("score".equals(dogChildNode.getNodeName())){//<score></score>
                        dog.setScore(Double.parseDouble(nodeValue));
                    }else if ("level".equals(dogChildNode.getNodeName())){//<level</level>
                        dog.setLevel(Integer.parseInt(nodeValue));
                    }else{
                        //如果不是我所需要的<name>、<score>、<level> 就算获取到了 子节点的value也不做任何处理
                    }
                }
            }
            dogs.add(dog);
        }
        return dogs;
    }
}
