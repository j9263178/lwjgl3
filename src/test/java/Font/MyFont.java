package Font;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTTBakedChar;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.Platform;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Math.round;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.stb.STBTruetype.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.memUTF8;

public class MyFont {
    String content;
    private float xoffset,yoffset,gapfactor=1;
    private final ByteBuffer ttf;
    private final STBTTFontinfo info;
    private float ContentScaleY,ContentScaleX;
    private final int ascent;
    private int FontHeight=12;
    private final int descent;
    private final float lineGap;
    private boolean lineBBEnabled=true,KerningEnabled=false;
    int BITMAP_W,BITMAP_H;
    STBTTBakedChar.Buffer cdata;
    private float lineOffset=0,scale;
    int texID;
    private ByteBuffer bitmap;
    private long window;
    private int ww = 800;
    private int wh = 600;

    public MyFont(String resource1,long window){
        this.window=window;

        String resource="/Users/joseph/Downloads/a-goblin-appears-font/AGoblinAppears-o2aV.ttf";
        try {
            ttf = IOUtil.ioResourceToByteBuffer(resource, 512 * 1024);
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

        long monitor = glfwGetPrimaryMonitor();

        int framebufferW;
        int framebufferH;
        try (MemoryStack s = stackPush()) {
            FloatBuffer px = s.mallocFloat(1);
            FloatBuffer py = s.mallocFloat(1);

            glfwGetMonitorContentScale(monitor, px, py);

            ContentScaleX = px.get(0);
            ContentScaleY = py.get(0);

            if (Platform.get() == Platform.MACOSX) {
                framebufferW = ww;
                framebufferH = wh;
            } else {
                framebufferW = round(ww * ContentScaleX);
                framebufferH = round(wh * ContentScaleY);
            }
        }

        BITMAP_W = round(512 * ContentScaleX);
        BITMAP_H = round(512 * ContentScaleY);
        cdata = init(BITMAP_W, BITMAP_H);

    }

    public void setPos(float x,float y){
        this.xoffset=x;
        this.yoffset=y;
    }

    private STBTTBakedChar.Buffer init(int BITMAP_W, int BITMAP_H) {
         this.content= "SHITWJEIRJO#IJRIO#J$34234234234\n1981093182903\nAJSOIDJASIODJ\n!!!@!@";
            this.FontHeight=12;
            texID = glGenTextures();
            STBTTBakedChar.Buffer cdata = STBTTBakedChar.malloc(96);

            bitmap = BufferUtils.createByteBuffer(BITMAP_W * BITMAP_H);
            stbtt_BakeFontBitmap(ttf, FontHeight * ContentScaleY, bitmap, BITMAP_W, BITMAP_H, 32, cdata);
        glBindTexture(GL_TEXTURE_2D, texID);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_ALPHA, BITMAP_W, BITMAP_H, 0, GL_ALPHA, GL_UNSIGNED_BYTE, bitmap);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

       // glClearColor(43f / 255f, 43f / 255f, 43f / 255f, 0f); // BG color
     /*   glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);*/
        return cdata;
    }

    private static float scale(float center, float offset, float factor) {
        return (offset - center) * factor + center;
    }

    public void draw(String content) {

        glBindTexture(GL_TEXTURE_2D, texID);

       // glClear(GL_COLOR_BUFFER_BIT);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_ALPHA, BITMAP_W, BITMAP_H, 0, GL_ALPHA, GL_UNSIGNED_BYTE, bitmap);
      //  glBindTexture(GL_TEXTURE_2D, texID);
       // glClearColor(43f / 255f, 43f / 255f, 43f / 255f, 0f);
        glColor3f(255f / 255f, 255f / 255f, 255f / 255f); // Text color
       // this.content=content;
        float scaleFactor = 1.0f + scale * 0.25f;
       // glClear(GL_COLOR_BUFFER_BIT);
        glPushMatrix();
        // Zoom
        glScalef(scaleFactor, scaleFactor, 1f);
        // Scroll
        glTranslatef(4.0f, FontHeight * 0.5f + 4.0f - lineOffset * FontHeight, 0f);

        scale = stbtt_ScaleForPixelHeight(info, FontHeight);

        try (MemoryStack stack = stackPush()) {
            IntBuffer pCodePoint = stack.mallocInt(1);

            FloatBuffer x = stack.floats(0.0f);
            FloatBuffer y = stack.floats(0.0f);

            STBTTAlignedQuad q = STBTTAlignedQuad.mallocStack(stack);

            int lineStart = 0;

            float factorX = 1.0f / ContentScaleX;
            float factorY = 1.0f / ContentScaleY;

            float lineY = 0f;

            glBegin(GL_QUADS);
            for (int i = 0, to = content.length(); i < to; ) {
                i += getCP(content, to, i, pCodePoint);

                int cp = pCodePoint.get(0);
                if (cp == '\n') {
                    if (lineBBEnabled) {
                        glEnd();
                        renderLineBB(lineStart, i - 1, y.get(0), scale);
                        glBegin(GL_QUADS);
                    }

                    y.put(0, lineY = y.get(0) + (ascent - descent + lineGap) * scale*gapfactor);
                    x.put(0, 0f);

                    lineStart = i;
                    continue;
                } else if (cp < 32 || 128 <= cp) {
                    continue;
                }

                float cpX = x.get(0);
                stbtt_GetBakedQuad(cdata, BITMAP_W, BITMAP_H, cp - 32, x, y, q, true);
                x.put(0, scale(cpX, x.get(0), factorX));
                if (KerningEnabled && i < to) {
                    getCP(content, to, i, pCodePoint);
                    x.put(0, x.get(0) + stbtt_GetCodepointKernAdvance(info, cp, pCodePoint.get(0)) * scale);
                }

                float
                        x0 =scale(cpX, q.x0()+xoffset, factorX),
                        x1 =scale(cpX, q.x1()+xoffset, factorX),
                        y0 =scale(lineY, q.y0()+yoffset, factorY),
                        y1 =scale(lineY, q.y1()+yoffset, factorY);

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
            if (lineBBEnabled) {
                renderLineBB(lineStart, content.length(), lineY, scale);
            }
           // glfwSwapBuffers(window);
        }

        glPopMatrix();
    //    glBindTexture(GL_TEXTURE_2D,0);

    }

    private void renderLineBB(int from, int to, float y, float scale) {
        glDisable(GL_TEXTURE_2D);
        glPolygonMode(GL_FRONT, GL_LINE);
        glColor3f(1.0f, 1.0f, 0.0f);

        float width = getStringWidth(info, content, from, to, FontHeight);
        y -= descent * scale;

        glBegin(GL_QUADS);
        glVertex2f(0.0f, y);
        glVertex2f(width, y);
        glVertex2f(width, y - FontHeight);
        glVertex2f(0.0f, y - FontHeight);
        glEnd();

        glEnable(GL_TEXTURE_2D);
        glPolygonMode(GL_FRONT, GL_FILL);
        glColor3f(169f / 255f, 183f / 255f, 198f / 255f); // Text color
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

                if (KerningEnabled && i < to) {
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

    private void windowSizeChanged(long window, int width, int height) {
        if (Platform.get() != Platform.MACOSX) {
            width /= ContentScaleX;
            height /= ContentScaleY;
        }

        this.ww = width;
        this.wh = height;

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0.0, width, height, 0.0, -1.0, 1.0);
        glMatrixMode(GL_MODELVIEW);

     //   FontDemo.this.setLineOffset(lineOffset);
    }


}
