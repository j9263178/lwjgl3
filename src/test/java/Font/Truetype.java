package Font;

/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */

import org.lwjgl.*;
import org.lwjgl.stb.*;
import org.lwjgl.system.*;

import java.io.*;
import java.nio.*;

import static java.lang.Math.*;
import static Font.IOUtil.*;
import static MainGame.GlobalObjects.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBTruetype.*;
import static org.lwjgl.system.MemoryStack.*;

/** STB Truetype demo. */
public final class Truetype extends FontDemo {
    public boolean popout=true;
    private int poptimes=0;
    private final ByteBuffer ttf;
    private final STBTTFontinfo info;
    private final int ascent;
    private final int descent;
    private final int lineGap;
    private STBTTBakedChar.Buffer cdata;
    private int BITMAP_W,BITMAP_H;
    private int                   texID;
    private ByteBuffer bitmap;
    public float gapFactor=1.5f;
    public float xo=100f,yo=0;

    public Truetype(int size) {
        super(size,window);

        try {
            ttf = ioResourceToByteBuffer("/Users/joseph/Downloads/press-start-2p-font/PressStart2P-vaV7.ttf", 512 * 1024);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        info = STBTTFontinfo.create();
        if (!stbtt_InitFont(info, ttf)) {
            throw new IllegalStateException("Failed to initialize font information.");
        }

        try (MemoryStack stack = stackPush()) {
            IntBuffer pAscent  = stack.mallocInt(1);
            IntBuffer pDescent = stack.mallocInt(1);
            IntBuffer pLineGap = stack.mallocInt(1);

            stbtt_GetFontVMetrics(info, pAscent, pDescent, pLineGap);

            ascent = pAscent.get(0);
            descent = pDescent.get(0);
            lineGap = pLineGap.get(0);
        }


        BITMAP_W = round(512 * getContentScaleX());
        BITMAP_H = round(512 * getContentScaleY());
        text="SHIHGOREOIEWJF\noiwejdiojweodj23i\njiojdoi12!*$!@*&#(!@*^#(!@";
        setScale(-1);
        cdata = init(BITMAP_W, BITMAP_H);
    }

    private STBTTBakedChar.Buffer init(int BITMAP_W, int BITMAP_H) {
        texID = glGenTextures();
        STBTTBakedChar.Buffer cdata = STBTTBakedChar.malloc(96);

        bitmap = BufferUtils.createByteBuffer(BITMAP_W * BITMAP_H);
        stbtt_BakeFontBitmap(ttf, getFontHeight() * getContentScaleY(), bitmap, BITMAP_W, BITMAP_H, 32, cdata);

        glBindTexture(GL_TEXTURE_2D, texID);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_ALPHA, BITMAP_W, BITMAP_H, 0, GL_ALPHA, GL_UNSIGNED_BYTE, bitmap);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        return cdata;
    }

    @Override
    public void loop() {

        glColor3f(1f, 1f, 1f); // Text color
        glBindTexture(GL_TEXTURE_2D, texID);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_ALPHA, BITMAP_W, BITMAP_H, 0, GL_ALPHA, GL_UNSIGNED_BYTE, bitmap);

       //     glfwPollEvents();

        //    glClear(GL_COLOR_BUFFER_BIT);

            float scaleFactor = 1.0f + getScale() * 0.25f;

            glPushMatrix();
            // Zoom
            glScalef(scaleFactor, scaleFactor, 1f);
            // Scroll
            glTranslatef(4.0f, getFontHeight() * 0.5f + 4.0f - getLineOffset() * getFontHeight(), 0f);

        if(popout)
            renderPopingText(cdata, BITMAP_W, BITMAP_H);
        else
            renderText(cdata, BITMAP_W, BITMAP_H);

            glPopMatrix();

