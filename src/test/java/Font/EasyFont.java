/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package Font;

import org.lwjgl.*;

import java.nio.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBEasyFont.*;

/** STB Easy Font demo. */
public final class EasyFont extends FontDemo {

    private static final int BASE_HEIGHT = 12;
    private int quads;
    private ByteBuffer charBuffer;

    public EasyFont(long window) {
        super(BASE_HEIGHT, window);
        text="sjitdewd\npwejadoiwe\njfjoierjfs\njiofejorf";
        charBuffer = BufferUtils.createByteBuffer(text.length() * 270);

        quads = stb_easy_font_print(0, 0, getText(), null, charBuffer);

        glEnableClientState(GL_VERTEX_ARRAY);
        glVertexPointer(2, GL_FLOAT, 16, charBuffer);

        glColor3f(169f / 255f, 183f / 255f, 198f / 255f); // Text color
    }

    public static void main(String[] args) {
    }

    @Override
    public void loop() {

            float scaleFactor = 10.0f + getScale() * 0.25f;

            glPushMatrix();
            // Zoom
            glScalef(scaleFactor, scaleFactor, 1f);
            // Scroll
            glTranslatef(0.0f, 0.0f - getLineOffset() * getFontHeight(), 0f);

            glDrawArrays(GL_QUADS, 0, quads * 4);

            glPopMatrix();

    }

}