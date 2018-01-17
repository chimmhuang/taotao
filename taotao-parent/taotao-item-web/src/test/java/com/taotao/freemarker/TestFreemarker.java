package com.taotao.freemarker;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.*;

public class TestFreemarker {

    @Test
    public void testFreemarker() throws Exception{
        //1.创建一个模板文件
        //WEB/INF/ftl/hello.ftl
        //2.创建一个Configuration对象
        Configuration configuration = new Configuration(Configuration.getVersion());
        //3.设置模板所在的路径
        configuration.setDirectoryForTemplateLoading(new File("G:/IDEA_workspace/taotao/taotao-parent/taotao-item-web/src/main/webapp/WEB-INF/ftl"));
        //4.设置模板的字符集，一般UTF-8
        configuration.setDefaultEncoding("utf-8");
        //5.使用Configuration对象，加载一个模板文件，需要指定模板文件的文件名
//        Template template = configuration.getTemplate("hello.ftl");
        Template template = configuration.getTemplate("student.ftl");
        //6.创建一个数据集，可以是pojo，可以是map，推荐使用map
        Map data = new HashMap();
        data.put("hello","hello freemarker");
        Student student = new Student(1,"小米",11,"银石广场");
        data.put("student",student);
        List<Student> studentList = new ArrayList<>();
        studentList.add(new Student(1,"小米1",111,"银石广场1"));
        studentList.add(new Student(2,"小米2",112,"银石广场2"));
        studentList.add(new Student(3,"小米3",113,"银石广场3"));
        studentList.add(new Student(4,"小米4",114,"银石广场4"));
        studentList.add(new Student(5,"小米5",115,"银石广场5"));
        studentList.add(new Student(6,"小米6",116,"银石广场6"));
        studentList.add(new Student(7,"小米7",117,"银石广场7"));
        data.put("stuList",studentList);
        //日期类型的处理
        data.put("date",new Date());
//        data.put("val","哈喽");
        //7.创建一个Writer对象，指定输出文件的路径及文件名
        Writer out = new FileWriter(new File("G:/IDEA_workspace/taotao/freemarker/out/student.html"));
        //8.使用模板对象的process方法，输出文件
        template.process(data,out);
        //9.关闭流
        out.close();
    }
}
