package com.mh.biji;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MH on 2015-06-24.
 */
public class AreaView extends View {

    private class AreaDot {
        public int X;
        public int Y;
        public int C;
    }

    private List listDot;
    private AreaDot AD;

    private boolean isInit = true;

    public AreaView(Context context, AttributeSet attrs) {
        super(context, attrs);

        listDot = new ArrayList();
        AD = new AreaDot();
    };

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isInit) {
            for (int i=0; i<(int)listDot.size(); i++) {
                AreaDot d = (AreaDot)listDot.get(i);
                drawDot(canvas, d);
            }
        } else {
            drawDot(canvas, AD);
        }
    };

    private void drawDot(Canvas canvas, AreaDot d) {
        Paint p = new Paint();
        p.setColor(d.C);
        canvas.drawPoint(d.X, d.Y, p);
    }

    public void reDraw() {
        this.postInvalidate();
    }

    public void toDot(int x, int y, int c) {
        AreaDot d = new AreaDot();
        d.X = x;
        d.Y = y;
        d.C = c;
        listDot.add(d);
    }

    public void addDot(int x, int y, int c) {
        toDot(x, y, c);
        AD.X = x;
        AD.Y = y;
        AD.C = c;
        reDraw();
    }

}
