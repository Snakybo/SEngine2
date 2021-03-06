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

package opengl;

import com.snakybo.torch.annotation.SerializedField;
import com.snakybo.torch.graphics.window.Window;
import com.snakybo.torch.input.cursor.Cursor;
import com.snakybo.torch.input.cursor.CursorLockMode;
import com.snakybo.torch.input.mouse.Mouse;
import com.snakybo.torch.input.mouse.MouseButton;
import com.snakybo.torch.object.Component;
import com.snakybo.torch.util.time.Time;
import org.joml.Vector2f;
import org.joml.Vector3f;

/**
 * @author Snakybo
 * @since 1.0
 */
public class CameraFreeLook extends Component
{
	@SerializedField private float sensitivity = 0.5f;
	
	protected void onStart()
	{
		Mouse.setCursorPosition(Window.getCenter());
		
		Cursor.setLockMode(CursorLockMode.Locked);
		Cursor.setVisible(false);
	}
	
	protected void onDestroy()
	{
		Cursor.setLockMode(CursorLockMode.None);
		Cursor.setVisible(true);
	}
	
	protected void onUpdate()
	{
		if(Cursor.getLockMode() != CursorLockMode.Locked)
		{
			if(Mouse.onDown(MouseButton.LEFT))
			{
				Cursor.setLockMode(CursorLockMode.Locked);
				Cursor.setVisible(false);
			}
		}
		
		if(Cursor.getLockMode() != CursorLockMode.Locked)
		{
			return;
		}
		
		Vector2f delta = Mouse.getCursorPositionDelta();

		if(delta.x != 0)
		{
			rotate(new Vector3f(0, 1, 0), delta.x);
		}

		if(delta.y != 0)
		{
			rotate(getTransform().right(), delta.y);
		}
	}
	
	private void rotate(Vector3f axis, float amount)
	{
		getTransform().rotate(axis, amount * sensitivity * Time.getDeltaTime());
	}
}
