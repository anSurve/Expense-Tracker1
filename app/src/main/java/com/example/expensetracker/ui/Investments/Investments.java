package com.example.expensetracker.ui.Investments;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.res.TypedArray;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.expensetracker.R;
import com.example.expensetracker.ui.earnExpense.EarnExpenseViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class Investments extends Fragment {

    private TabLayout tabLayout;
    private LinearLayout mainView;
    private FirebaseFirestore fDb;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root =  inflater.inflate(R.layout.investments_fragment, container, false);
        tabLayout = root.findViewById(R.id.tabLayout);
        mainView = root.findViewById(R.id.mainView);
        fDb = FirebaseFirestore.getInstance();

        fetchInvestments();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //txt.setText(tab.getText().toString() + " is selected");
                clearAllData();
                String selectedTab = tab.getText().toString();
                if(selectedTab.contains("Invest"))
                    fetchInvestments();
                else
                    fetchLoans();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return root;
    }
    private void fetchLoans(){
        fDb.collection("Loans")
                .whereEqualTo("user_id", FirebaseAuth.getInstance().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (DocumentSnapshot document : task.getResult()) {
                                String category = document.get("from").toString();
                                String Amount = document.get("amount").toString();
                                String Desc = document.get("description").toString();
                                String maturity = document.get("tenure").toString();
                                createTextView(category, Amount, Desc, maturity);
                                //Toast.makeText(getActivity(), "Amount - "+Amount, Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Failed to fetch data", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void fetchInvestments(){
        fDb.collection("Investments")
                .whereEqualTo("user_id", FirebaseAuth.getInstance().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            for (DocumentSnapshot document : task.getResult()) {
                                String category = document.get("category").toString();
                                String Amount = document.get("amount").toString();
                                String Desc = document.get("description").toString();
                                String maturity = document.get("maturity").toString();
                                createTextView(category, Amount, Desc, maturity);
                                //Toast.makeText(getActivity(), "Amount - "+Amount, Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Failed to fetch data", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void createTextView(String from, String amount, String desc, String maturity) {

        final TextView textView_item_name = new TextView(getActivity());
        final TextView amountTxt = new TextView(getActivity());
        final TextView descTxt = new TextView(getActivity());
        final TextView maturityTxt = new TextView(getActivity());
        final View nView = new View(getActivity());

        final LinearLayout.LayoutParams _viewparams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        final LinearLayout.LayoutParams _params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);

        final LinearLayout.LayoutParams _paramsline = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 2);

        int[] attrs = {android.R.attr.background};
        TypedArray ta = getActivity().obtainStyledAttributes(R.style.Divider, attrs);
        nView.setLayoutParams(_paramsline);
        nView.setBackground(ta.getDrawable(0));

        _params.weight = 1.0f;
        _params.gravity= Gravity.START;
        textView_item_name.setLayoutParams(_params);
        textView_item_name.setTextSize(20);
        textView_item_name.setGravity(Gravity.CENTER);
        textView_item_name.setPadding(10, 10, 10, 10);
        textView_item_name.setText(from);

        amountTxt.setLayoutParams(_params);
        amountTxt.setTextSize(20);
        amountTxt.setGravity(Gravity.CENTER);
        amountTxt.setText(amount);

        descTxt.setLayoutParams(_params);
        descTxt.setTextSize(16);
        descTxt.setGravity(Gravity.CENTER);
        descTxt.setText(desc);

        maturityTxt.setLayoutParams(_params);
        maturityTxt.setTextSize(16);
        maturityTxt.setGravity(Gravity.CENTER);
        maturityTxt.setText(maturity);

        LinearLayout ll = new LinearLayout(getActivity());
        ll.setLayoutParams(_viewparams);
        ll.setOrientation(LinearLayout.VERTICAL);

        LinearLayout ll1 = new LinearLayout(getActivity());
        ll1.setLayoutParams(_viewparams);
        ll1.setOrientation(LinearLayout.VERTICAL);

        ll.setPadding(30, 30, 30, 30);
        ll.addView(textView_item_name);
        ll.addView(amountTxt);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ll.indexOfChild(maturityTxt) != -1)
                    ll.removeView(maturityTxt);
                else
                    ll.addView(maturityTxt);

                if (ll.indexOfChild(descTxt) != -1)
                    ll.removeView(descTxt);
                else
                    ll.addView(descTxt);
            }
        });
        ll1.addView(ll);
        ll1.addView(nView);
        mainView.addView(ll1);
    }

    private void clearAllData(){
        if(mainView.getChildCount() > 0)
            mainView.removeAllViews();
    }
}
