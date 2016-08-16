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

package com.snakybo.torch.xml;

import com.snakybo.torch.debug.Logger;
import com.snakybo.torch.debug.LoggerInternal;
import com.snakybo.torch.util.FileUtils;
import org.w3c.dom.Document;

import java.net.URI;
import java.nio.file.NoSuchFileException;

/**
 * @author Snakybo
 * @since 1.0
 */
public final class XMLParser
{
	private XMLParser()
	{
		throw new AssertionError();
	}
	
	public static Object decode(String file) throws NoSuchFileException
	{
		LoggerInternal.log("Begin decoding XML file: " + file);
		
		URI uri = FileUtils.toURI(file);
		Document document = XMLParserUtils.getDocument(uri);
		
		if(document == null)
		{
			Logger.logError("Unable to decode XML: " + file);
			return null;
		}
		
		String rootNode = document.getDocumentElement().getNodeName();
		LoggerInternal.log("Document is of type: " + rootNode);
		
		switch(rootNode)
		{
		case "scene":
			return null;
		case "material":
			return MaterialParser.decode(document.getDocumentElement());
		case "texture":
			return TextureParser.decode(document.getDocumentElement());
		default:
			Logger.logError("Unknown root node type: " + rootNode);
			return null;
		}
	}
}
