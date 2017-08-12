package com.lynxsolutions.intern.sappi.news;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lynxsolutions.intern.sappi.R;

/**
 * Created by farkaszsombor on 03.08.2017.
 */

public class RouteHolder extends RecyclerView.ViewHolder{

    private TextView from,to;
    private Button btn;

    public RouteHolder(View v) {
        super(v);
        from = (TextView) v.findViewById(R.id.map_from_text);
        to = (TextView)v.findViewById(R.id.map_to_text);
        btn = v.findViewById(R.id.button_route);
    }

    public TextView getFrom() {
        return from;
    }

    public void setFrom(TextView from) {
        this.from = from;
    }

    public TextView getTo() {
        return to;
    }

    public void setTo(TextView to) {
        this.to = to;
    }

    public Button getBtn() {
        return btn;
    }

    public void setBtn(Button btn) {
        this.btn = btn;
    }
}
