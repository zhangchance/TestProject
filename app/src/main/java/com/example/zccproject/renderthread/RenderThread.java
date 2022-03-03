package com.example.zccproject.renderthread;

import android.animation.Animator;
import android.graphics.Canvas;
import android.graphics.Paint;
import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.View;

import com.example.zccproject.renderthread.typeannotation.DisplayListCanvas;
import com.example.zccproject.renderthread.typeannotation.RenderNodeAnimator;


@SuppressWarnings("unused")
public final class RenderThread {

    private RenderThread() {
    }

    private static RenderThreadDelegate DELEGATE;

    static {
        init(false);
    }

    public static void init(boolean skipAndroidVersionCheck) {
        RenderThreadDelegate delegate = DELEGATE;
        if (delegate == null || !delegate.isSupported()) {
            RenderThreadMethods methods = RenderThreadMethods.create(skipAndroidVersionCheck);
            if (methods != null) {
                DELEGATE = new RenderThreadDelegateHw(methods);
            } else {
                DELEGATE = new RenderThreadDelegate();
            }
        }
    }

    public static boolean isSupported() {
        return DELEGATE.isSupported();
    }

    @NonNull
    public static CanvasProperty<Float> createCanvasProperty(@NonNull Canvas canvas, float initialValue) {
        return createCanvasProperty(canvas, initialValue, true);
    }

    @NonNull
    public static CanvasProperty<Float> createCanvasProperty(@NonNull Canvas canvas, float initialValue, boolean useRenderThread) {
        return DELEGATE.createCanvasProperty(canvas, initialValue, useRenderThread);
    }

    @NonNull
    public static CanvasProperty<Paint> createCanvasProperty(@NonNull Canvas canvas, @NonNull Paint initialValue) {
        return createCanvasProperty(canvas, initialValue, true);
    }

    @NonNull
    public static CanvasProperty<Paint> createCanvasProperty(@NonNull Canvas canvas, @NonNull Paint initialValue, boolean useRenderThread) {
        return DELEGATE.createCanvasProperty(canvas, initialValue, useRenderThread);
    }

    public static boolean isDisplayListCanvas(@NonNull Canvas canvas) {
        return DELEGATE.isDisplayListCanvas(canvas);
    }

    public static void setAnimatorTarget(@RenderNodeAnimator @NonNull Animator animator, @DisplayListCanvas @NonNull Canvas target) {
        DELEGATE.setTarget(animator, target);
    }

    public static void drawCircle(
            @NonNull Canvas canvas,
            @NonNull CanvasProperty<Float> cx,
            @NonNull CanvasProperty<Float> cy,
            @NonNull CanvasProperty<Float> radius,
            @NonNull CanvasProperty<Paint> paint) {

        DELEGATE.drawCircle(canvas, cx, cy, radius, paint);
    }

    public static void drawRoundRect(
            @NonNull Canvas canvas,
            @NonNull CanvasProperty<Float> left,
            @NonNull CanvasProperty<Float> top,
            @NonNull CanvasProperty<Float> right,
            @NonNull CanvasProperty<Float> bottom,
            @NonNull CanvasProperty<Float> rx,
            @NonNull CanvasProperty<Float> ry,
            @NonNull CanvasProperty<Paint> paint) {

        DELEGATE.drawRoundRect(canvas, left, top, right, bottom, rx, ry, paint);
    }

    @NonNull
    public static Animator createFloatAnimator(
            @NonNull View view,
            @NonNull Canvas canvas,
            @NonNull CanvasProperty<Float> property,
            float targetValue) {

        return DELEGATE.createFloatAnimator(view, canvas, property, targetValue);
    }

    @NonNull
    public static Animator createPaintAlphaAnimator(
            @Nullable View view,
            @NonNull Canvas canvas,
            @NonNull CanvasProperty<Paint> property,
            @FloatRange(from = 0f, to = 255f) float targetValue) {

        return DELEGATE.createPaintAlphaAnimator(view, canvas, property, targetValue);
    }

    @NonNull
    public static Animator createPaintStrokeWidthAnimator(
            @Nullable View view,
            @NonNull Canvas canvas,
            @NonNull CanvasProperty<Paint> property,
            float targetValue) {

        return DELEGATE.createPaintStrokeWidthAnimator(view, canvas, property, targetValue);
    }
}
