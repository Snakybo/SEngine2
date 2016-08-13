// The MIT License(MIT)
//
// Copyright(c) 2016 Kevin Krol
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.

package com.snakybo.torch.parser;

import com.snakybo.torch.debug.Logger;
import com.snakybo.torch.debug.LoggerInternal;
import com.snakybo.torch.object.Component;
import com.snakybo.torch.object.GameObject;
import com.snakybo.torch.reflection.ReflectionUtil;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Snakybo
 * @since 1.0
 */
final class GameObjectParser
{
	private GameObjectParser()
	{
		throw new AssertionError();
	}
	
	public static Iterable<GameObject> parseGameObjectList(NodeList nodeList)
	{
		List<GameObject> result = new ArrayList<>();
		LoggerInternal.log("Parsing GameObject nodes");
		
		for(int i = 0; i < nodeList.getLength(); i++)
		{
			Node node = nodeList.item(i);
			
			if(node.getNodeType() == Node.ELEMENT_NODE)
			{
				Element element = (Element)node;
				String name = element.getElementsByTagName("name").item(0).getTextContent();
				name = !name.isEmpty() ? name : "GameObject";
				
				LoggerInternal.log("Parsing GameObject node: " + name);
				
				GameObject gameObject = new GameObject(name);
				parseTransform(gameObject, element);
				
				NodeList componentNodeList = element.getElementsByTagName("component");
				
				if(componentNodeList.getLength() > 0)
				{
					Iterable<Component> components = parseComponentList(name, componentNodeList);
					
					for(Component component : components)
					{
						LoggerInternal.log("Adding component " + component.getName() + " to GameObject " + name);
						gameObject.addComponent(component);
					}
				}
				
				result.add(gameObject);
			}
		}
		
		return result;
	}
	
	private static void parseTransform(GameObject gameObject, Element element)
	{
		Element position = (Element)element.getElementsByTagName("position").item(0);
		Element rotation = (Element)element.getElementsByTagName("rotation").item(0);
		Element scale = (Element)element.getElementsByTagName("scale").item(0);
		
		gameObject.getTransform().setLocalPosition(ParserUtil.parseVector3(position));
		gameObject.getTransform().setLocalRotation(ParserUtil.parseQuaternion(rotation));
		gameObject.getTransform().setLocalScale(ParserUtil.parseVector3(scale));
	}
	
	private static Iterable<Component> parseComponentList(String name, NodeList nodeList)
	{
		List<Component> result = new ArrayList<>();
		LoggerInternal.log("Parsing Component nodes for GameObject node: " + name);
		
		for(int i = 0; i < nodeList.getLength(); i++)
		{
			Node node = nodeList.item(i);
			
			if(node.getNodeType() == Node.ELEMENT_NODE)
			{
				Element element = (Element) node;
				String clazz = element.getElementsByTagName("class").item(0).getTextContent();
				
				LoggerInternal.log("Parsing Component node: " + clazz);
				NodeList parameterNodeList = element.getElementsByTagName("parameter");
				
				try
				{
					Class<?> c = Class.forName(clazz);
					Component component;
					
					if(parameterNodeList.getLength() > 0)
					{
						Object[] parameters = ParserUtil.parseParameterList(parameterNodeList);
						
						Class<?>[] parameterTypes = ReflectionUtil.getObjectTypes(parameters);
						
						Constructor constructor = c.getConstructor(parameterTypes);
						component = (Component) constructor.newInstance(parameters);
					}
					else
					{
						Constructor constructor = c.getConstructor();
						component = (Component) constructor.newInstance();
					}
					
					result.add(component);
				} catch(Exception e)
				{
					Logger.logError(e.getMessage(), e);
				}
			}
		}
		
		return result;
	}
}