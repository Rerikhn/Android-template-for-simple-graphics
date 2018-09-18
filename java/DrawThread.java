import android.annotation.SuppressLint;
import android.graphics.*;
import android.hardware.SensorEventListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;

import java.util.Random;

// Main class of all graphics with thread

public class DrawThread extends Thread {

    private DisplayMetrics metrics;
    private final SurfaceHolder surfaceHolder;
    private long prevTime;
    private Paint paint;
    private boolean runFlag;
    private float scaleX, scaleY;
    private DisplayMetrics metrics;

    @SuppressLint("ClickableViewAccessibility")
    DrawThread(SurfaceHolder surfaceHolder, DisplayMetrics metrics) {
        this.surfaceHolder = surfaceHolder;
        this.metrics = metrics;
        // Paint
        paint = new Paint();
        paint.setAntiAlias(true);
        
        // multiply for every x or y point in canvas for scaling (16:9 only)
        scaleY = metrics.heightPixels / 1280.f;
        scaleX = metrics.widthPixels / 720.f;
        
        // load components for graphics
        
        // save current time
        prevTime = System.currentTimeMillis();
    }

    void setRunning(boolean run) {
        runFlag = run;
    }

    @Override
    public void run() {
        Canvas canvas;
        while (!runFlag) {
            try {
                surfaceHolder.wait();
            } catch (InterruptedException ignored) {
            }
        }
        while (runFlag) {
            // get current time and calculate difference with older time
            long now = System.currentTimeMillis();
            long elapsedTime = now - prevTime;
            // 1 - second = 1000 milliseconds
            //  60 frames per second = (1/60s) * 1000ms ~ 16.6ms
            if (elapsedTime > 16) {
                //if time > N milliseconds, save current time
                prevTime = now;
                //updateFrame(); // picture update with 60 fps (only for moving objects)
            }
            canvas = null;
            try {
                // get Canvas and create drawings
                canvas = surfaceHolder.lockCanvas(null);
                if (canvas != null) synchronized (surfaceHolder) {
                    // graphics
                    canvas.drawColor(Color.BLACK);
                    
                    // multiply for scaleXY is necessary 
                }
            } finally {
                if (canvas != null) {
                    // if graphics done, set it on display
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

    private void updateFrame() {
        // supporting method for graphics update
        // only for moving objects
    }

    /**
     * This function is remapping line value
     * Example:
     * 0 from 10
     * remap(4 from 0  to 10) = 40 from 0 to 100
     *
     * @param s  value to find
     * @param a1 start value 1
     * @param a2 stop value 1
     * @param b1 start value 2
     * @param b2 start value 2
     * @return returns a float value
     */
    private float reMap(float s, float a1, float a2, float b1, float b2) {
        return b1 + (s - a1) * (b2 - b1) / (a2 - a1);
    }

}


