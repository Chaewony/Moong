package com.eustress.moong;

import android.graphics.drawable.GradientDrawable;

public class CloudGradientDrawable extends GradientDrawable {

    int mStartColor;
    int mCenterColor;
    int mEndColor;

    public CloudGradientDrawable(int pStartColor, int pCenterColor, int pEndColor, int pStrokeWidth, int pStrokeColor, float cornerRadius) {
        //super(Orientation.BOTTOM_TOP,new int[]{pStartColor,pCenterColor,pEndColor});
        super(Orientation.TR_BL,new int[]{pStartColor,pCenterColor,pEndColor});
        setStroke(pStrokeWidth,pStrokeColor);
        setShape(GradientDrawable.RECTANGLE);
        setCornerRadius(cornerRadius);

        mStartColor = pStartColor;
        mCenterColor = pCenterColor;
        mEndColor = pEndColor;
    }

    public void SetStartColor(int pStartColor) {
        mStartColor = pStartColor;
        this.setColors(new int[]{mStartColor,mCenterColor,mEndColor});
    }
}
