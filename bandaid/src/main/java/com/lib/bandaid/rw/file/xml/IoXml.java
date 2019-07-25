package com.lib.bandaid.rw.file.xml;

import android.content.Context;

import com.lib.bandaid.rw.file.utils.XmlUtil;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.dom4j.Document;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zy on 2017/7/25.
 * 读取xml里的任务
 */
public final class IoXml {

    private static DomDriver domDriver;

    private IoXml() {
    }

    /**
     * @param xmlPath
     * @return
     */
    public static <T> T readXmlFromPath(Class clazz, String xmlPath) {
        try {
            if (domDriver == null) {
                domDriver = new DomDriver();
            }
            Document document = XmlUtil.parseXmlPath(xmlPath);
            String xml = document.asXML();
            XStream xStream = new XStream(domDriver);
            xStream.processAnnotations(clazz);//应用Person类的注解
            T tt = (T) xStream.fromXML(xml);
            return tt;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     *
     * @param context
     * @param clazz
     * @param filePath
     * @param <T>
     * @return
     */
    public static <T> T readXmlFromAssets(Context context, Class clazz, String filePath) {
        try {
            if (domDriver == null) {
                domDriver = new DomDriver();
            }
            InputStream inputStream = context.getAssets().open(filePath);
            Document document = XmlUtil.parseXmlIO(inputStream);
            String xml = document.asXML();
            XStream xStream = new XStream(domDriver);
            xStream.processAnnotations(clazz);//应用Person类的注解
            T tt = (T) xStream.fromXML(xml);
            return tt;
        } catch (Exception e) {
            return null;
        }
    }

    public static String readFromXml(Class clazz, String xmlPath) {
        try {
            Document document = XmlUtil.parseXmlPath(xmlPath);
            String xml = document.asXML();
            return xml;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param xml
     * @return
     */
    public static <T> T readFromXmlString(Class clazz, String xml) {
        try {
            XStream xStream = new XStream(new DomDriver());
            xStream.processAnnotations(clazz);//应用Person类的注解
            T tt = (T) xStream.fromXML(xml);
            return tt;
        } catch (Exception e) {
            return null;
        }
    }

    public static Object readFromStream(Class c, InputStream inputStream) {
        try {
            Document document = XmlUtil.parseXmlIO(inputStream);
            String xml = document.asXML();
            XStream xStream = new XStream(new DomDriver());
            xStream.processAnnotations(c);//应用Person类的注解
            return xStream.fromXML(xml);
        } catch (Exception e) {
            return null;
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean writeToXml(Object o, String projectPath) {
        try {
            String xml = classToXml(o);
            return XmlUtil.writeXml(projectPath, xml);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean saveOrUpDate(Object o, String projectPath) {
        try {
            String xml = classToXml(o);
            return XmlUtil.writeXml(projectPath, xml);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean writeStrToXml(String xml, String projectPath) {
        try {
            return XmlUtil.writeXml(projectPath, xml);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String classToXml(Object o) {
        try {
            XStream xStream = new XStream(new DomDriver());
            xStream.autodetectAnnotations(true);
            String xml = xStream.toXML(o);
            return xml;
        } catch (Exception e) {
            return null;
        }
    }


}
