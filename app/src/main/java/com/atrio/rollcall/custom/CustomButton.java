package com.atrio.rollcall.custom;

import android.content.Context;
import android.view.animation.ScaleAnimation;
import android.widget.Button;

/**
 * Created by Admin on 27-03-2017.
 */

public class CustomButton extends Button {
    public CustomButton(Context context) {
        super(context);

        ScaleAnimation anim = new ScaleAnimation(0,1,0,1);
        anim.setDuration(1000);
        anim.setFillAfter(true);
        this.startAnimation(anim);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

    }
}
