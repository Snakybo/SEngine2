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

package com.snakybo.torch.util.debug;

import java.util.logging.Level;

/**
 * <p>
 * The main logging utility of the engine.
 * </p>
 *
 * <p>
 * All logs are written to both the console,
 * and a file located at {@code %appdata%/TorchEngine/log.txt}.
 * </p>
 *
 * @author Snakybo
 * @since 1.0
 */
public final class Logger
{
	private Logger()
	{
		throw new AssertionError();
	}
	
	/**
	 * <p>
	 * Log a message.
	 * </p>
	 *
	 * @param msg The message to log.
	 */
	public static void log(Object msg)
	{
		LoggerInternal.logInternal(Level.INFO, msg);
	}
	
	/**
	 * <p>
	 * Log a warning.
	 * </p>
	 *
	 * @param msg The warning to log.
	 */
	public static void logWarning(Object msg)
	{
		LoggerInternal.logInternal(Level.WARNING, msg);
	}
	
	/**
	 * <p>
	 * Log an error.
	 * </p>
	 *
	 * @param msg The error to log.
	 */
	public static void logError(Object msg)
	{
		LoggerInternal.logInternal(Level.SEVERE, msg);
	}
	
	/**
	 * <p>
	 * Log an error.
	 * </p>
	 *
	 * @param msg The error to log.
	 * @param thrown The related exception.
	 */
	public static void logError(Object msg, Throwable thrown)
	{
		LoggerInternal.logInternal(Level.SEVERE, msg, thrown);
	}
}
