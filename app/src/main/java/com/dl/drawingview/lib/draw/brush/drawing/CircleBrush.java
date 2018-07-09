package com.dl.drawingview.lib.draw.brush.drawing;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.NonNull;

import com.dl.drawingview.lib.draw.brush.Brush;
import com.dl.drawingview.lib.draw.model.DrawingPath;
import com.dl.drawingview.lib.draw.model.DrawingPoint;
import com.dl.drawingview.lib.draw.helper.resource.Resource;

import imdemo.dl.com.drawingview.R;


/**
 * CircleBrush
 * AndroidDrawingView <com.vilyever.drawingview.brush>
 * Created by vilyever on 2015/10/21.
 * Feature:
 * 约束圆绘制，即起点和终点围成的矩形的中心圆
 */
public class CircleBrush extends ShapeBrush {
    final CircleBrush self = this;

    
    /* #Constructors */
    public CircleBrush() {

    }

    public CircleBrush(float size, int color) {
        this(size, color, FillType.Hollow);
    }

    public CircleBrush(float size, int color, FillType fillType) {
        this(size, color, fillType, false);
    }

    public CircleBrush(float size, int color, FillType fillType, boolean edgeRounded) {
        super(size, color, fillType, edgeRounded);
    }

    /* Public Methods */
    public static CircleBrush defaultBrush() {
        return new CircleBrush(Resource.getDimensionPixelSize(R.dimen.drawingViewBrushDefaultSize), Color.BLACK);
    }

    /* #Overrides */
    @Override
    public boolean isEdgeRounded() {
        return true;
    }

    @NonNull
    @Override
    public Brush.Frame drawPath(Canvas canvas, @NonNull DrawingPath drawingPath, @NonNull Brush.DrawingState state) {
        updatePaint();
        if (drawingPath.getPoints().size() > 1) {
            DrawingPoint beginPoint = drawingPath.getPoints().get(0);
            DrawingPoint lastPoint = drawingPath.getPoints().get(drawingPath.getPoints().size() - 1);

            float centerX = (beginPoint.getX() + lastPoint.getX()) / 2.0f;
            float centerY = (beginPoint.getY() + lastPoint.getY()) / 2.0f;
            float radius = Math.min(Math.abs(beginPoint.getX() - lastPoint.getX()), Math.abs(beginPoint.getY() - lastPoint.getY())) / 2.0f;

            RectF drawingRect = new RectF();
            drawingRect.left = centerX - radius;
            drawingRect.top = centerY - radius;
            drawingRect.right = centerX + radius;
            drawingRect.bottom = centerY + radius;

            if ((drawingRect.right - drawingRect.left) < getSize()
                    || (drawingRect.bottom - drawingRect.top) < getSize()) {
                return Brush.Frame.EmptyFrame();
            }

            Brush.Frame pathFrame = makeFrameWithBrushSpace(drawingRect);

            if (state.isFetchFrame() || canvas == null) {
                return pathFrame;
            }

            Path path = new Path();
            path.addCircle(centerX, centerY, radius, Path.Direction.CW);

            if (state.isCalibrateToOrigin()) {
                path.offset(-pathFrame.left, -pathFrame.top);
            }

            canvas.drawPath(path, getPaint());

            return pathFrame;
        }

        return Brush.Frame.EmptyFrame();
    }

}