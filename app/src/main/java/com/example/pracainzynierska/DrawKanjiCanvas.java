package com.example.pracainzynierska;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class DrawKanjiCanvas extends SurfaceView implements SurfaceHolder.Callback{

    private Paint paint;
    private float startX, startY, endX, endY, canvasHeight, canvasWidth, textSize = 750;
    private SurfaceHolder surfaceHolder;
    private Path path;
    private List<Path> paths = new ArrayList<>();
    boolean enabled = true;
    public void drawingEnabled(boolean isEnabled){
        enabled = isEnabled;
    }
    public DrawKanjiCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
        surfaceHolder = getHolder();

        paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(15f);
        paint.setTextSize(textSize);

        path = new Path();
        surfaceHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        SurfaceHolder surfaceHolder = getHolder();
        Canvas canvas = surfaceHolder.lockCanvas();
        canvas.drawColor(Color.rgb(0x4A, 0x4A, 0x4A));
        canvas.drawPath(path, paint);
        surfaceHolder.unlockCanvasAndPost(canvas);

        canvasHeight = surfaceHolder.lockCanvas().getHeight();
        surfaceHolder.unlockCanvasAndPost(canvas);
        canvasWidth = surfaceHolder.lockCanvas().getWidth();
        surfaceHolder.unlockCanvasAndPost(canvas);
        restart();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(enabled) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startX = event.getX();
                    startY = event.getY();
                    path = new Path();
                    path.moveTo(startX, startY);
                    paths.add(path);

                    break;
                case MotionEvent.ACTION_MOVE:
                    endX = event.getX();
                    endY = event.getY();
                    path.lineTo(endX, endY);
                    break;
                case MotionEvent.ACTION_UP:
                    break;
            }
            drawPath(true);
        }
        return true;
    }
    private void drawPath(boolean drawLastStroke) {
            SurfaceHolder surfaceHolder = getHolder();
            Canvas canvas = surfaceHolder.lockCanvas();
            canvas.drawColor(Color.rgb(0x4A, 0x4A, 0x4A));
            for (Path p : paths) {
                canvas.drawPath(p, paint);
            }
            if(drawLastStroke) {
                canvas.drawPath(path, paint);
            }
            surfaceHolder.unlockCanvasAndPost(canvas);

    }



    public void drawKanji(String kanji){
        Canvas canvas = surfaceHolder.lockCanvas();
        if (canvas != null) {
            paint.setColor(Color.argb(128, 255, 255, 255));
            textSize = canvasWidth / 2 + textSize / 2;
            if(textSize > canvasHeight){
                textSize = canvasHeight * 3 / 4;
            }
            if(textSize > canvasWidth){
                textSize = canvasWidth * 3 / 4;
            }

            paint.setTextSize((textSize));
            canvas.drawText(kanji, (canvasWidth/2) -(textSize / 2), (canvasHeight/4 * 3), paint);
            paint.setColor(Color.GREEN);
            surfaceHolder.unlockCanvasAndPost(canvas);
        }

    }

    public void undoLastStroke() {
        if (!paths.isEmpty()) {
            paths.remove(paths.size() - 1);
            if(!paths.isEmpty()) {
                drawPath(false);
            }
            else{
                restart();
            }
        }
    }

    public void restart(){
        path.reset();
        paths.clear();
        drawPath(true);
    }

}
