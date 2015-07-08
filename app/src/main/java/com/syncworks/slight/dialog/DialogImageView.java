package com.syncworks.slight.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.syncworks.slight.R;

/**
 * Created by Kim on 2015-07-07.
 */
public class DialogImageView extends Dialog{

    ImageView imgView;
    Button btnClose;

    private Drawable imgDrawable = null;

    private OnCloseListener closeListener = null;


    public DialogImageView(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);*/
        setContentView(R.layout.dialog_image_view);
        this.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        imgView = (ImageView) findViewById(R.id.image_view);
        btnClose = (Button) findViewById(R.id.btn_close);

        if (imgDrawable != null) {
            imgView.setImageDrawable(imgDrawable);
        }

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBtnClose();
            }
        });
    }

    public void setImgView(Drawable drawable) {
        imgDrawable = drawable;
    }


    public interface OnCloseListener {
        void onBtnClose();
    }

    public void setOnCloseListener(OnCloseListener listener) {
        closeListener = listener;
    }

    private void onBtnClose() {
        if (closeListener != null) {
            closeListener.onBtnClose();
        }
    }
}
