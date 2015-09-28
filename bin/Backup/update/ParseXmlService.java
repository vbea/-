package com.binxin.zdapp.update;

import java.io.InputStream;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ParseXmlService
{
	public HashMap<String, String> parseXml(InputStream inStream) throws Exception
	{
		HashMap<String, String> hashMap = new HashMap<String, String>();
		
		// 实例化一个文档构建器工厂
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		// 通过文档构建器工厂获取一个文档构建器
		DocumentBuilder builder = factory.newDocumentBuilder();
		// 通过文档构建器构建一个文档实例
		Document document = builder.parse(inStream);
		//获取XML文件根节点
		Element root = document.getDocumentElement();
		//获得所有子节点
		NodeList childNodes = root.getChildNodes();
		for (int i = 0; i < childNodes.getLength(); i++)
		{
			//遍历子节点
			Node childNode = (Node)childNodes.item(i);
			if (childNode.getNodeType() == Node.ELEMENT_NODE)
			{
				Element childElement = (Element) childNode;
				//版本号
				if ("version".equals(childElement.getNodeName()))
				{
					hashMap.put("version",childElement.getFirstChild().getNodeValue());
				}
				//软件名称
				else if (("vername".equals(childElement.getNodeName())))
				{
					hashMap.put("vername",childElement.getFirstChild().getNodeValue());
				}
				//下载地址
				else if (("url".equals(childElement.getNodeName())))
				{
					hashMap.put("url",childElement.getFirstChild().getNodeValue());
				}
				//新版特征
				else if (("newmsg".equals(childElement.getNodeName())))
				{
					hashMap.put("newmsg",childElement.getFirstChild().getNodeValue());
				}
			}
		}
		return hashMap;
	}
}
