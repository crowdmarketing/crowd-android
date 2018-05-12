package net.jspiner.crowd.ui.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import net.jspiner.crowd.R;

public class LoadingDialog extends AlertDialog {

    protected LoadingDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_loading);
    }
}
