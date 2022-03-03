package com.example.zccproject.view;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;


import com.example.zccproject.renderthread.CanvasProperty;
import com.example.zccproject.renderthread.RenderThread;

public class RenderView extends FrameLayout {
    private boolean useRenderThread = false;

    private Animator radiusAnimator;
    private Animator alphaAnimator;
    private Animator transAnimator;

    private CanvasProperty<Float> centerXProperty;
    private CanvasProperty<Float> centerYProperty;
    private CanvasProperty<Float> radiusProperty;
    private CanvasProperty<Paint> paintProperty;

    private boolean animateNext;

    private long animationDurationMillis;

    public RenderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
    }

    public void setUseRenderThread(boolean useRenderThread) {
        this.useRenderThread = useRenderThread;
    }

    public void startAnimation(long durationMillis) {
        animationDurationMillis = durationMillis;
        animateNext = true;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (animateNext) {
            initialiseAnimation(canvas);
            animateNext = false;
        }

        if (centerXProperty != null && centerYProperty != null && radiusProperty != null && paintProperty != null) {
            RenderThread.drawCircle(canvas, centerXProperty, centerYProperty, radiusProperty, paintProperty);
        }
    }

    private void initialiseAnimation(Canvas canvas) {
        float width = getWidth();
        float height = getHeight();
        float centerX = width / 2f;
        float centerY = height / 2f;
        float initialRadius = 0f;
        float targetRadius = Math.min(width, height) / 2f;
        float targetAlpha = 0f;
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);

        centerXProperty = RenderThread.createCanvasProperty(canvas, centerX, useRenderThread);
        centerYProperty = RenderThread.createCanvasProperty(canvas, centerY, useRenderThread);
        radiusProperty = RenderThread.createCanvasProperty(canvas, initialRadius, useRenderThread);
        paintProperty = RenderThread.createCanvasProperty(canvas, paint, useRenderThread);

        if (radiusAnimator != null) {
            radiusAnimator.cancel();
        }
        radiusAnimator = RenderThread.createFloatAnimator(this, canvas, radiusProperty, targetRadius);
        radiusAnimator.setInterpolator(new LinearInterpolator());
        radiusAnimator.setDuration(animationDurationMillis);
        radiusAnimator.start();

        if (alphaAnimator != null) {
            alphaAnimator.cancel();
        }
        alphaAnimator = RenderThread.createPaintAlphaAnimator(this, canvas, paintProperty, targetAlpha);
        alphaAnimator.setDuration(animationDurationMillis);
        alphaAnimator.start();

        if (transAnimator != null) {
            transAnimator.cancel();
        }
        transAnimator = RenderThread.createFloatAnimator(this, canvas, centerXProperty, centerX * 2);
        transAnimator.setDuration(animationDurationMillis);
        transAnimator.start();
    }
}
