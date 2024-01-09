package com.xyhc.cms.utils;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * xml解析工具类
 *
 * @author apollo
 * @since 2022-07-30
 */
public class XMLHelper {
    DocumentBuilderFactory factory = null;
    DocumentBuilder builder = null;
    Document document = null;
    Element element = null;

    public XMLHelper(String xmlStr) {
        factory = DocumentBuilderFactory.newInstance();
        try {
            builder = factory.newDocumentBuilder();
            document = builder.parse(new ByteArrayInputStream((xmlStr.getBytes("GBK"))));
            element = document.getDocumentElement();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public XMLHelper(String xmlStr, String encode) {
        factory = DocumentBuilderFactory.newInstance();
        try {
            builder = factory.newDocumentBuilder();
            document = builder.parse(new ByteArrayInputStream((xmlStr.getBytes(encode))));
            element = document.getDocumentElement();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析xml返回所需key下的所有元素的文本值(只有一个名字为key的元素)
     *
     * @param key element的标签名
     * @return key元素的所有子集元素的文本值
     */
    public Map<String, String> readXMLText(String key) {
        Map<String, String> map = new HashMap<String, String>();
        NodeList recordNodes = element.getElementsByTagName(key);
        NodeList childNodes = null;
        Node node = null;
        node = recordNodes.item(0);
        childNodes = node.getChildNodes();//得到子元素
        map = new HashMap<String, String>();
        for (int j = 0; j < childNodes.getLength(); j++) {
            if (childNodes.item(j).getNodeType() == Node.ELEMENT_NODE) {//必须是element类型的
                map.put(childNodes.item(j).getNodeName(), childNodes.item(j).getFirstChild() == null ? "" : childNodes.item(j).getFirstChild().getNodeValue());
            }
        }
        return map;
    }

    /**
     * 解析xml
     *
     * @param key element的标签名
     * @return key元素的所有子集元素的文本值
     */
    public String readXMLByName(String key) {
        return element.getFirstChild().getNodeValue();
    }

    /**
     * 解析xml 获得最低
     *
     * @param key element的标签名
     * @return key元素的所有子集元素的文本值
     */
    public String readByName(String key) {
        NodeList recordNodes = element.getElementsByTagName(key);
        String xmlText = recordNodes.item(0).getFirstChild().getNodeValue();
        return xmlText;
    }

    /**
     * 解析xml
     *
     * @param key element的标签名
     * @return key元素的所有子集元素的文本值
     */
    public Map<String, String> readXMLMapByName(String key) {
        NodeList list = element.getChildNodes();
        Map<String, String> map = new HashMap<String, String>();
        for (int i = 0; i < list.getLength(); i++) {
            if (list.item(i) == null || list.item(i).getFirstChild() == null) {
                continue;
            } else {
                map.put(list.item(i).getNodeName(), list.item(i).getFirstChild().getNodeValue());
            }
        }
        return map;
    }

    /**
     * 解析xml返回所需xml下所有元素的列表(有多个名字为key的元素)
     *
     * @param key element的标签名
     * @return key元素的所有子集元素的文本值集合
     */
    public List<Map<String, String>> readXMLList(String key) {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Map<String, String> map = null;

        NodeList recordNodes = element.getElementsByTagName(key);
        NodeList childNodes = null;
        Node node = null;
        for (int i = 0; i < recordNodes.getLength(); i++) {
            node = recordNodes.item(i);
            childNodes = node.getChildNodes();//得到子元素
            map = new HashMap<String, String>();
            for (int j = 0; j < childNodes.getLength(); j++) {
                if (childNodes.item(j).getNodeType() == Node.ELEMENT_NODE) {//必须是element类型的
                    map.put(childNodes.item(j).getNodeName(), childNodes.item(j).getFirstChild() == null ? "" : childNodes.item(j).getFirstChild().getNodeValue());
                }
            }
            list.add(map);
        }
        return list;
    }

//    public static void main(String[] arg) {
//
//        StringBuffer sb = new StringBuffer();
//        String xml = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg><appid><![CDATA[wxd88ec4864ec6b776]]></appid><mch_id><![CDATA[1244143202]]></mch_id><nonce_str><![CDATA[LLuqI6C2zr8sVsMF]]></nonce_str><sign><![CDATA[C10BABFB3025CBB6C861ED7050CF26E0]]></sign><result_code><![CDATA[SUCCESS]]></result_code><transaction_id><![CDATA[1004920996201506130247214508]]></transaction_id><out_trade_no><![CDATA[ZY20150613222200001]]></out_trade_no><out_refund_no><![CDATA[201506132223181434205398974]]></out_refund_no><refund_id><![CDATA[2004920996201506130009713926]]></refund_id><refund_channel><![CDATA[]]></refund_channel><refund_fee>301</refund_fee><coupon_refund_fee>0</coupon_refund_fee><total_fee>301</total_fee><coupon_refund_count>0</coupon_refund_count><cash_refund_fee>301</cash_refund_fee></xml>";
//        XMLHelper x = new XMLHelper(xml);
//        System.out.println(x.readByName("return_code"));
//        Map<String, String> map = x.readXMLMapByName("xml");
//        for (String k : map.keySet()) {
//            System.out.println(k + "-" + map.get(k));
//        }
//    }
}