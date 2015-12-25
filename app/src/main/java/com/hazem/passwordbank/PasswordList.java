package com.hazem.passwordbank;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eftimoff.patternview.PatternView;
import com.hazem.passwordbank.common.CommonService;
import com.hazem.passwordbank.listner.HidingScrollListener;
import com.hazem.passwordbank.model.Item;
import com.hazem.passwordbank.model.User;
import com.hazem.passwordbank.viewholder.RecyclerHeaderViewHolder;

import java.util.List;

/**
 * Created by hazem on 12/17/15.
 */
public class PasswordList extends AppCompatActivity implements View.OnClickListener{

    private CommonService commonService = new CommonService(this);
    private User currentUser;

    private Dialog dialog;
    private EditText inputName;
    private EditText inputURL;
    private EditText inputPassword;

    private TextInputLayout inputLayoutName;
    private TextInputLayout inputLayoutURL;
    private TextInputLayout inputLayoutPassword;

    private Toolbar mToolbar;
    private FloatingActionButton mFabButton;

    private RecyclerView recyclerView;
    private PatternView dialogPattern;
    private TextView dialogTitleTextView;
    private Item selectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeRed);
        currentUser = commonService.getUser();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_list);

        // Force RTL
        ViewCompat.setLayoutDirection(getWindow().getDecorView(), ViewCompat.LAYOUT_DIRECTION_RTL);

        mFabButton = (FloatingActionButton) findViewById(R.id.fab1);
        mFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showListDialog();
            }
        });

        initToolbar();
        initRecyclerView();
    }

    ///////////////////////////////////////////////////////////////////////////////////

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        setTitle(getString(R.string.app_name));
        mToolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(commonService.getItems());
        recyclerView.setAdapter(recyclerAdapter);

        recyclerView.addOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                hideViews();
            }

            @Override
            public void onShow() {
                showViews();
            }
        });
    }

    private void hideViews() {
        mToolbar.animate().translationY(-mToolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));

        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mFabButton.getLayoutParams();
        int fabBottomMargin = lp.bottomMargin;
        mFabButton.animate().translationY(mFabButton.getHeight()+fabBottomMargin).setInterpolator(new AccelerateInterpolator(2)).start();
    }

    private void showViews() {
        mToolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
        mFabButton.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
    }

    ///////////////////////////////////////////////////////////////////////////////////

    public void showListDialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_password_dialog);
        dialog.setCancelable(true);

        Window window = dialog.getWindow();
        Display display = this.getWindowManager().getDefaultDisplay();
        window.setLayout(display.getWidth() * 5 / 6, display.getHeight() * 1 / 2);

        inputName = (EditText) dialog.findViewById(R.id.input_name1);
        inputURL = (EditText) dialog.findViewById(R.id.input_url1);
        inputPassword = (EditText) dialog.findViewById(R.id.input_password1);

        inputLayoutName = (TextInputLayout) dialog.findViewById(R.id.input_layout_name1);
        inputLayoutURL = (TextInputLayout) dialog.findViewById(R.id.input_layout_URL1);
        inputLayoutPassword = (TextInputLayout) dialog.findViewById(R.id.input_layout_password1);

        Button addItemButton = (Button) dialog.findViewById(R.id.addItemButton);
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!inputName.getText().toString().equals("") && !inputURL.getText().toString().equals("")
                    && !inputPassword.getText().toString().equals("")){

                    Item newItem = new Item();
                    newItem.setAuthor(currentUser);

                    newItem.setName(inputName.getText().toString());
                    newItem.setEmailLogin(inputURL.getText().toString());
                    newItem.setPassword(inputPassword.getText().toString());

                    commonService.saveItem(newItem);

                    RecyclerAdapter recyclerAdapter = new RecyclerAdapter(commonService.getItems());
                    recyclerView.setAdapter(recyclerAdapter);

                    dialog.dismiss();
                } else {
                    Toast.makeText(PasswordList.this, getResources().getString(R.string.fill_all_fields), Toast.LENGTH_LONG).show();
                }

            }
        });
        dialog.show();
    }

    private boolean validateName() {
        if (inputName.getText().toString().trim().isEmpty()) {
            inputLayoutName.setError(getString(R.string.nameErrorString));
            inputName.requestFocus();
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (inputPassword.getText().toString().trim().isEmpty()) {
            inputLayoutPassword.setError(getString(R.string.passwordErrorString));
            inputPassword.requestFocus();
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }

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

        dialogPattern.setOnPatternDetectedListener(new PatternView.OnPatternDetectedListener() {
            @Override
            public void onPatternDetected() {
                if (currentUser.getPattern().equals(dialogPattern.getPatternString())) {
                    dialog.dismiss();
                    Toast.makeText(PasswordList.this, selectedItem.getPassword(), Toast.LENGTH_LONG).show();
                    Toast.makeText(PasswordList.this, selectedItem.getPassword(), Toast.LENGTH_LONG).show();
                    Toast.makeText(PasswordList.this, selectedItem.getPassword(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.itemPasswordTextView:
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

    private class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int TYPE_HEADER = 2;
        private static final int TYPE_ITEM = 1;
        private List<Item> mItemList;

        public RecyclerAdapter(List<Item> itemList) {
            mItemList = itemList;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            if (viewType == TYPE_ITEM) {
                final View view = LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false);
                return new RecyclerItemViewHolder(view);
            } else if (viewType == TYPE_HEADER) {
                final View view = LayoutInflater.from(context).inflate(R.layout.recycler_header, parent, false);
                return new RecyclerHeaderViewHolder(view);
            }
            throw new RuntimeException("There is no type that matches the type " + viewType + " + make sure your using types correctly");
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
            if (!isPositionHeader(position)) {
                RecyclerItemViewHolder holder = (RecyclerItemViewHolder) viewHolder;
                Item itemText = mItemList.get(position - 1); // header
                holder.setItemText(itemText);
            }
        }

        public int getBasicItemCount() {
            return mItemList == null ? 0 : mItemList.size();
        }


        @Override
        public int getItemViewType(int position) {
            if (isPositionHeader(position)) {
                return TYPE_HEADER;
            }

            return TYPE_ITEM;
        }

        @Override
        public int getItemCount() {
            return getBasicItemCount() + 1; // header
        }

        private boolean isPositionHeader(int position) {
            return position == 0;
        }


        class RecyclerItemViewHolder extends RecyclerView.ViewHolder {

            private TextView itemNameTextView, itemMailTextView, itemPasswordTextView;

            public RecyclerItemViewHolder(final View parent) {
                super(parent);
                itemNameTextView = (TextView) parent.findViewById(R.id.itemNameTextView);
                itemMailTextView = (TextView) parent.findViewById(R.id.itemMailTextView);
                itemPasswordTextView = (TextView) parent.findViewById(R.id.itemPasswordTextView);
            }

            public void setItemText(final Item item) {
                itemNameTextView.setText(item.getName());
                itemMailTextView.setText(item.getEmailLogin());

                itemPasswordTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedItem = item;
                        showPatternDialog();
                    }
                });
            }
        }

    }

}
