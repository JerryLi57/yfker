package com.lolaage.codegenerator;

import com.lolaage.codegenerator.freemarker.LineToHumpDirective;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * @description:
 * @author: lijiayu
 * @date: 2020-03-06 17:41
 **/
public class FreemarkerUtil {

    public Template getTemplate(String name) {
        try {
            // 通过Freemarker的Configuration读取相应的ftl
            Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);// 这里是对应的你使用jar包的版本号：<version>2.3.23</version>

            // configuration.setDirectoryForTemplateLoading(new File("/ftl")); //如果是maven项目可以使用这种方式
            configuration.setSharedVariable("linetohump", new LineToHumpDirective());
            // 第二个参数 为你对应存放.ftl文件的包名
            configuration.setClassForTemplateLoading(this.getClass(), "/ftl");
            Template template = configuration.getTemplate(name);

            return template;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void print(String name, Map<String, Object> root) {
        // 通过Template可以将模版文件输出到相应的文件流
        Template template = this.getTemplate(name);
        try {
            template.process(root, new PrintWriter(System.out));// 在控制台输出内容
        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 输出HTML文件
     *
     * @param name
     * @param root
     * @param outFile
     */
    public void fprint(String name, Map<String, Object> root, String outFile) {
        FileWriter out = null;
        try {
            // 通过一个文件输出流，就可以写到相应的文件中，此处用的是绝对路径
            out = new FileWriter(new File(GeneratorConf.BuilderConf.LOCALDIR + outFile));
            Template temp = this.getTemplate(name);
            temp.process(root, out);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 输出HTML文件
     *
     * @param name
     * @param root
     * @param outFile
     */
    public void fprint(String name, Map<String, Object> root, String outFile, String dir) {
        FileWriter out = null;
        try {
            File file = new File(GeneratorConf.BuilderConf.LOCALDIR + File.separator + dir + File.separator + outFile);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            // 通过一个文件输出流，就可以写到相应的文件中，此处用的是绝对路径
            out = new FileWriter(file);
            Template temp = this.getTemplate(name);

            temp.process(root, out);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
