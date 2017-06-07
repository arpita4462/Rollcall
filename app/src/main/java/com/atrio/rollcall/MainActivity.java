package com.atrio.rollcall;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.atrio.rollcall.custom.CustomButton;
import com.atrio.rollcall.custom.CustomDialog;

public class MainActivity extends AppCompatActivity {

    private ImageView image;
    RelativeLayout rel;
    private CustomDialog cdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        image = (ImageView)findViewById(R.id.img_bg);
        rel = (RelativeLayout) findViewById(R.id.activity_main);
        cdd = new CustomDialog(MainActivity.this);
        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.myanimation);
        image.startAnimation(animation1);
        GifImageView gifImageView = (GifImageView) findViewById(R.id.GifImageView);
        gifImageView.setGifImageResource(R.drawable.anigif);

        final Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {

                // Log.i("Handler", "running");

                RelativeLayout.LayoutParams buttonParam = new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                CustomButton b1 = new CustomButton(MainActivity.this);
                b1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                b1.setText(R.string.button_tittle);
                b1.setTypeface(Typeface.MONOSPACE);
                b1.setTextColor(Color.parseColor("#000000"));
                b1.setBackgroundColor(Color.parseColor("#FFFFFF"));
                b1.setLayoutParams(buttonParam);
                buttonParam.setMargins(50,50,50,50);
                buttonParam.addRule(RelativeLayout.BELOW, R.id.img_bg);
                rel.addView(b1);

                b1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cdd = new CustomDialog(MainActivity.this);
                        cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        // overridePendingTransition(R.anim.left_right, R.anim.right_left);
                        cdd.show();
                    }
                });
            }
        };

        handler.postDelayed(r, 3000);

    }


    @Override
    protected void onResume() {
        super.onResume();
        //Log.i("Calling123","ONRESUME");
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Log.i("Calling098","ONPause");
        cdd.dismiss();
    }
}
