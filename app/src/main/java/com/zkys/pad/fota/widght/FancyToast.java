package com.zkys.pad.fota.widght;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zkys.pad.fota.R;
import com.zkys.pad.fota.base.Global;

/**
 * 统一Toast
 */
public class FancyToast {

    public static Toast makeText(String msg) {
        return makeText(Global.getContext(), msg, Toast.LENGTH_SHORT);
    }

    public static Toast makeText(Context context, String message, int duration) {
        Toast toast = new Toast(context);
        toast.setDuration(duration);
        View layout = LayoutInflater.from(context).inflate(R.layout.fancytoast_layout, (ViewGroup) null, false);
        TextView tvMst = layout.findViewById(R.id.toast_text);
        tvMst.setText(message);
        toast.setView(layout);
        return toast;
    }
}
