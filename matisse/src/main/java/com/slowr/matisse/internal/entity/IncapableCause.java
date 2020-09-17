/*
 * Copyright 2017 Zhihu Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an &quot;AS IS&quot; BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.slowr.matisse.internal.entity;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IntDef;
import androidx.fragment.app.FragmentActivity;

import com.slowr.matisse.R;
import com.slowr.matisse.internal.ui.widget.IncapableDialog;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

@SuppressWarnings("unused")
public class IncapableCause {
    public static final int TOAST = 0x00;
    public static final int DIALOG = 0x01;
    public static final int NONE = 0x02;

    @Retention(SOURCE)
    @IntDef({TOAST, DIALOG, NONE})
    public @interface Form {
    }

    private int mForm = TOAST;
    private String mTitle;
    private String mMessage;

    public IncapableCause(String message) {
        mMessage = message;
    }

    public IncapableCause(String title, String message) {
        mTitle = title;
        mMessage = message;
    }

    public IncapableCause(@Form int form, String message) {
        mForm = form;
        mMessage = message;
    }

    public IncapableCause(@Form int form, String title, String message) {
        mForm = form;
        mTitle = title;
        mMessage = message;
    }

    public static void handleCause(Context context, IncapableCause cause) {
        if (cause == null)
            return;

        switch (cause.mForm) {
            case NONE:
                // do nothing.
                break;
            case DIALOG:
//                IncapableDialog incapableDialog = IncapableDialog.newInstance(cause.mTitle, cause.mMessage);
//                incapableDialog.show(((FragmentActivity) context).getSupportFragmentManager(),
//                        IncapableDialog.class.getName());

                LayoutInflater inflater = ((FragmentActivity) context).getLayoutInflater();
                View layout = inflater.inflate(R.layout.layout_custom_toast, (ViewGroup) ((FragmentActivity) context).findViewById(R.id.custom_toast_layout));
                TextView tv = (TextView) layout.findViewById(R.id.txt_toast_message);
                tv.setText(cause.mMessage);
                Toast toast = new Toast(((FragmentActivity) context));
                toast.setGravity(Gravity.CENTER, 0, 100);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(layout);
                toast.show();
                break;
            case TOAST:
            default:
//                Toast.makeText(context, cause.mMessage, Toast.LENGTH_SHORT).show();
                LayoutInflater inflater1 = ((FragmentActivity) context).getLayoutInflater();
                View layout1 = inflater1.inflate(R.layout.layout_custom_toast, (ViewGroup) ((FragmentActivity) context).findViewById(R.id.custom_toast_layout));
                TextView tv1 = (TextView) layout1.findViewById(R.id.txt_toast_message);
                tv1.setText(cause.mMessage);
                Toast toast1 = new Toast(((FragmentActivity) context));
                toast1.setGravity(Gravity.CENTER, 0, 100);
                toast1.setDuration(Toast.LENGTH_SHORT);
                toast1.setView(layout1);
                toast1.show();
                break;
        }
    }
}
