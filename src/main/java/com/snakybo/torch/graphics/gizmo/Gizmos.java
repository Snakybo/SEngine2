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

package com.snakybo.torch.graphics.gizmo;

import com.snakybo.torch.asset.Assets;
import com.snakybo.torch.component.Camera;
import com.snakybo.torch.debug.Logger;
import com.snakybo.torch.graphics.color.Color;
import com.snakybo.torch.graphics.mesh.Mesh;
import com.snakybo.torch.graphics.renderer.MeshRendererInternal;
import com.snakybo.torch.graphics.shader.Shader;
import com.snakybo.torch.object.Component;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_LINE;
import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glPolygonMode;

/**
 * <p>
 * Used for drawing gizmos in the game view.
 * </p>
 *
 * <p>
 * Gizmos do not have a depth test, and are not affected by lighting effects.
 * </p>
 *
 * @author Snakybo
 * @since 1.0
 */
public final class Gizmos
{
	private static Shader shader;
	
	private static Color color;
	
	static
	{
		shader = Assets.load(Shader.class, "gizmos.glsl");
		
		setColor(Color.WHITE);
	}
	
	private Gizmos()
	{
		throw new AssertionError();
	}
	
	/**
	 * <p>
	 * Reset all gizmo properties.
	 * </p>
	 *
	 * <p>
	 * This is automatically called before {@link Component#onRenderGizmos()} is called,
	 * but you can also call it manually in your {@code onRenderGizmos} method to reset everything.
	 * </p>
	 */
	public static void reset()
	{
		setColor(Color.WHITE);
	}
	
	/**
	 * <p>
	 * Draw a solid cube at the target {@code position}.
	 * </p>
	 *
	 * @param position The position to draw the cube at.
	 * @param size The size of the cube.
	 */
	public static void drawCube(Vector3f position, Vector3f size)
	{
		if(!canRender())
		{
			return;
		}
		
		shader.bind();
		
		updateMatrix(position, size);
		GizmoShapeCube.render(GL_TRIANGLES);
		
		shader.unbind();
	}
	
	/**
	 * <p>
	 * Draw the wireframe of a cube at the target {@code position}.
	 * </p>
	 *
	 * @param position The position to draw the cube at.
	 * @param size The size of the cube.
	 */
	public static void drawCubeWireframe(Vector3f position, Vector3f size)
	{
		if(!canRender())
		{
			return;
		}
		
		shader.bind();
		
		updateMatrix(position, size);
		GizmoShapeCubeWireframe.render(GL_LINES);
		
		shader.unbind();
	}
	
	/**
	 * <p>
	 * Draw a solid sphere at the target {@code position}.
	 * </p>
	 *
	 * @param position The position to draw the sphere at.
	 * @param radius The radius of the sphere.
	 */
	public static void drawSphere(Vector3f position, float radius)
	{
		if(!canRender())
		{
			return;
		}
		
		shader.bind();
		
		updateMatrix(position, new Vector3f(radius));
		GizmoShapeSphere.render(GL_QUADS);
		
		shader.unbind();
	}
	
	/**
	 * <p>
	 * Draw a wireframe of a sphere at the target {@code position}.
	 * </p>
	 *
	 * @param position The position to draw the sphere at.
	 * @param radius The radius of the sphere.
	 */
	public static void drawSphereWireframe(Vector3f position, float radius)
	{
		if(!canRender())
		{
			return;
		}
		
		shader.bind();
		
		glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		
		updateMatrix(position, new Vector3f(radius));
		GizmoShapeSphere.render(GL_QUADS);
		
		glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		
		shader.unbind();
	}
	
	private static void updateMatrix(Vector3f position, Vector3f size)
	{
		shader.setUniform4fv("_model", new Matrix4f().translate(position).scale(size));
		shader.setUniform4fv("_projection", Camera.getMainCamera().getProjectionMatrix());
		shader.setUniform4fv("_view", Camera.getMainCamera().getViewMatrix());
		
		shader.setUniform3f("color", new Vector3f(color.getRed(), color.getGreen(), color.getBlue()));
	}
	
	private static boolean canRender()
	{
		if(!GizmosInternal.isInGizmoRenderPass)
		{
			Logger.logWarning("Gizmos can only be rendered from Component.onRenderGizmos()");
			return false;
		}
		
		return true;
	}
	
	/**
	 * <p>
	 * Set the color for the gizmos.
	 * </p>
	 *
	 * <p>
	 * All gizmos drawn after setting the color will be drawn with that color.
	 * </p>
	 *
	 * @param color The new color for the gizmos.
	 */
	public static void setColor(Color color)
	{
		color = color != null ? color : Color.WHITE;
		Gizmos.color = color;
	}
	
	/**
	 * <p>
	 * Get the color the gizmos are rendered with.
	 * </p>
	 *
	 * @return The color of the gizmos.
	 */
	public static Color getColor()
	{
		return color;
	}
}