package org.pjj.json;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.pjj.json.entity.Address;
import org.pjj.json.entity.Person;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 测试使用json
 * Map集合、JavaBean、字符串、文件  -->  Json对象
 * 生成.json文件
 * json数组   JSONArray   [json,json,json]
 * Map集合  -->  JSONArray    (map转json数组)
 *
 *
 * @author PengJiaJun
 */
public class SampleDemo01 {



    //a. Map集合 转 Json对象
    public static void demo01(){
        Map<String,String> map = new HashMap<>();
        map.put("s01","张三");
        map.put("s02","李四");
        map.put("s03","王五");

        //map -> json
        JSONObject json = new JSONObject(map);
        System.out.println(json);//{"s02":"李四","s01":"张三","s03":"王五"}   可以得到json格式    {key:value,key:value}
    }

    //b. JavaBean 转 Json对象
    public static void demo02(){
        Person person = new Person("张三",23,new Address("北京","上海"));
        //JavaBean -> json
        JSONObject json = new JSONObject(person);
        System.out.println(json);//{"address":{"schoolAddress":"上海","homeAddress":"北京"},"name":"张三","age":23}
    }

    //c. String 转 Json对象
    public static void demo03(){
        String str = "{'name':'张三','age':'23','hobby':'玩游戏'}";

        //String - json     (String转json有一个前提: 这个String得是一个json格式的 才能转json  不然 一个 String str = "abc" 怎么转json)
        JSONObject json = new JSONObject(str);
        System.out.println(json);// {"name":"张三","age":"23","hobby":"玩游戏"}
    }

