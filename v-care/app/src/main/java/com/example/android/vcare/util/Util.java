package com.example.android.vcare.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;

import com.example.android.vcare.R;

import static android.view.View.GONE;

public class Util {

    public static int pxToDp(Context context, int px) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static void setViewShown(View view, boolean shown) {
        Context context = view.getContext();
        if (shown) {
            if (view.getVisibility() == GONE) {
                view.startAnimation(AnimationUtils.loadAnimation(
                        context, android.R.anim.fade_in));
                view.setVisibility(View.VISIBLE);
            }
        } else {
            if (view.getVisibility() == View.VISIBLE) {
                view.startAnimation(AnimationUtils.loadAnimation(
                        context, android.R.anim.fade_out));
                view.setVisibility(GONE);
            }
        }
    }

    public static void setListShown(View mListContainer, View mProgressContainer, boolean shown, boolean animate) {

        if (mListContainer == null || (mListContainer.getVisibility() == View.VISIBLE) == shown) {
            return;
        }
        Context context = mListContainer.getContext();
        if (shown) {
            if (animate) {
                mProgressContainer.startAnimation(AnimationUtils.loadAnimation(
                        context, android.R.anim.fade_out));
                mListContainer.startAnimation(AnimationUtils.loadAnimation(
                        context, android.R.anim.fade_in));
            } else {
                mProgressContainer.clearAnimation();
                mListContainer.clearAnimation();
            }
            mProgressContainer.setVisibility(GONE);
            mListContainer.setVisibility(View.VISIBLE);
        } else {
            if (animate) {
                mProgressContainer.startAnimation(AnimationUtils.loadAnimation(
                        context, android.R.anim.fade_in));
                mListContainer.startAnimation(AnimationUtils.loadAnimation(
                        context, android.R.anim.fade_out));
            } else {
                mProgressContainer.clearAnimation();
                mListContainer.clearAnimation();
            }
            mProgressContainer.setVisibility(View.VISIBLE);
            mListContainer.setVisibility(GONE);
        }
    }

    public static void showOkOnlyDisableCancelAlertDialog(Context context, String title, String message, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, onClickListener)
                .setCancelable(false);

        AlertDialog dialog = builder.create();
        if (TextUtils.isEmpty(title)) {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        dialog.show();
    }

    public static void showOkOnlyDisableCancelAlertDialog(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setCancelable(false);

        AlertDialog dialog = builder.create();
        if (TextUtils.isEmpty(title)) {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        dialog.show();
    }

    public static void showYesNoDisableCancelAlertDialog(Context context, String title, String message,
                                                         DialogInterface.OnClickListener positiveClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.yes, positiveClickListener)
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setCancelable(false);

        AlertDialog dialog = builder.create();
        if (TextUtils.isEmpty(title)) {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        dialog.show();
    }

    public static void showYesNoDisableCancelAlertDialog(Context context, String title, String message,
                                                         DialogInterface.OnClickListener positiveClickListener,
                                                         DialogInterface.OnClickListener negativeClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.yes, positiveClickListener)
                .setNegativeButton(R.string.no, negativeClickListener)
                .setCancelable(false);

        AlertDialog dialog = builder.create();
        if (TextUtils.isEmpty(title)) {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        dialog.show();
    }

}
