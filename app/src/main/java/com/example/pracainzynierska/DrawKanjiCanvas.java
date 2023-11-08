package com.example.pracainzynierska;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class DrawKanjiCanvas extends SurfaceView implements SurfaceHolder.Callback{

    private Paint paint;
    private float startX, startY, endX, endY, canvasHeight, canvasWidth;
    private SurfaceHolder surfaceHolder;
    private Path path;
    ArrayList <Path> previousPaths = new ArrayList<Path>();

    public DrawKanjiCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(15f);
        paint.setTextSize(40);
        path = new Path();


    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        Canvas canvas = surfaceHolder.lockCanvas();
        canvas.drawColor(Color.BLACK);
        surfaceHolder.unlockCanvasAndPost(canvas);
        canvasHeight = surfaceHolder.lockCanvas().getHeight();
        surfaceHolder.unlockCanvasAndPost(canvas);
        canvasWidth = surfaceHolder.lockCanvas().getWidth();
        surfaceHolder.unlockCanvasAndPost(canvas);


    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                path.moveTo(startX, startY);

                break;
            case MotionEvent.ACTION_MOVE:
                endX = event.getX();
                endY = event.getY();
                path.lineTo(endX,endY);
                drawPath();
                //drawKanji("A");
                break;
            case MotionEvent.ACTION_UP:
                previousPaths.add(path);

        }
        return true;
    }
    private void drawPath() {

            Canvas canvas = surfaceHolder.lockCanvas();

            canvas.drawColor(Color.BLACK);
            canvas.drawPath(path, paint);
            surfaceHolder.unlockCanvasAndPost(canvas);
        }

    public void drawKanji(String kanji){
        Canvas canvas = surfaceHolder.lockCanvas();
        if (canvas != null) {
            paint.setColor(Color.GRAY);
            System.out.println("AAAAAAAAAAAAA");
            canvas.drawText(kanji, canvasWidth/2, canvasHeight/2, paint);
            paint.setColor(Color.WHITE);
            surfaceHolder.unlockCanvasAndPost(canvas);
        }

    }

    public void undoLastStroke() {

        if (!previousPaths.isEmpty()) {
            path.reset();
            path = previousPaths.get(previousPaths.size() - 2);
            //path.addPath(previousPaths.get(previousPaths.size() - 2));
            System.out.println(previousPaths.size());
            previousPaths.remove(previousPaths.get(previousPaths.size() - 1));

            drawPath();
        }
    }

}
