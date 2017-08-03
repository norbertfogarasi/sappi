package com.lynxsolutions.intern.sappi.news;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lynxsolutions.intern.sappi.R;

/**
 * Created by farkaszsombor on 03.08.2017.
 */

public class ViewHolder1 extends RecyclerView.ViewHolder {

    private TextView label1;
    private Button btn;
    ImageView image;
    public ViewHolder1(View v) {
        super(v);
        label1 = (TextView) v.findViewById(R.id.text1);
        image = (ImageView) v.findViewById(R.id.imageView4);
        btn = (Button) v.findViewById(R.id.button);
    }

    public TextView getLabel1() {
        return label1;
    }

    public void setLabel1(TextView label1) {
        this.label1 = label1;
    }

    public Button getBtn() {
        return btn;
    }
    public void setBtn(Button btn) {
        this.btn = btn;
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }
}
