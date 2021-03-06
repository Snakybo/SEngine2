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

package com.snakybo.torch.util.reflection;

import com.snakybo.torch.annotation.SerializedField;
import com.snakybo.torch.util.debug.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Used internally by the engine.
 * </p>
 *
 * @author Snakybo
 * @since 1.0
 */
public final class SerializedFieldUtils
{
	private SerializedFieldUtils()
	{
		throw new AssertionError();
	}
	
	public static void set(Object object, Field field, boolean value)
	{
		Class<?> t = field.getType();
		if(t.equals(boolean.class))
		{
			try
			{
				boolean accessible = field.isAccessible();
				field.setAccessible(true);
				field.setBoolean(object, value);
				field.setAccessible(accessible);
			}
			catch (IllegalArgumentException | IllegalAccessException e)
			{
				Logger.logError(e.getMessage(), e);
			}
		}
		else
		{
			Logger.logError("boolean is not equal to " + t);
		}
	}
	
	public static void set(Object object, Field field, byte value)
	{
		Class<?> t = field.getType();
		if(t.equals(byte.class))
		{
			try
			{
				boolean accessible = field.isAccessible();
				field.setAccessible(true);
				field.setByte(object, value);
				field.setAccessible(accessible);
			}
			catch (IllegalArgumentException | IllegalAccessException e)
			{
				Logger.logError(e.getMessage(), e);
			}
		}
		else
		{
			Logger.logError("byte is not equal to " + t);
		}
	}
	
	public static void set(Object object, Field field, short value)
	{
		Class<?> t = field.getType();
		if(t.equals(short.class))
		{
			try
			{
				boolean accessible = field.isAccessible();
				field.setAccessible(true);
				field.setShort(object, value);
				field.setAccessible(accessible);
			}
			catch (IllegalArgumentException | IllegalAccessException e)
			{
				Logger.logError(e.getMessage(), e);
			}
		}
		else
		{
			Logger.logError("short is not equal to " + t);
		}
	}
	
	public static void set(Object object, Field field, int value)
	{
		Class<?> t = field.getType();
		if(t.equals(int.class))
		{
			try
			{
				boolean accessible = field.isAccessible();
				field.setAccessible(true);
				field.setInt(object, value);
				field.setAccessible(accessible);
			}
			catch (IllegalArgumentException | IllegalAccessException e)
			{
				Logger.logError(e.getMessage(), e);
			}
		}
		else
		{
			Logger.logError("int is not equal to " + t);
		}
	}
	
	public static void set(Object object, Field field, float value)
	{
		Class<?> t = field.getType();
		if(t.equals(float.class))
		{
			try
			{
				boolean accessible = field.isAccessible();
				field.setAccessible(true);
				field.setFloat(object, value);
				field.setAccessible(accessible);
			}
			catch (IllegalArgumentException | IllegalAccessException e)
			{
				Logger.logError(e.getMessage(), e);
			}
		}
		else
		{
			Logger.logError("float is not equal to " + t);
		}
	}
	
	public static void set(Object object, Field field, double value)
	{
		Class<?> t = field.getType();
		if(t.equals(double.class))
		{
			try
			{
				boolean accessible = field.isAccessible();
				field.setAccessible(true);
				field.setDouble(object, value);
				field.setAccessible(accessible);
			}
			catch (IllegalArgumentException | IllegalAccessException e)
			{
				Logger.logError(e.getMessage(), e);
			}
		}
		else
		{
			Logger.logError("double is not equal to " + t);
		}
	}
	
	public static void set(Object object, Field field, long value)
	{
		Class<?> t = field.getType();
		if(t.equals(long.class))
		{
			try
			{
				boolean accessible = field.isAccessible();
				field.setAccessible(true);
				field.setLong(object, value);
				field.setAccessible(accessible);
			}
			catch (IllegalArgumentException | IllegalAccessException e)
			{
				Logger.logError(e.getMessage(), e);
			}
		}
		else
		{
			Logger.logError("long is not equal to " + t);
		}
	}
	
	public static void set(Object object, Field field, char value)
	{
		Class<?> t = field.getType();
		if(t.equals(char.class))
		{
			try
			{
				boolean accessible = field.isAccessible();
				field.setAccessible(true);
				field.setChar(object, value);
				field.setAccessible(accessible);
			}
			catch (IllegalArgumentException | IllegalAccessException e)
			{
				Logger.logError(e.getMessage(), e);
			}
		}
		else
		{
			Logger.logError("char is not equal to " + t);
		}
	}
	
	public static void set(Object object, Field field, String value)
	{
		Class<?> t = field.getType();
		if(t.equals(String.class) || value == null)
		{
			try
			{
				boolean accessible = field.isAccessible();
				field.setAccessible(true);
				field.set(object, value);
				field.setAccessible(accessible);
			}
			catch (IllegalArgumentException | IllegalAccessException e)
			{
				Logger.logError(e.getMessage(), e);
			}
		}
		else
		{
			Logger.logError("String is not equal to " + t);
		}
	}
	
	public static void set(Object object, Field field, Object value)
	{
		Class<?> t = field.getType();
		if(value == null || t.isAssignableFrom(value.getClass()))
		{
			try
			{
				boolean accessible = field.isAccessible();
				field.setAccessible(true);
				field.set(object, value);
				field.setAccessible(accessible);
			}
			catch (IllegalArgumentException | IllegalAccessException e)
			{
				Logger.logError(e.getMessage(), e);
			}
		}
		else
		{
			Logger.logError(value.getClass() + " is not equal to " + t);
		}
	}
	
	public static Field[] getAll(Object object)
	{
		List<Field> result = new ArrayList<>();
		
		for(Field field : object.getClass().getDeclaredFields())
		{
			if(!isExposed(field))
			{
				continue;
			}
			
			result.add(field);
		}
		
		return result.toArray(new Field[result.size()]);
	}
	
	public static Field get(Object object, String name) throws NoSuchFieldException
	{
		Class<?> clazz = object.getClass();
		
		while(clazz != null)
		{
			try
			{
				Field field = clazz.getDeclaredField(name);
				
				if(field != null && isExposed(field))
				{
					return field;
				}
			}
			catch(NoSuchFieldException | SecurityException e)
			{
			}
			finally
			{
				clazz = clazz.getSuperclass();
			}
		}
		
		throw new NoSuchFieldException("No field with name: " + name + " found on object: " + object);
	}
	
	public static Object getValue(Object object, Field field)
	{
		Object result = null;
		
		boolean accessible = field.isAccessible();
		field.setAccessible(true);
		
		try
		{
			result = field.get(object);
		}
		catch(IllegalArgumentException | IllegalAccessException e)
		{
			Logger.logError(e.getMessage(), e);
		}
		
		field.setAccessible(accessible);
		return result;
	}
	
	public static boolean isExposed(Field field)
	{
		int modifiers = field.getModifiers();
		
		// A field cannot be static
		if(Modifier.isStatic(modifiers))
		{
			return false;
		}
		
		// A field must have the SerializedField annotation or be public
		if(!field.isAnnotationPresent(SerializedField.class) && !Modifier.isPublic(modifiers))
		{
			return false;
		}
		
		return true;
	}
}
