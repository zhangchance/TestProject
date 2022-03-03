package com.example.zccproject;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zccproject.view.RenderView;

public class SecondActivity extends AppCompatActivity {
    private TextView mMoveView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        final RenderView testRenderThreadOff = findViewById(R.id.test_off);
        final RenderView testRenderThreadOn = findViewById(R.id.test_on);
        mMoveView = findViewById(R.id.test_move);
        testRenderThreadOn.setUseRenderThread(true);

        findViewById(R.id.button_test).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(final View button) {
                        startTest(button, testRenderThreadOff, testRenderThreadOn);
                    }
                });
    }
    private void startTest(final View button, RenderView... renderViews) {

        // start animation for 3s
        button.setEnabled(false);
        for (RenderView renderView : renderViews) {
            renderView.startAnimation(3000);
        }

        // after 1s -> pause UI thread (for 1s)
        button.postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        pauseUiThread(1000);
                    }
                },
                1000);

        // after 3s -> re-enable button
        button.postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        button.setEnabled(true);
                    }
                },
                3000);

        mMoveView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        ObjectAnimator animator = ObjectAnimator.ofFloat(mMoveView, "translationX", 0f, 1200f);
        animator.setDuration(3000);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mMoveView.setLayerType(View.LAYER_TYPE_NONE, null);
            }
        });
        animator.start();
    }

    void pauseUiThread(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Toast.makeText(this, "Pause interrupted", Toast.LENGTH_SHORT).show();
        }
    }
}
