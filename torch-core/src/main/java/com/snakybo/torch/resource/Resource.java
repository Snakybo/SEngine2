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

package com.snakybo.torch.resource;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.snakybo.torch.debug.Logger;
import com.snakybo.torch.util.FileUtils;

/**
 * @author Snakybo
 * @since 1.0
 */
public final class Resource
{
	private Resource()
	{
		throw new AssertionError();
	}
	
	/**
	 * Get the path to a resource as an URI.
	 * @param path The path to the resource.
	 * @return An URI of the resource.
	 */
	public static URI get(String path)
	{
		try
		{
			URL resource = Resource.class.getResource("/" + path);
			if(resource != null)
			{
				return resource.toURI();
			}
			
			Logger.logError("Unable to find a resource at: " + path);
			return null;
		}
		catch(URISyntaxException e)
		{
			Logger.logException(e);
		}
		
		return null;
	}
	
	/**
	 * Load a resource from the specified path.
	 * @param path The path to the resource.
	 * @return The resource, can be anything as
	 * long as the receiving class knows what to do with the data.
	 */
	public static Object load(String path)
	{
		return load(get(path));
	}
	
	/**
	 * Load a resource from the specified URI.
	 * @param path The path to the resource.
	 * @return The resource, can be anything as
	 * long as the receiving class knows what to do with the data.
	 */
	public static Object load(URI path)
	{
		Path p = Paths.get(path);
		
		if(Files.exists(p))
		{
			return ResourceLoader.load(path, FileUtils.getExtension(path));
		}
		
		Logger.logWarning("No resource found at: " + p);
		return null;
	}
}
