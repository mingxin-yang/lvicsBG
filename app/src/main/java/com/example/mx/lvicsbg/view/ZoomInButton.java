package com.example.mx.lvicsbg.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageButton;

import com.example.mx.lvicsbg.R;

/**
 * Created by XIN on 2018/3/15.
 */

@SuppressLint("AppCompatCustomView")
public class ZoomInButton extends ImageButton {
    private float mCircleSize;
    private float mShadowRadius;
    private float mShadowOffset;
    int mPlusColor;
    int mColorPressed;
    public ZoomInButton(Context context) {
        super(context);
    }

    public ZoomInButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public ZoomInButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }
    void init(Context context, AttributeSet attributeSet) {
        TypedArray attr = context.obtainStyledAttributes(attributeSet,
                R.styleable.AddFloatingActionButton, 0, 0);
        mPlusColor = attr.getColor(R.styleable.AddFloatingActionButton_fab_plusIconColor,
                getResources().getColor(R.color.half_black));
        mColorPressed = attr.getColor(R.styleable.FloatingActionButton_fab_colorPressed,
                getResources().getColor(R.color.white_pressed));
        mCircleSize = getResources().getDimension(R.dimen.fab_size_normal);
        mShadowRadius = getResources().getDimension(R.dimen.fab_shadow_radius);
        mShadowOffset = getResources().getDimension(R.dimen.fab_shadow_offset);
        attr.recycle();
        updateBackground();
    }
    Drawable getIconDrawable() {
        final float iconSize = getResources().getDimension(R.dimen.fab_icon_size);
        final float iconHalfSize = iconSize / 2f;

        final float plusSize = getResources().getDimension(R.dimen.fab_plus_icon_size);
        final float plusHalfStroke = getResources().getDimension(R.dimen.fab_plus_icon_stroke) / 2f;
        final float plusOffset = (iconSize - plusSize) / 2f;

        final Shape shape = new Shape() {
            @Override
            public void draw(Canvas canvas, Paint paint) {
                canvas.drawRect(plusOffset, iconHalfSize - plusHalfStroke,
                        iconSize - plusOffset, iconHalfSize + plusHalfStroke, paint);
                canvas.drawRect(iconHalfSize - plusHalfStroke, plusOffset,
                        iconHalfSize + plusHalfStroke, iconSize - plusOffset, paint);
            }
        };

        ShapeDrawable drawable = new ShapeDrawable(shape);

        final Paint paint = drawable.getPaint();
        paint.setColor(mPlusColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);

        return drawable;
    }
    void updateBackground(){
        LayerDrawable layerDrawable = new LayerDrawable(
                new Drawable[] {
                        getResources().getDrawable(R.drawable.fab_bg_normal),
                        createFillDrawable(1),
                        getIconDrawable()
                });
        int iconOffset = (int) (mCircleSize - getResources().getDimension(R.dimen.fab_icon_size)) / 2;
        int circleInsetHorizontal = (int) (mShadowRadius);
        int circleInsetTop = (int) (mShadowRadius - mShadowOffset);
        int circleInsetBottom = (int) (mShadowRadius + mShadowOffset);

        layerDrawable.setLayerInset(1,
                circleInsetHorizontal,
                circleInsetTop,
                circleInsetHorizontal,
                circleInsetBottom);
        layerDrawable.setLayerInset(2,
                circleInsetHorizontal + iconOffset,
                circleInsetTop + iconOffset,
                circleInsetHorizontal + iconOffset,
                circleInsetBottom + iconOffset);

        setBackgroundCompat(layerDrawable);
    }

    public void setBackgroundCompat(Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(drawable);
        } else {
            setBackgroundDrawable(drawable);
        }
    }
    private StateListDrawable createFillDrawable(float strokeWidth) {
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[] { android.R.attr.state_pressed }, createCircleDrawable(mColorPressed, strokeWidth));
        return drawable;
    }
    private Drawable createCircleDrawable(int color, float strokeWidth) {
        int alpha = Color.alpha(color);
        int opaqueColor = opaque(color);

        ShapeDrawable fillDrawable = new ShapeDrawable(new OvalShape());

        final Paint paint = fillDrawable.getPaint();
        paint.setAntiAlias(true);
        paint.setColor(opaqueColor);

        Drawable[] layers = {
                fillDrawable,
        };

        LayerDrawable drawable =
                new LayerDrawable(layers);
        return drawable;
    }
    private int opaque(int argb) {
        return Color.rgb(
                Color.red(argb),
                Color.green(argb),
                Color.blue(argb)
        );
    }

}
