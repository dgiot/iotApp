package android.tx.com.dgiot_amis.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.tx.com.dgiot_amis.R;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;



/**
 * 作者：Jie
 * 标题View
 */
public class BaseTopLayout extends LinearLayout {
    private String strTitle = "",strRight = "";
    private ImageView imgRight,imgBack , imgRight2;
    private int imgId,textRightColor ,imgId2;
    private TextView textTitle,textRight;
    private Context context;
    private LinearLayout frame1 , frame2;

    public BaseTopLayout(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.model_base_top_layout, this);
        TypedArray ta = context.obtainStyledAttributes(attributeSet, R.styleable.BaseTopAttr, 0, 0);
        try {
            if (ta.hasValue(R.styleable.BaseTopAttr_textTitle)) {
                strTitle = ta.getString(R.styleable.BaseTopAttr_textTitle);
            }
            if(ta.hasValue(R.styleable.BaseTopAttr_textRight)) {
                strRight = ta.getString(R.styleable.BaseTopAttr_textRight);
            }
            if(ta.hasValue(R.styleable.BaseTopAttr_imgRight)) {
                imgId =  ta.getResourceId(R.styleable.BaseTopAttr_imgRight, 0);
            }
            if(ta.hasValue(R.styleable.BaseTopAttr_imgRight2)) {
                imgId2 =  ta.getResourceId(R.styleable.BaseTopAttr_imgRight2, 0);
            }
            if(ta.hasValue(R.styleable.BaseTopAttr_textRightColor)) {
                textRightColor = ta.getColor(R.styleable.BaseTopAttr_textRightColor, Color.BLACK);
            }
            initView();
        } finally {
            ta.recycle();
        }
    }

    private void initView() {
        textRight = (TextView) findViewById(R.id.base_top_text_right);
        textTitle = (TextView) findViewById(R.id.base_top_text_title);
        imgRight  = (ImageView)findViewById(R.id.base_top_img_right);
        imgBack   = (ImageView)findViewById(R.id.base_top_img_back);
        frame1    =  findViewById(R.id.base_top_frame_right);
        frame2    =  findViewById(R.id.base_top_frame_right2);

        imgRight2  = (ImageView)findViewById(R.id.base_top_img_right2);

        if("".equals(strTitle)){
            textTitle.setText("");
        }else{
            textTitle.setText(strTitle);
        }
        if("".equals(strRight)){
            textRight.setVisibility(GONE);
        }else{
            textRight.setText(strRight);
        }
        if(imgId != 0) {
            imgRight.setImageDrawable(getContext().getResources().getDrawable(imgId));
            frame1.setVisibility(VISIBLE);
        }
        if(imgId2 != 0) {
            imgRight2.setImageDrawable(getContext().getResources().getDrawable(imgId2));
            frame2.setVisibility(VISIBLE);
        }

        imgBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((Activity)context).finish();
                ((Activity) getContext()).overridePendingTransition(0, R.anim.base_slide_right_out);
            }
        });

        frame2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnRightClickLitener != null){
                    mOnRightClickLitener.onRightClick2();
                }
            }
        });
        imgRight2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnRightClickLitener != null){
                    mOnRightClickLitener.onRightClick2();
                }
            }
        });
        imgRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnRightClickLitener != null){
                    mOnRightClickLitener.onRightClick();
                }
            }
        });
        frame1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnRightClickLitener != null){
                    mOnRightClickLitener.onRightClick();
                }
            }
        });
        textRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mOnRightClickLitener != null){
                    mOnRightClickLitener.onRightClick();
                }
            }
        });

    }

    public void setTextTitle(String title){
        if(textTitle != null && textTitle.getVisibility() == VISIBLE){
            textTitle.setText(title);
        }
    }

    public void setRightTitle(String title){
        if(textRight != null){
            if ( title.length() > 0 ) {
                textRight.setVisibility(VISIBLE);
            }else{
                textRight.setVisibility(GONE);
            }
            textRight.setText(title);
        }
    }

    public void setTextRightGone(){
        textRight.setVisibility(GONE);
    }

    public void setTextRightVisible(){
        textRight.setVisibility(VISIBLE);
    }

    public void setImgRightGone(){
        frame1.setVisibility(GONE);
    }

    public void setImgRight2Gone(){
        frame2.setVisibility(GONE);
    }

    public void setBackGone(){
        if(imgBack != null){
            imgBack.setVisibility(GONE);
        }
    }


    public interface OnRightClickLitener{
        void onRightClick();
        void onRightClick2();
    }
    public OnRightClickLitener mOnRightClickLitener;

    public void setOnRightClickLitener(OnRightClickLitener onRightClickLitener){
        this.mOnRightClickLitener = onRightClickLitener;
    }



    /**
     * drawable转bitmap
     *
     * @param drawable
     * @return
     */
    private Bitmap drawableToBitmap(Drawable drawable)
    {
        if (drawable == null) {
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(30, 30, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, 30, 30);
        drawable.draw(canvas);
        return bitmap;
    }
}
