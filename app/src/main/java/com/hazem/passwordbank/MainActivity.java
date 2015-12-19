package com.hazem.passwordbank;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.eftimoff.patternview.PatternView;

import fr.ganfra.materialspinner.MaterialSpinner;

public class MainActivity extends AppCompatActivity {
    PatternView patternView;
    String patternString;
    private EditText inputName;
    private TextInputLayout inputLayoutName;
    private EditText inputPassword;
    private TextInputLayout inputLayoutPassword;

    MaterialSpinner yearSpinner;
    MaterialSpinner monthSpinner;
    MaterialSpinner daySpinner;
    private ArrayAdapter<String> yearsAdapter;
    private ArrayAdapter<String> monthsAdapter;
    private ArrayAdapter<String> daysAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        designPatternView();
        designView();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Make Action", null).show();

            }
        });
    }

    public void designView() {
        inputName = (EditText) findViewById(R.id.input_name);
        inputName.addTextChangedListener(new MyTextWatcher(inputName));
        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);

        inputPassword = (EditText) findViewById(R.id.input_password);
        inputPassword.addTextChangedListener(new MyTextWatcher(inputPassword));
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);


        String[] years = {"Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6"};
        yearSpinner = (MaterialSpinner) findViewById(R.id.yearSpinner);
        yearsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years);
        yearsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearsAdapter);
        yearSpinner.setPaddingSafe(0, 0, 0, 0);

        String[] months = {"Item 1", "Item 2", "Item 3", "Item 4", "Item 5", "Item 6"};
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

    public void designPatternView() {
        patternView = (PatternView) findViewById(R.id.patternView);
        Toast.makeText(getApplicationContext(), "ENTER PATTERN", Toast.LENGTH_LONG).show();
        patternView.setOnPatternDetectedListener(new PatternView.OnPatternDetectedListener() {

            @Override
            public void onPatternDetected() {
                if (patternString == null) {
                    patternString = patternView.getPatternString();
//                    patternView.clearPattern();
                    return;
                }
                if (patternString.equals(patternView.getPatternString())) {
                    Toast.makeText(getApplicationContext(), patternView.getPatternString() + "\n" + patternString + "\n  PATTERN CORRECT", Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(getApplicationContext(), "PATTERN NOT CORRECT", Toast.LENGTH_SHORT).show();
//                patternView.clearPattern();
            }
        });
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
                case R.id.input_name:
                    validateName();
                    break;
                case R.id.input_password:
                    validatePassword();
                    break;
            }
        }
    }
}
