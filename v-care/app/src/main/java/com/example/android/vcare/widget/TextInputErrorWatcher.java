package com.example.android.vcare.widget;

import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;

public class TextInputErrorWatcher implements TextWatcher {

    private final TextInputLayout inputLayout;

    public TextInputErrorWatcher(TextInputLayout inputLayout) {
        this.inputLayout = inputLayout;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence.length() > 0) {
            inputLayout.setError(null);
            inputLayout.setErrorEnabled(false);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
