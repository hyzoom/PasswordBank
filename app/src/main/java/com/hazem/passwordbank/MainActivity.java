package com.hazem.passwordbank;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eftimoff.patternview.PatternView;
import com.hazem.passwordbank.common.CommonService;
import com.hazem.passwordbank.model.Item;
import com.hazem.passwordbank.model.User;

import fr.ganfra.materialspinner.MaterialSpinner;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    public static User currentUser;
    private CommonService commonService = new CommonService(this);
    private Button loginButton;

    private PatternView dialogPattern;

    private EditText inputName;
    private TextInputLayout inputLayoutName;
    private EditText inputPassword;
    private TextInputLayout inputLayoutPassword;

    private TextView dialogTitleTextView;

    MaterialSpinner yearSpinner;
    MaterialSpinner monthSpinner;
    MaterialSpinner daySpinner;
    private ArrayAdapter<String> yearsAdapter;
    private ArrayAdapter<String> monthsAdapter;
    private ArrayAdapter<String> daysAdapter;

    private Toolbar mToolbar;

    private Dialog dialog;
    private Button dialogResetPatternButton;
    private String dialogPatternString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeRed);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Force RTL
        ViewCompat.setLayoutDirection(getWindow().getDecorView(), ViewCompat.LAYOUT_DIRECTION_RTL);
        
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        setTitle(getString(R.string.app_name));
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));


        currentUser = commonService.getUser();

        designView();

        loginButton = (Button) findViewById(R.id.loginButton);
        if(currentUser == null) {
            loginButton.setText(getResources().getText(R.string.newUser));
        }

        loginButton.setOnClickListener(this);
    }

    public void designView() {
        inputName = (EditText) findViewById(R.id.input_name);
        inputName.addTextChangedListener(new MyTextWatcher(inputName));
        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);

        inputPassword = (EditText) findViewById(R.id.input_password);
        inputPassword.addTextChangedListener(new MyTextWatcher(inputPassword));
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);


        String[] years = {"Item 1", "Item 2", "Item 3", "Item 4", "Item 55", "Item 6"};
        yearSpinner = (MaterialSpinner) findViewById(R.id.yearSpinner);
        yearsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years);
        yearsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearsAdapter);
        yearSpinner.setPaddingSafe(0, 0, 0, 0);

        String[] months = {"Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 000"};
        monthSpinner = (MaterialSpinner) findViewById(R.id.monthSpinner);
        monthsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, months);
        monthsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthsAdapter);
        monthSpinner.setPaddingSafe(0, 0, 0, 0);

        String[] days = {"Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6"};
        daySpinner = (MaterialSpinner) findViewById(R.id.daySpinner);
        daysAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, days);
        daysAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        daySpinner.setAdapter(daysAdapter);
        daySpinner.setPaddingSafe(0, 0, 0, 0);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginButton:
                if (!validateName())
                    return;
                if (!validatePassword())
                    return;

                if (currentUser == null){
                    currentUser = new User();
                    currentUser.setName(inputName.getText().toString());
                    currentUser.setPassword(inputPassword.getText().toString());
                    currentUser.setPattern("");
                }

                showPatternDialog();
                break;

            case R.id.dialogResetPatternButton:
                dialogPatternString = "";
                dialogTitleTextView.setText(getResources().getString(R.string.dialog_pattern_title));
                break;
        }
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
            /*switch (view.getId()) {
                case R.id.input_name:
                    validateName();
                    break;
                case R.id.input_password:
                    validatePassword();
                    break;
            }*/
        }
    }

    private void clearPattern() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialogPattern.clearPattern();
            }
        }, 500);
    }

    private boolean validateName() {
        if (inputName.getText().toString().equals("") || inputName.getText().toString()== null) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.nameErrorString), Toast.LENGTH_LONG).show();
            return false;
        }

        if (currentUser != null && !inputName.getText().toString().equals(currentUser.getName())) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.user_name_error), Toast.LENGTH_LONG).show();
            return false;
        }

        /*if (inputName.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(getString(R.string.ErrorString));
            inputName.requestFocus();
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }*/
        return true;
    }

    private boolean validatePassword() {
        if (inputPassword.getText().toString().equals("") || inputPassword.getText().toString()== null) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.passwordErrorString), Toast.LENGTH_LONG).show();
            return false;
        }

        if (currentUser != null && !inputPassword.getText().toString().equals(currentUser.getPassword())) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.user_password_error), Toast.LENGTH_LONG).show();
            return false;
        }

        /*if (inputPassword.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError(getString(R.string.Error1String));
            inputPassword.requestFocus();
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }*/

        return true;
    }

    private void showPatternDialog() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_pattern, null);

        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(dialogView);
        dialog.show();

        dialogTitleTextView = (TextView) dialog.findViewById(R.id.dialogTitleTextView);

        dialogPattern = (PatternView) dialog.findViewById(R.id.dialogPattern);
        dialogResetPatternButton = (Button) dialog.findViewById(R.id.dialogResetPatternButton);

        dialogResetPatternButton.setOnClickListener(this);

        if (currentUser.getPattern().equals("")){
            dialogResetPatternButton.setVisibility(View.VISIBLE);

            dialogResetPatternButton.setEnabled(false);

            dialogTitleTextView.setText(getResources().getString(R.string.add_pattern));
        }

        dialogPattern.setOnPatternDetectedListener(new PatternView.OnPatternDetectedListener() {
            @Override
            public void onPatternDetected() {
                if (currentUser.getPattern().equals("")) {
                    if (dialogPatternString.equals("")) {
                        dialogPatternString = dialogPattern.getPatternString();
                        dialogResetPatternButton.setEnabled(true);
                        dialogTitleTextView.setText(getResources().getString(R.string.re_pattern));
                    } else {
                        if (dialogPatternString.equals(dialogPattern.getPatternString())) {
                            currentUser.setPattern(dialogPattern.getPatternString());
                            commonService.saveUser(currentUser);
                            dialog.dismiss();
                            Intent intent = new Intent(MainActivity.this, PasswordList.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                } else {
                    if (currentUser.getPattern().equals(dialogPattern.getPatternString())) {
                        dialog.dismiss();
                        Intent intent = new Intent(MainActivity.this, PasswordList.class);
                        startActivity(intent);
                        finish();
                    }
                }
                clearPattern();
            }
        });

    }

}