        //    glfwSwapBuffers(getWindow());

    }

    private static float scale(float center, float offset, float factor) {
        return (offset - center) * factor + center;
    }

    private void renderText(STBTTBakedChar.Buffer cdata, int BITMAP_W, int BITMAP_H) {
        float scale = stbtt_ScaleForPixelHeight(info, getFontHeight());

        try (MemoryStack stack = stackPush()) {
            IntBuffer pCodePoint = stack.mallocInt(1);

            FloatBuffer x = stack.floats(0.0f);
            FloatBuffer y = stack.floats(0.0f);

            STBTTAlignedQuad q = STBTTAlignedQuad.mallocStack(stack);

            int lineStart = 0;

            float factorX = 1.0f / getContentScaleX();
            float factorY = 1.0f / getContentScaleY();

            float lineY = 0.0f;

            glBegin(GL_QUADS);
            for (int i = 0, to = text.length()-1; i < to; ) {
                i += getCP(text, to, i, pCodePoint);

                int cp = pCodePoint.get(0);
                if (cp == '\n') {
                    if (isLineBBEnabled()) {
                        glEnd();
                        renderLineBB(lineStart, i - 1, y.get(0), scale);
                        glBegin(GL_QUADS);
                    }

                    y.put(0, lineY = y.get(0) + (ascent - descent + lineGap) * scale*gapFactor);
                    x.put(0, 0.0f);

                    lineStart = i;
                    continue;
                } else if (cp < 32 || 128 <= cp) {
                    continue;
                }

                float cpX = x.get(0);
                stbtt_GetBakedQuad(cdata, BITMAP_W, BITMAP_H, cp - 32, x, y, q, true);
                x.put(0, scale(cpX, x.get(0), factorX));
                if (isKerningEnabled() && i < to) {
                    getCP(text, to, i, pCodePoint);
                    x.put(0, x.get(0) + stbtt_GetCodepointKernAdvance(info, cp, pCodePoint.get(0)) * scale);
                }

                float
                        x0 = scale(cpX, q.x0()+xo, factorX),
                        x1 = scale(cpX, q.x1()+xo, factorX),
                        y0 = scale(lineY, q.y0()+yo, factorY),
                        y1 = scale(lineY, q.y1()+yo, factorY);

                glTexCoord2f(q.s0(), q.t0());
                glVertex2f(x0, y0);

                glTexCoord2f(q.s1(), q.t0());
                glVertex2f(x1, y0);

                glTexCoord2f(q.s1(), q.t1());
                glVertex2f(x1, y1);

                glTexCoord2f(q.s0(), q.t1());
                glVertex2f(x0, y1);
            }
            glEnd();
            if (isLineBBEnabled()) {
                renderLineBB(lineStart, text.length(), lineY, scale);
            }
        }
    }

    private void renderPopingText(STBTTBakedChar.Buffer cdata, int BITMAP_W, int BITMAP_H) {
        float scale = stbtt_ScaleForPixelHeight(info, getFontHeight());

        try (MemoryStack stack = stackPush()) {
            IntBuffer pCodePoint = stack.mallocInt(1);

            FloatBuffer x = stack.floats(0.0f);
            FloatBuffer y = stack.floats(0.0f);

            STBTTAlignedQuad q = STBTTAlignedQuad.mallocStack(stack);

            int lineStart = 0;

            float factorX = 1.0f / getContentScaleX();
            float factorY = 1.0f / getContentScaleY();

            float lineY = 0.0f;

            if(poptimes<text.length()) poptimes+=1;

            glBegin(GL_QUADS);
            for (int i = 0, to = poptimes; i < to; ) {
                i += getCP(text, to, i, pCodePoint);

                int cp = pCodePoint.get(0);
                if (cp == 'ㄙ'){
                    glColor3f(1f, 1f, 0f); // Text color
                }
                if (cp == '\b'){

                    glColor3f(1f, 1f, 1f); // Text color
                }
                if (cp == '\n') {
                    if (isLineBBEnabled()) {
                        glEnd();
                        renderLineBB(lineStart, i - 1, y.get(0), scale);
                        glBegin(GL_QUADS);
                    }

                    y.put(0, lineY = y.get(0) + (ascent - descent + lineGap) * scale*gapFactor);
                    x.put(0, 0.0f);

                    lineStart = i;
                    continue;
                } else if (cp < 32 || 128 <= cp) {
                    continue;
                }

                float cpX = x.get(0);
                stbtt_GetBakedQuad(cdata, BITMAP_W, BITMAP_H, cp - 32, x, y, q, true);
                x.put(0, scale(cpX, x.get(0), factorX));
                if (isKerningEnabled() && i < to) {
                    getCP(text, to, i, pCodePoint);
                    x.put(0, x.get(0) + stbtt_GetCodepointKernAdvance(info, cp, pCodePoint.get(0)) * scale);
                }

                float
                        x0 = scale(cpX, q.x0()+xo, factorX),
                        x1 = scale(cpX, q.x1()+xo, factorX),
                        y0 = scale(lineY, q.y0()+yo, factorY),
                        y1 = scale(lineY, q.y1()+yo, factorY);

                glTexCoord2f(q.s0(), q.t0());
                glVertex2f(x0, y0);

                glTexCoord2f(q.s1(), q.t0());
                glVertex2f(x1, y0);

                glTexCoord2f(q.s1(), q.t1());
                glVertex2f(x1, y1);

                glTexCoord2f(q.s0(), q.t1());
                glVertex2f(x0, y1);
            }
            glEnd();
            if (isLineBBEnabled()) {
                renderLineBB(lineStart, text.length(), lineY, scale);
            }
        }
    }

    private void renderLineBB(int from, int to, float y, float scale) {
        glDisable(GL_TEXTURE_2D);
        glPolygonMode(GL_FRONT, GL_LINE);
        glColor3f(1.0f, 1.0f, 0.0f);

        float width = getStringWidth(info, text, from, to, getFontHeight());
        y -= descent * scale;

        glBegin(GL_QUADS);
        glVertex2f(0.0f, y);
        glVertex2f(width, y);
        glVertex2f(width, y - getFontHeight());
        glVertex2f(0.0f, y - getFontHeight());
        glEnd();

        glEnable(GL_TEXTURE_2D);
        glPolygonMode(GL_FRONT, GL_FILL);
        glColor3f(1f, 1f, 1f); // Text color
    }

    private float getStringWidth(STBTTFontinfo info, String text, int from, int to, int fontHeight) {
        int width = 0;

        try (MemoryStack stack = stackPush()) {
            IntBuffer pCodePoint       = stack.mallocInt(1);
            IntBuffer pAdvancedWidth   = stack.mallocInt(1);
            IntBuffer pLeftSideBearing = stack.mallocInt(1);

            int i = from;
            while (i < to) {
                i += getCP(text, to, i, pCodePoint);
                int cp = pCodePoint.get(0);

                stbtt_GetCodepointHMetrics(info, cp, pAdvancedWidth, pLeftSideBearing);
                width += pAdvancedWidth.get(0);

                if (isKerningEnabled() && i < to) {
                    getCP(text, to, i, pCodePoint);
                    width += stbtt_GetCodepointKernAdvance(info, cp, pCodePoint.get(0));
                }
            }
        }

        return width * stbtt_ScaleForPixelHeight(info, fontHeight);
    }

    private static int getCP(String text, int to, int i, IntBuffer cpOut) {
        char c1 = text.charAt(i);
        if (Character.isHighSurrogate(c1) && i + 1 < to) {
            char c2 = text.charAt(i + 1);
            if (Character.isLowSurrogate(c2)) {
                cpOut.put(0, Character.toCodePoint(c1, c2));
                return 2;
            }
        }
        cpOut.put(0, c1);
        return 1;
    }

}
