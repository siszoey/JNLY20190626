package com.lib.bandaid.rw.file.utils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zy on 2017/7/18.
 */

public final class XmlUtil {

    private XmlUtil() {
    }

    /**
     * @param xmlPath
     * @return
     */
    public static Object readFromXml(Class c, String xmlPath) {
        try {
            Document document = parseXmlPath(xmlPath);
            String xml = document.asXML();
            XStream xStream = new XStream(new DomDriver());
            xStream.processAnnotations(c);//应用Person类的注解
            return xStream.fromXML(xml);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param inputStream
     * @return
     */
    public static Object readFromXml(Class c, InputStream inputStream) {
        try {
            Document document = parseXmlIO(inputStream);
            String xml = document.asXML();
            XStream xStream = new XStream(new DomDriver());
            xStream.processAnnotations(c);//应用Person类的注解
            return xStream.fromXML(xml);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * <p>
     * 按照物理路径解析XML
     * </p>
     *
     * @param path 物理路径
     * @return 文档对象
     * @creation 20160812
     * @author 鲁洋
     */
    public static Document parseXmlPath(String path) throws Exception {
        Document doc = null;
        try {
            doc = SAXReader.class.newInstance().read(new File(path));
        } catch (DocumentException e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        } catch (InstantiationException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
        return doc;
    }

    public static Document parseXmlIO(InputStream inputStream) throws Exception {
        Document doc = null;
        try {
            doc = SAXReader.class.newInstance().read(inputStream);
        } catch (DocumentException e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        } catch (InstantiationException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                inputStream.close();
            } catch (Exception e) {

            }
        }
        return doc;
    }

    /**
     * <p>
     * 按照物理路径解析XML
     * </p>
     *
     * @param path     物理路径
     * @param encoding 编码格式
     * @return 文档对象
     * @creation 20160812
     * @author 鲁洋
     */
    public static Document parseXmlPath(String path, String encoding) {
        Document doc = null;
        try {
            SAXReader rd = SAXReader.class.newInstance();
            rd.setEncoding(encoding);
            FileReader fr = new FileReader(path);
            while (!fr.ready()) {
            }
            doc = rd.read(fr);
            fr.close();
        } catch (DocumentException e) {
            e.printStackTrace();
            return null;
        } catch (InstantiationException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doc;
    }

    /**
     * <p>
     * 创建XML文档
     * </p>
     *
     * @param strPath    文件生成的物理路径
     * @param strContent XML内容
     * @return 是否创建成功
     * @creation 20160812
     * @author 鲁洋
     */
    public static boolean writeXml(String strPath, String strContent) {
        boolean blnFlag = false;
        try {
            File file = new File(strPath);
            FileWriter fw = new FileWriter(file);
            fw.write(strContent);
            fw.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return blnFlag;
    }

    /**
     * 输出xml文件
     *
     * @param document
     * @param filePath
     * @throws IOException
     */
    public static void writeXml(Document document, String filePath) throws IOException {
        File xmlFile = new File(filePath);
        XMLWriter writer = null;
        try {
            if (xmlFile.exists())
                xmlFile.delete();
            writer = new XMLWriter(new FileOutputStream(xmlFile), OutputFormat.createPrettyPrint());
            writer.write(document);
            writer.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    /**
     * <p>
     * 创建XML文档
     * </p>
     *
     * @param strPath    文件生成的物理路径
     * @param strContent XML内容
     * @param encoding   编码类型   列如："UTF-8"
     * @return 是否创建成功
     * @creation 20161027
     * @author liujifei
     */
    public static boolean writeXml(String strPath, String strContent, String encoding) {
        boolean blnFlag = false;
        try {
            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(strPath), encoding);
            out.write(strContent);
            out.flush();
            out.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return blnFlag;
    }

    /**
     * <p>
     * 解析XML字符串
     * </p>
     *
     * @param strXml xml字符串
     * @return 文档对象
     * @creation 20160812
     * @author 鲁洋
     */
    public static Document parseXmlStr(String strXml) {
        Document doc = null;
        try {
            /*
             * 处理XML字符串前言中有内容的问题 edit by 鲁洋 2013-05-07
			 */
            int index = strXml.indexOf("<");
            if (index > 0) {
                // 临时将前言内容写入日志
                strXml = strXml.substring(strXml.indexOf("<"), strXml.length());
            }
            doc = DocumentHelper.parseText(strXml);
        } catch (DocumentException e) {
            return null;
        }
        return doc;
    }

    /**
     * <p>
     * 获取Element对象
     * </p>
     *
     * @param doc      文档对象
     * @param strXPath XPath路径
     * @return
     * @creation 20160812
     * @author 鲁洋
     */
    public static Element getSingleElement(Document doc, String strXPath) {
        return (Element) doc.selectSingleNode(strXPath);
    }

    /**
     * <p>
     * 获取Element对象集
     * </p>
     *
     * @param doc      文档对象
     * @param strXPath XPath路径
     * @return
     * @creation 20160812
     * @author 鲁洋
     */
    @SuppressWarnings("unchecked")
    public static List<Element> getElements(Document doc, String strXPath) {
        return (List<Element>) doc.selectNodes(strXPath);
    }

    /**
     * <p>
     * 获取Element对象集
     * </p>
     *
     * @param element  元素对象
     * @param strXpath XPath路径
     * @return 元素对象集合
     * @creation 20160812
     * @author 鲁洋
     */
    @SuppressWarnings("unchecked")
    public static List<Element> getElements(Element element, String strXpath) {
        return (List<Element>) element.selectNodes(strXpath);
    }

    /**
     * <p>
     * 获取当前元素的下一级元素
     * </p>
     *
     * @param element 元素对象
     * @return 元素对象集合
     * @creation 20160812
     * @author 鲁洋
     */
    @SuppressWarnings("unchecked")
    public static List<Element> getChildrens(Element element) {
        return (List<Element>) element.elements();
    }

    /**
     * <p>
     * 获取Element对象的属性
     * </p>
     *
     * @param element      元素对象
     * @param strAttribute 属性名称
     * @return 属性值
     * @creation 20160812
     * @author 鲁洋
     */
    public static String getAttribute(Element element, String strAttribute) {
        return element.attributeValue(strAttribute);
    }

    /**
     * <p>
     * 获取Element对象的属性
     * </p>
     *
     * @param doc          文档对象
     * @param strXPath     XPath路径
     * @param strAttribute 属性名称
     * @return 属性值
     * @creation 20160812
     * @author 鲁洋
     */
    public static String getAttribute(Document doc, String strXPath, String strAttribute) {
        return ((Element) doc.selectSingleNode(strXPath)).attributeValue(strAttribute);
    }

    /**
     * <p>
     * 获取元素对象的节点值
     * </p>
     *
     * @param element 元素对象
     * @return 节点值
     * @creation 20160812
     * @author 鲁洋
     */
    public static String getText(Element element) {
        return element.getTextTrim();
    }

    /**
     * <p>
     * 获取元素对象的节点值
     * </p>
     *
     * @param doc      文档对象
     * @param strXPath XPath
     * @return
     * @creation 20160812
     * @author 鲁洋
     */
    public static String getText(Document doc, String strXPath) {
        return ((Element) doc.selectSingleNode(strXPath)).getTextTrim();
    }

    /**
     * <p>
     * 根据属性值查询元素对象
     * </p>
     *
     * @param doc       文档对象
     * @param strXPath  XPath路径
     * @param attrName  属性名称
     * @param attrValue 属性值
     * @return 元素对象集合
     * @creation 20160812
     * @author 鲁洋
     */
    public static List<Element> getElementsByAttrValue(Document doc, String strXPath, String attrName, String attrValue) {
        List<Element> list = getElements(doc, strXPath);
        List<Element> returnList = new ArrayList<Element>();
        for (Element element : list) {
            if (getAttribute(element, attrName).equals(attrValue)) {
                returnList.add(element);
            }
        }
        return returnList;
    }

    /**
     * <p>
     * 删除指定路径下指定位置的节点
     * </p>
     *
     * @param doc
     * @param strPath
     * @param seq
     * @return
     * @creation 20160812
     * @author 鲁洋
     */
    public static Document deleteElementBySeq(Document doc, String strPath, int seq) {
        List list = doc.selectNodes(strPath);
        if (null != list && list.size() > 0) {
            Element element = (Element) list.get(seq);
            boolean b = element.getParent().remove(element);
        }
        return doc;
    }

    /**
     * <p>
     * 指定节点增加一个子节点
     * </p>
     *
     * @param doc
     * @param strPath
     * @param element
     * @return
     * @creation 20160812
     * @author 鲁洋
     */
    public static Document addElement(Document doc, String strPath, Element element) {
        Element parentElement = (Element) doc.selectSingleNode(strPath);
        parentElement.add(element);

        return doc;
    }

    /**
     * <p>
     * 更新指定节点的属性
     * </p>
     *
     * @param doc
     * @param strPath
     * @param strName
     * @param strValue
     * @return
     * @creation 20160812
     * @author 鲁洋
     */
    public static Document updateElement(Document doc, String strPath, String strName, String strValue) {
        Element parentElement = (Element) doc.selectSingleNode(strPath);
        Attribute attribute = parentElement.attribute(strName);
        attribute.setValue(strValue);
        return doc;
    }

    /**
     * @param path
     * @param strXpath
     * @return
     * @throws DocumentException
     */
    @SuppressWarnings("unchecked")
    public static List<Element> getElements(String path, String strXpath) throws DocumentException {
        System.out.println(path);
        File file = new File(path);
        if (file.exists()) {
            Document document = new SAXReader().read(file);
            List<Element> lists = document.selectNodes(strXpath);
            return lists;
        } else {
            return null;
        }
    }

    /**
     * 修改指定元素Text
     *
     * @param doc
     * @param strPath
     * @param nodeText
     */
    public static void updateEleText(Document doc, String strPath, String nodeText) {
        Element element = (Element) doc.selectSingleNode(strPath);
        element.setText(nodeText);
    }

    /**
     * 修改指定元素属性
     *
     * @param doc
     * @param strPath
     * @param strName
     * @param strValue
     */
    public static void updateEleAttr(Document doc, String strPath, String strName, String strValue) {
        Element parentElement = (Element) doc.selectSingleNode(strPath);
        Attribute attribute = parentElement.attribute(strName);
        attribute.setValue(strValue);
    }

    /**
     * 修改Document名称所有节点中指定属性的只
     *
     * @param doc
     * @param strPath
     * @param attrName
     * @param attrValue
     */
    @SuppressWarnings("unchecked")
    public static void updateElesAttr(Document doc, String strPath, String attrName, String attrValue) {
        List<Element> lists = doc.selectNodes(strPath);
        for (Element e : lists) {
            Attribute attribute = e.attribute(attrName);
            if (attribute != null) {
                attribute.setValue(attrValue);
            } else {
                continue;
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static String getElesAttr(Document doc, String strPath, String attrName) {
        List<Element> lists = doc.selectNodes(strPath);
        String str = null;
        for (Element e : lists) {
            Attribute attribute = e.attribute(attrName);
            if (attribute != null) {
                str = attribute.getValue();
                break;
            } else {
                continue;
            }
        }
        return str;
    }

    /**
     * 格式化xml字符串
     *
     * @param xmlStr
     * @return
     * @throws Exception
     */
    public static String xmlFormat(String xmlStr) throws Exception {
        Document document = DocumentHelper.parseText(xmlStr);
        StringWriter writer = new StringWriter();
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("UTF-8");
        XMLWriter xmlWriter = new XMLWriter(writer, format);
        xmlWriter.write(document);
        return writer.toString();
    }

}
