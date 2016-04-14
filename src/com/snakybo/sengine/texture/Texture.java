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

package com.snakybo.sengine.texture;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNPACK_ALIGNMENT;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glPixelStorei;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

import com.snakybo.sengine.debug.Logger;
import com.snakybo.sengine.debug.LoggerInternal;
import com.snakybo.sengine.util.IDestroyable;

/**
 * @author Snakybo
 * @since 1.0
 */
public final class Texture implements IDestroyable
{
	private int width;
	private int height;
	
	private int id;
	
	public Texture(String fileName)
	{
		fileName = "./res/" + fileName;
		
		ByteBuffer data = loadTexture(fileName);
		
		id = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, id);
		
		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, data);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		
		glGenerateMipmap(GL_TEXTURE_2D);
	}
	
	@Override
	public void destroy()
	{
		glDeleteTextures(id);
	}
	
	public void bind()
	{
		glActiveTexture(GL_TEXTURE0);
		
		glBindTexture(GL_TEXTURE_2D, id);
	}
	
	private final ByteBuffer loadTexture(String fileName)
	{
		ByteBuffer data = null;
		
		try
		{
			LoggerInternal.log("Loading Texture from: " + fileName, this);
			
			File file = new File(fileName);			
			if(!file.exists())
			{
				throw new FileNotFoundException(fileName + " cannot be found");
			}
			
			BufferedImage image = ImageIO.read(file);
			
			width = image.getWidth();
			height = image.getHeight();
			
			int[] pixels = image.getRGB(0, 0, width, height, null, 0, width);
	
			data = BufferUtils.createByteBuffer(height * width * 4);
			
			boolean hasAlpha = image.getColorModel().hasAlpha();
	
			// Put each pixel in a Byte Buffer
			for(int y = 0; y < height; y++)
			{
				for(int x = 0; x < width; x++)
				{
					int pixel = pixels[y * width + x];
	
					byte alphaByte = hasAlpha ? (byte)((pixel >> 24) & 0xFF) : (byte)(0xFF);
	
					data.put((byte)((pixel >> 16) & 0xFF));
					data.put((byte)((pixel >> 8) & 0xFF));
					data.put((byte)((pixel) & 0xFF));
					data.put(alphaByte);
				}
			}
	
			data.flip();
		}
		catch(IOException e)
		{
			Logger.logException(e);
		}
		
		return data;
	}
}
