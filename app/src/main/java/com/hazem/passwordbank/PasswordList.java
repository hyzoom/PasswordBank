package com.hazem.passwordbank;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

/**
 * Created by hazem on 12/17/15.
 */
public class PasswordList extends AppCompatActivity {
    private Dialog dialog;
    private EditText inputName;
    private EditText inputURL;
    private EditText inputPassword;

    private TextInputLayout inputLayoutName;
    private TextInputLayout inputLayoutURL;
    private TextInputLayout inputLayoutPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_list);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showListDialog();
            }
        });
    }

    public void showListDialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_password_dialog);
        dialog.setCancelable(true);
        Window window = dialog.getWindow();
        Display display = this.getWindowManager().getDefaultDisplay();
        window.setLayout(display.getWidth() * 5 / 6,
                display.getHeight() * 2 / 3);

        inputName = (EditText) dialog.findViewById(R.id.input_name1);
        inputURL = (EditText) dialog.findViewById(R.id.input_url1);
        inputPassword = (EditText) dialog.findViewById(R.id.input_password1);

        inputLayoutName = (TextInputLayout) dialog.findViewById(R.id.input_layout_name1);
        inputLayoutURL = (TextInputLayout) dialog.findViewById(R.id.input_layout_URL1);
        inputLayoutPassword = (TextInputLayout) dialog.findViewById(R.id.input_layout_password1);

        inputName.addTextChangedListener(new MyTextWatcher(inputName));
        inputPassword.addTextChangedListener(new MyTextWatcher(inputPassword));

        dialog.show();
    }

    private boolean validateName() {
        if (inputName.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(getString(R.string.ErrorString));
            inputName.requestFocus();
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (inputPassword.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError(getString(R.string.Error1String));
            inputPassword.requestFocus();
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }

    class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_name1:
                    validateName();
                    break;
                case R.id.input_password1:
                    validatePassword();
                    break;
            }
        }
    }
}
