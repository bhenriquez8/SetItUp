package com.example.bjarne.setitup;

import android.widget.TextView;

/**
 * Created by bjarne on 10/15/15.
 */
public class WakeViewHolder {
    private TextView wakeView;
    private TextView messageView;
    public WakeViewHolder() {}
    public WakeViewHolder(TextView messageView, TextView wakeView) {
        this.wakeView = wakeView;
        this.messageView = messageView;
    }

    public TextView getWakeView() {
        return wakeView;
    }

    public void setWakeView(TextView wakeView) {
        this.wakeView = wakeView;
    }

    public TextView getMessageView() {
        return messageView;
    }

    public void setMessageView(TextView messageView) {
        this.messageView = messageView;
    }
}
