package com.example.zccproject.renderthread;

import androidx.annotation.NonNull;

import com.example.zccproject.renderthread.typeannotation.CanvasProperty;


public final class HardwareCanvasProperty<T> extends com.example.zccproject.renderthread.CanvasProperty<T> {

    @NonNull
    @CanvasProperty
    private final Object property;

    HardwareCanvasProperty(@CanvasProperty @NonNull Object property) {
        this.property = property;
    }

    @NonNull
    @CanvasProperty
    Object getProperty() {
        return property;
    }

    @Override
    public boolean isHardware() {
        return true;
    }
}
