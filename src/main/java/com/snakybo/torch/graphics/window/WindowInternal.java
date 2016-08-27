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

package com.snakybo.torch.graphics.window;

import com.snakybo.torch.Game;
import com.snakybo.torch.debug.LoggerInternal;
import com.snakybo.torch.graphics.monitor.DisplayMode;
import com.snakybo.torch.graphics.renderer.Renderer;
import com.snakybo.torch.input.joystick.JoystickController;
import com.snakybo.torch.input.keyboard.KeyboardController;
import com.snakybo.torch.input.mouse.Mouse;
import com.snakybo.torch.input.mouse.MouseController;
import com.snakybo.torch.util.callback.TorchCallbacks;
import org.joml.Vector2f;
import org.lwjgl.glfw.Callbacks;

import static org.lwjgl.glfw.GLFW.GLFW_BLUE_BITS;
import static org.lwjgl.glfw.GLFW.GLFW_DECORATED;
import static org.lwjgl.glfw.GLFW.GLFW_GREEN_BITS;
import static org.lwjgl.glfw.GLFW.GLFW_RED_BITS;
import static org.lwjgl.glfw.GLFW.GLFW_REFRESH_RATE;
import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.GLFW_VISIBLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDefaultWindowHints;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetCharCallback;
import static org.lwjgl.glfw.GLFW.glfwSetCursorEnterCallback;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowFocusCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowIconifyCallback;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * @author Snakybo
 * @since 1.0
 */
public final class WindowInternal
{
	private static DisplayMode displayMode;
	
	private static long nativeId;
	
	private WindowInternal()
	{
		throw new AssertionError();
	}
	
	static void create(DisplayMode displayMode, WindowMode windowMode)
	{
		if(WindowInternal.displayMode != null || nativeId != NULL)
		{
			destroy();
		}
		
		LoggerInternal.log("Creating window: " + displayMode);
		
		WindowInternal.displayMode = displayMode;
		
		long monitor = NULL;
		
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GL_FALSE);
		
		if(windowMode == WindowMode.Borderless)
		{
			glfwWindowHint(GLFW_RED_BITS, displayMode.getBitsPerPixel());
			glfwWindowHint(GLFW_GREEN_BITS, displayMode.getBitsPerPixel());
			glfwWindowHint(GLFW_BLUE_BITS, displayMode.getBitsPerPixel());
			glfwWindowHint(GLFW_REFRESH_RATE, displayMode.getFrequency());
			
			glfwWindowHint(GLFW_DECORATED, GL_FALSE);
		}
		else if(windowMode == WindowMode.Fullscreen)
		{
			monitor = displayMode.getMonitor().getNativeId();
		}
		
		nativeId = glfwCreateWindow(displayMode.getWidth(), displayMode.getHeight(), Game.getName(), monitor, NULL);
		if(nativeId == NULL)
		{
			throw new RuntimeException("Unable to create GLFW window");
		}
		
		glfwSetWindowFocusCallback(nativeId, (window, focus) -> TorchCallbacks.onWindowFocus.getCallbacks().forEach(cb -> cb.onWindowFocus(focus)));
		glfwSetWindowIconifyCallback(nativeId, (window, iconified) -> TorchCallbacks.onWindowIconify.getCallbacks().forEach(cb -> cb.onWindowIconify(iconified)));
		
		glfwSetCharCallback(nativeId, (window, codepoint) -> TorchCallbacks.onCharPressed.getCallbacks().forEach(cb -> cb.onCharPressed((char)codepoint)));
		
		glfwSetCursorEnterCallback(nativeId, (window, entered) -> TorchCallbacks.onCursorEnter.getCallbacks().forEach(cb -> cb.onCursorEnter(entered)));
		glfwSetScrollCallback(nativeId, (window, x, y) -> Mouse.setScrollDelta(new Vector2f((float)x, (float)y)));
		
		glfwMakeContextCurrent(nativeId);
		glfwShowWindow(nativeId);
		Window.setVSyncEnabled(true);
		
		Renderer.create();
		
		KeyboardController.create();
		MouseController.create();
		JoystickController.create();
	}
	
	public static void pollEvents()
	{
		glfwPollEvents();
	}
	
	public static void update()
	{
		glfwSwapBuffers(nativeId);
	}
	
	public static void destroy()
	{
		LoggerInternal.log("Destroying window");
		
		Callbacks.glfwFreeCallbacks(nativeId);
		
		glfwDestroyWindow(nativeId);
		
		displayMode = null;
		nativeId = NULL;
	}
	
	public static boolean isCloseRequested()
	{
		return glfwWindowShouldClose(nativeId);
	}
	
	public static long getNativeId()
	{
		return nativeId;
	}
}