    //d. 文件 转 Json对象  实际上是: 文件 -> 字符串 -> json
    public void demo04(){
        InputStream in = super.getClass().getClassLoader().getResourceAsStream("org/pjj/json/per.json");
        byte[] buf = new byte[10];
        int len = -1;
        StringBuffer sb = new StringBuffer();
        try{
            while((len=in.read(buf)) != -1){
                //byte[] -> String
                String str = new String(buf,0,len);
                sb.append(str);
            }
            System.out.println(sb);//{"address":{"hAddress":"广西","sAddress":"广东"},"name":"张三","age":23}

            //String -> json
            JSONObject jsonObject = new JSONObject(sb.toString());
            System.out.println(jsonObject);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    //d. 文件 转 Json对象    demo04()是自己将 文件变成字符串  再  将字符串变为json... 本方法是使用commons-io.jar将文件变成字符串   再将 字符串变成json
    public static void demo04Plus(){
        String msg= null;
        try {
            //调用commons-io.jar 中的  FileUtils类的方法readFileToString  将文件 转为 String
            msg = FileUtils.readFileToString(new File("src/org/pjj/json/per.json"),"utf-8");

            //String -> json
            JSONObject jsonObject = new JSONObject(msg);
            System.out.println(jsonObject);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //e. 生成.json文件
    public static void demo05(){
        //准备json数据(map-json)
        Map<String,Person> map = new HashMap<>();
        Person person1 = new Person("zs",23,new Address("广东","广西"));
        Person person2 = new Person("ls",23,new Address("北京","上海"));
        Person person3 = new Person("ww",23,new Address("丽江","大理"));
        map.put("zs",person1);
        map.put("ls",person2);
        map.put("ww",person3);

        //map -> json
        JSONObject jsonObject = new JSONObject(map);
        System.out.println(jsonObject);

        //生成json文件
        try {
//            使用JSONObject类的write方法,write方法需要一个Writer(是个抽象类)参数, 所以使用FileWriter类(Writer的子类)  FileWriter的参数 是往哪里写(往哪个文件写入)
            FileWriter fileWriter = new FileWriter("E:/IDEA/ideaProject/JavaCommonApplications/src/org/pjj/json/mapPerson.json");
            jsonObject.write(fileWriter);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //f. JSONArray      [{"name":"张三","age":23},{"classno":1,"classname":"不搞学习班"},{"schoolname":"广西偷电瓶学校","zone":"一区"}]
    public static void demo06(){
        String jsonArrayStr = "[{'name':'张三','age':23},{'classno':1,'classname':'不搞学习班'},{'schoolname':'广西偷电瓶学校','zone':'一区'}]";
        //字符串格式的json数组 -> json数组
        JSONArray jsonArray = new JSONArray(jsonArrayStr);
        System.out.println(jsonArray);

    }

    //g. map 转 json数组       Map -> JSONArray
    //对于json的类型转换,通常需要引入另一个json库    json库: json-lib     六个jar: commons-beanutils.jar commons-collections.jar commons-logging.jar ezmorph.jar json-lib-2.4-jdk15.jar
    public static void demo07(){
        Map<String,String> map = new HashMap<>();
        map.put("key1","value1");
        map.put("key2","value2");
        map.put("key3","value3");

        //冲突: JSONArray既存在于json.jar 又 存在于json-lib库  (就是说两个包中都有JSONArray这个类 使用时需要注意包名 不要弄错了)
        net.sf.json.JSONArray jsonArray = new net.sf.json.JSONArray();

        //map -> json
        jsonArray = jsonArray.fromObject(map);//jsonArray.fromObject 返回一个已经将map转换成了的 JSONArray类的 实例
        System.out.println(jsonArray);


        //貌似这个json-lib比较牛啤
        //冲突: JSONObject即存在于json.jar 又 存在于json-lib中  而上面已经 import org.json.JSONObject 所以我如果直接写 JSONObject
        // 那么就是 使用的json.jar中的JSONObject  而我们现在需要使用json-lib中的JSONObject 就需要包名.类型写全 net.sf.json.JSONObject
        net.sf.json.JSONObject jsonObject = new net.sf.json.JSONObject();
        String str = "{'name':'张三','age':'23','hobby':'玩游戏'}";
        jsonObject = jsonObject.fromObject(str);//jsonObject.fromObject(str) 会返回一个 已经转换后的对象 我们可以用一个对象接收就OK了
        System.out.println(jsonObject);

        //↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑用json-lib 将 String 转为 json↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑=====↓↓↓↓↓↓↓↓↓↓↓↓用json-lib 将 对象转为 json↓↓↓↓↓↓↓↓↓↓↓↓

        net.sf.json.JSONObject jsonObject1 = new net.sf.json.JSONObject();
        Person person = new Person("张三",23,new Address("北京","上海"));
        jsonObject1 = jsonObject1.fromObject(person);
        System.out.println(jsonObject1);

    }

    //之后, 全部使用json-lib库
    //h. JSONArray 转 Map
    public static void demo08(){
        //JSONArray -> 获取每一个json -> key:value -> map        从json数组中获取到每一个json, 每一个json就是key:value, map也是key:value

        //准备一个JSONArray数组
        String jsonArrayStr = "[{'name':'张三','age':23},{'classno':1,'classname':'不搞学习班'},{'schoolname':'广西偷电瓶学校','zone':'一区'}]";

        //String -> JSONArray
        net.sf.json.JSONArray jsonArray = new net.sf.json.JSONArray();
        jsonArray = jsonArray.fromObject(jsonArrayStr);

        //JSONArray->获取每一个json
        Map<String,Object> map = new HashMap<>();
        for(int i=0;i<jsonArray.size();i++){
            //根据下标, 获取到每一个json  (返回的json是Object类型,我们需要将Object类型的json强转为JSONObject类型的)
            net.sf.json.JSONObject jsonObject = (net.sf.json.JSONObject)jsonArray.get(i);

            //获取每一个json的key:value   ->  map
            Set<String> keys = jsonObject.keySet();//每个json的所有key    一个json可能有多个key嘛 如{"k":"v","k":"v","k":"v"}
            for (String key : keys) {//遍历每个json的所有key
                Object value = jsonObject.get(key);//根据key取value
                map.put(key,value);//放入map
            }
        }
        System.out.println(map);

    }



    public static void main(String[] args) {
//        demo01();
//        demo02();
//        demo03();
//        new SampleDemo01().demo04();
//        demo04Plus();
//        demo05();
//        demo06();
//        demo07();
        demo08();

    }
}
