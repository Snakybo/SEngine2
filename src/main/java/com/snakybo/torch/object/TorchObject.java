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

package com.snakybo.torch.object;

import com.snakybo.torch.debug.Logger;
import com.snakybo.torch.scene.SceneInternal;

import java.io.Serializable;

/**
 * @author Snakybo
 * @since 1.0
 */
public class TorchObject implements Serializable
{
	private String name;
	
	public TorchObject(String name)
	{
		setName(name);
	}
	
	/**
	 * @return The name of the object.
	 */
	@Override
	public String toString()
	{
		return getName();
	}
	
	/**
	 * Set the name of the object.
	 * @param name The new name.
	 */
	public final void setName(String name)
	{
		if(name == null || name.isEmpty())
		{
			Logger.logError("Invalid name for object");
			return;
		}
		
		this.name = name;
	}
	
	/**
	 * @return The name of the object.
	 */
	public final String getName()
	{
		return name;
	}
	
	public static void destroy(TorchObject obj)
	{
		if(obj instanceof GameObject)
		{
			// Destroy the GameObject
			GameObject gameObject = (GameObject)obj;
			SceneInternal.remove(gameObject);
			
			// Also destroy all components
			gameObject.components.forEach(TorchObject::destroy);
			
			// Destroy all children of the GameObject
			gameObject.getTransform().getChildren().forEach((t) -> destroy(t.gameObject));
		}
		else if(obj instanceof Component)
		{
			Component component = (Component)obj;
			component.gameObject.componentsToRemove.add(component);
		}
	}
}