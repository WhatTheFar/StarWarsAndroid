//Jakpat Mingmongkolmitr 5931217221
package com.example.starwars;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;


public class StarWarsView extends View {

    private boolean isNext;
    private int yPos,tempPos;
    private int textSize;
    private DrawingThread animator;
    private final int[][] x = {
            {0, -120, -240},
            {-105, -120, -135},
            {0, -120, -240}
    };
    private final int[][] y = {
            {0, 300, 0},
            {300, 360, 300},
            {460, 610, 460}
    };
    private final int[][] color = {
            {255, 123, 231, 211},
            {255, 172, 201, 199},
            {255, 192, 136, 100}
    };


    public StarWarsView(Context context) {
        super(context);
        init();
    }

    public StarWarsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public StarWarsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setSaveEnabled(true);
        isNext = false;
        yPos = 1000;
        tempPos = 0;
        textSize = 80;
        animator = new DrawingThread(this, 100, new Handler(Looper.getMainLooper()));
        animator.startAnimate();
    }

    public void restartAnimation() {
        isNext = false;
        yPos = 1000;
        tempPos = 0;
        textSize = 80;
        animator.delay = 100;
        animator.startAnimate();
    }

    public void decreaseDelay() {
        if (animator.delay/2 > 0)
            animator.delay /= 2;
    }

    public void increaseDelay() {
        animator.delay *= 2;
    }

    public void stopAnimate() {
        animator.stopAnimate();
    }

    public void startAnimate() {
        animator.startAnimate();
    }

    public boolean isAnimationStop() {
        return animator.animationThread == null;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = getWidth();
        int h = getHeight();

        if (isNext) {
//            animator.delay = 150;
            canvas.save();
            canvas.rotate(35, w, 0);
            for (int i = 0; i < 3; i++) {
                int temp = 0;
                if(i == 1) {
                    temp = tempPos;
                    tempPos += 10;
                    if(tempPos > 150)
                        tempPos = 0;
                }
                Paint paint = new Paint();
                paint.setStyle(Paint.Style.FILL_AND_STROKE);

                paint.setARGB(color[i][0], color[i][1], color[i][2], color[i][3]);

                Path path = new Path();
                path.moveTo(w + x[i][0], yPos + y[i][0] + temp);
                path.lineTo(w + x[i][1], yPos + y[i][1] + temp);
                path.lineTo(w + x[i][2], yPos + y[i][2] + temp);
                path.close();
                canvas.drawPath(path, paint);
            }
            canvas.restore();

            yPos += 20;
            return;
        }

        Paint paint = new Paint();
        paint.setTextSize(textSize);
        String text1 = "A long time ago,";
        String text2 = "in a galaxy far, far away...";
        Rect bound1 = new Rect();
        Rect bound2 = new Rect();
        paint.getTextBounds(text1, 0, text1.length(), bound1);
        paint.getTextBounds(text2, 0, text2.length(), bound2);
        canvas.drawText(text1, w / 2 - bound1.width() / 2, yPos, paint);
        canvas.drawText(text2, w / 2 - bound2.width() / 2, yPos + bound2.height() + 20, paint);

        textSize -= 2;
        yPos -= 40;

        if (yPos < 0) {
            isNext = true;
            yPos = -300;
        }

    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();

        BundleSavedState savedState = new BundleSavedState(superState);
        // Save Instance State(s) here to the 'savedState.getBundle()'
        savedState.getBundle().putInt("y_pos", yPos);
        savedState.getBundle().putInt("text_size", textSize);
        savedState.getBundle().putBoolean("is_next", isNext);
        savedState.getBundle().putInt("delay", animator.delay);
        Log.wtf("View", "onSave");
        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        BundleSavedState ss = (BundleSavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());

        Bundle bundle = ss.getBundle();
        // Restore State from bundle here
        yPos = bundle.getInt("y_pos");
        textSize = bundle.getInt("text_size");
        isNext = bundle.getBoolean("is_next");
        animator.delay = bundle.getInt("delay");
        Log.wtf("View", "onRestore");
    }

    class DrawingThread implements Runnable {
        private View view;
        private int delay;
        private Handler h;
        private Thread animationThread;

        DrawingThread(View view, int delay, Handler h) {
            this.view = view;
            this.delay = delay;
            this.h = h;
        }

        void startAnimate() {
            if (animationThread == null) {
                animationThread = new Thread(this);
                animationThread.start();
            }
        }

        void stopAnimate() {
            animationThread = null;
        }

        @Override
        public void run() {
            while (Thread.currentThread() == animationThread) {
                h.post(new Runnable() {
                    @Override
                    public void run() {
                        view.invalidate();
                        //h.postDelayed(this, delay);
                    }
                });
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
