package com.nlptech.function.keyboardrender;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

import com.nlptech.inputmethod.latin.settings.Settings;
import com.nlptech.keyboardview.keyboard.Key;
import com.nlptech.keyboardview.keyboard.Keyboard;
import com.nlptech.keyboardview.keyboard.internal.KeyDrawParams;
import com.nlptech.keyboardview.keyboard.render.DefaultKeyboardRender;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RGBKeyboardRender extends DefaultKeyboardRender {

    private ColorMatrix hueMatrix;
    /**
     * LinearGradient 的所有顏色
     **/
    private static final int[] COLORS = new int[]{
            convert(Color.YELLOW),
            convert(Color.parseColor("#00FFAA")),
            convert(Color.parseColor("#008888")),
            convert(Color.BLUE),
            convert(Color.parseColor("#880088")),
            convert(Color.parseColor("#FF0088")),
            convert(Color.RED),
            convert(Color.parseColor("#FF8800")),
            convert(Color.parseColor("#888800")),
            convert(Color.YELLOW),};

    /**
     * LinearGradient 中，顏色的座標值為[0,1]，此值為每個顏色之間的間隔
     **/
    private static final float DELTA_OF_NEXT_COLOR = 1f / (COLORS.length - 2);

    private static final float HALF_OF_DELTA_OF_NEXT_COLOR = DELTA_OF_NEXT_COLOR / 2;

    /**
     * 所有顏色的位置，POSITIONS 長度必須和 {@link #COLORS} 一樣
     **/
    private static final float[] POSITIONS = new float[]{
            0,
            HALF_OF_DELTA_OF_NEXT_COLOR,
            HALF_OF_DELTA_OF_NEXT_COLOR + DELTA_OF_NEXT_COLOR,
            HALF_OF_DELTA_OF_NEXT_COLOR + DELTA_OF_NEXT_COLOR * 2,
            HALF_OF_DELTA_OF_NEXT_COLOR + DELTA_OF_NEXT_COLOR * 3,
            HALF_OF_DELTA_OF_NEXT_COLOR + DELTA_OF_NEXT_COLOR * 4,
            HALF_OF_DELTA_OF_NEXT_COLOR + DELTA_OF_NEXT_COLOR * 5,
            HALF_OF_DELTA_OF_NEXT_COLOR + DELTA_OF_NEXT_COLOR * 6,
            HALF_OF_DELTA_OF_NEXT_COLOR + DELTA_OF_NEXT_COLOR * 7,
            1
    };

    /**
     * fps
     **/
    private static final float FPS = 16;

    /**
     * 速率：pixel/ms
     **/
    private static final float VELOCITY = 0.3f;

    private final Paint mRgbPaint;
    private final Rect canvasRect;

    private float mDeltaPixel;
    private float mDeltaPixelMore;
    private long mLastDrawCanvasTime;
    private long mLastDrawCanvasTimeMore;

    /**
     * for draw border
     **/
    @Nonnull
    private final Paint mBorderPaint;
    @Nonnull
    private final RectF mBorderRectF;

    private static int convert(int color) {
        float[] a = new float[3];
        Color.colorToHSV(color, a);
        a[1] = 1;
        a[2] = 1;
        int c = Color.HSVToColor(a);
        return c;
    }

    public RGBKeyboardRender() {
        this.mRgbPaint = new Paint();
        this.canvasRect = new Rect();
        PorterDuffXfermode porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY);
        mRgbPaint.setXfermode(porterDuffXfermode);
        mDeltaPixel = 0f;
        mDeltaPixelMore = 0f;
        mLastDrawCanvasTime = 0L;
        mLastDrawCanvasTimeMore = 0L;
        mBorderPaint = new Paint();
        mBorderRectF = new RectF();
    }

    @Override
    public void onDrawKeyBackground(int keyboardType, @Nonnull Key key, int keyStatus, @Nonnull KeyDrawParams keyDrawParams, @Nonnull KeyboardDrawParams keyboardDrawParams, @Nonnull Drawable keyBackground, @Nonnull Canvas canvas, @Nonnull Paint paint) {
        super.onDrawKeyBackground(keyboardType, key, keyStatus, keyDrawParams, keyboardDrawParams, keyBackground, canvas, paint);
        final Rect padding = keyboardDrawParams.keyBackgroundPadding;
        final int bgWidth, bgHeight, bgX, bgY;
        final int keyWidth = key.getDrawWidth();
        final int keyHeight = key.getHeight();
        bgWidth = keyWidth + padding.left + padding.right;
        bgHeight = keyHeight + padding.top + padding.bottom;
        bgX = -padding.left;
        bgY = -padding.top;
        canvas.translate(bgX, bgY);
        onDrawKeyBorder(key, canvas, keyDrawParams, bgWidth, bgHeight);
        canvas.translate(-bgX, -bgY);

    }

    /**
     * Key border
     **/
    private void onDrawKeyBorder(Key key, Canvas canvas, KeyDrawParams params, int bgWidth, int bgHeight) {
        if (!Settings.getInstance().getCurrent().mKeyBorderShown) {
            return;
        }
        if (key.isSpacebarKey()) {
            return;
        }
        mBorderPaint.setStrokeWidth(params.mKeyBorderWidth);
        mBorderPaint.setColor(Color.WHITE);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderRectF.left = 0;
        mBorderRectF.top = 0;
        mBorderRectF.right = bgWidth;
        mBorderRectF.bottom = bgHeight;
        canvas.drawRoundRect(mBorderRectF, params.mKeyBorderRadius, params.mKeyBorderRadius, mBorderPaint);
    }

    @Override
    public void afterDrawKeyboard(int keyboardType, @Nonnull Keyboard keyboard, @Nonnull KeyDrawParams keyDrawParams, @Nonnull KeyboardDrawParams keyboardDrawParams, @Nonnull Canvas canvas) {
        if (keyboardType == KeyboardType.EMOJI_PAGE_KEYBOARD) {
            return;
        }
        super.afterDrawKeyboard(keyboardType, keyboard, keyDrawParams, keyboardDrawParams, canvas);
        if (keyboardType == KeyboardType.MORE_KEYS_KEYBOARD) {
            drawMorekeyBackground(canvas);
        } else {
            drawBackground(canvas);
        }
    }

    @Override
    public void onDrawKeyPreviewText(@Nonnull Canvas canvas, @Nullable Bitmap textBitmap) {
        super.onDrawKeyPreviewText(canvas, textBitmap);
        if (textBitmap != null) {
            Rect canvasRect = new Rect();
            canvasRect.left = 0;
            canvasRect.top = 0;
            canvasRect.right = textBitmap.getWidth();
            canvasRect.bottom = textBitmap.getHeight();
            canvas.drawRect(canvasRect, mRgbPaint);

        }
    }

    @Override
    public boolean isInvalidationNeeded() {
        return true;
    }

    private void drawMorekeyBackground(Canvas canvas) {
        final long currentTime = System.currentTimeMillis();
        final int width = canvas.getWidth();
        final int height = canvas.getHeight();

        // first time
        if (mLastDrawCanvasTimeMore == 0L) {
            mLastDrawCanvasTimeMore = currentTime;
        }

        // update delta pixel
        final long deltaTime = currentTime - mLastDrawCanvasTimeMore;
        if (deltaTime >= FPS) {
            mLastDrawCanvasTimeMore = currentTime;
            final float addedDeltaPixel = VELOCITY * deltaTime;
            mDeltaPixelMore += addedDeltaPixel;

            float pathLength = height;
            if (mDeltaPixelMore > pathLength) {
                mDeltaPixelMore %= pathLength;
            }
        }
        // draw
        LinearGradient linearGradient = new LinearGradient(
                0 - mDeltaPixelMore, -mDeltaPixelMore,
                height - mDeltaPixelMore, height - mDeltaPixelMore,
                COLORS,
                POSITIONS,
                Shader.TileMode.REPEAT);

        mRgbPaint.setShader(linearGradient);
        canvasRect.left = 0;
        canvasRect.top = 0;
        canvasRect.right = width;
        canvasRect.bottom = height;
        canvas.drawRect(canvasRect, mRgbPaint);
    }

    private void drawBackground(Canvas canvas) {
        final long currentTime = System.currentTimeMillis();
        final int width = canvas.getWidth();
        final int height = canvas.getHeight();

        // first time
        if (mLastDrawCanvasTime == 0L) {
            mLastDrawCanvasTime = currentTime;
        }

        // update delta pixel
        final long deltaTime = currentTime - mLastDrawCanvasTime;
        if (deltaTime >= FPS) {
            mLastDrawCanvasTime = currentTime;
            final float addedDeltaPixel = VELOCITY * deltaTime;
            mDeltaPixel += addedDeltaPixel;

            float pathLength = height;
            if (mDeltaPixel > pathLength) {
                mDeltaPixel %= pathLength;
            }
        }
        // draw
        LinearGradient linearGradient = new LinearGradient(
                0 - mDeltaPixel, -mDeltaPixel,
                height - mDeltaPixel, height - mDeltaPixel,
                COLORS,
                POSITIONS,
                Shader.TileMode.REPEAT);

        mRgbPaint.setShader(linearGradient);
        canvasRect.left = 0;
        canvasRect.top = 0;
        canvasRect.right = width;
        canvasRect.bottom = height;
        canvas.drawRect(canvasRect, mRgbPaint);
    }
}
