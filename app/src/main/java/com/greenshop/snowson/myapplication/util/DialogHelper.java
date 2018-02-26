package com.greenshop.snowson.myapplication.util;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;

import com.greenshop.snowson.myapplication.R;

/**
 * Created by snowson on 18-2-26.
 */

public class DialogHelper {
    public static Dialog getLoadingDialog(Context context) {
        Dialog dialog = new Dialog(context, R.style.dialog);
        dialog.setContentView(R.layout.layout_dialog);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setDimAmount(0.1f);
        return dialog;
    }
}
