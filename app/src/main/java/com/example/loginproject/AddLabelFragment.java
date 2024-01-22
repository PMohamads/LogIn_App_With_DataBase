package com.example.loginproject;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddLabelFragment extends Fragment {

    TextView LabelNum;
    EditText LabelName,LabelDesc;
    Button Add;
    LinearLayout linearLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_add_label, container, false);
        LabelName = view.findViewById(R.id.labelname);
        LabelDesc = view.findViewById(R.id.labeldesc);
        LabelNum = view.findViewById(R.id.labelnum);
        Add = view.findViewById(R.id.addtodatabase);
        linearLayout = view.findViewById(R.id.linear_layout);
        ShowLabels();

        Intent i = getActivity().getIntent();
        String emailhesabi = i.getStringExtra("Email");

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("kullanicilar").child(emailhesabi);
                databaseReference.child("Labels").child(LabelName.getText().toString().trim()).child("name").setValue(LabelName.getText().toString().trim());
                databaseReference.child("Labels").child(LabelName.getText().toString().trim()).child("desc").setValue(LabelDesc.getText().toString().trim());
                Toast.makeText(getActivity(),"Etiketi Başarlı Bir Şekilde VeriTabanına Eklenmiştir ...",Toast.LENGTH_LONG).show();
                ShowLabels();
                LabelName.setText("");
                LabelDesc.setText("");
            }
        });

        return view;
    }

    private void ShowLabels() {
        linearLayout.removeAllViews();
        Intent i = getActivity().getIntent();
        String emailhesabi = i.getStringExtra("Email");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("kullanicilar").child(emailhesabi).child("Labels");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Long Count = snapshot.getChildrenCount();
                LabelNum.setText(Count.toString());
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String LabelsName = dataSnapshot.child("name").getValue(String.class);
                    String LabelsDesc = dataSnapshot.child("desc").getValue(String.class);

                    LinearLayout yataylayout = new LinearLayout(getActivity());
                    yataylayout.setOrientation(LinearLayout.HORIZONTAL);
                    yataylayout.setPadding(15,15,15,15);

                    TextView labelnameanddesc = new TextView(getActivity());
                    labelnameanddesc.setText(LabelsName + " : \n" + LabelsDesc);
                    labelnameanddesc.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,220,2f
                    ));

                    Button DeletButton = new Button(getActivity());
                    DeletButton.setBackgroundResource(R.drawable.delete);
                    DeletButton.setText("");
                    DeletButton.setTag(dataSnapshot.getKey());
                    DeletButton.setPadding(0, 0, 0, 0);
                    DeletButton.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));
                    ViewGroup.MarginLayoutParams deletebuttonparams = new ViewGroup.MarginLayoutParams(100,100);
                    deletebuttonparams.setMargins(8,50,10,0);
                    DeletButton.setLayoutParams(deletebuttonparams);
                    DeletButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String labelid = (String) v.getTag();
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("kullanicilar").child(emailhesabi).child("Labels").child(labelid);
                            databaseReference.removeValue();
                            Toast.makeText(getActivity(),"Etiketi Başarlı Bir Şekilde Silinmiştir",Toast.LENGTH_LONG).show();
                            ShowLabels();
                        }
                    });

                    Button ModifyButton = new Button(getActivity());
                    ModifyButton.setBackgroundResource(R.drawable.edit);
                    ModifyButton.setTag(dataSnapshot.getKey());
                    ModifyButton.setPadding(0, 0, 0, 0);
                    ModifyButton.setText("");
                    ModifyButton.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    ));
                    ViewGroup.MarginLayoutParams modifybuttonparams = new ViewGroup.MarginLayoutParams(100,100);
                    modifybuttonparams.setMargins(10,50,8,0);
                    ModifyButton.setLayoutParams(modifybuttonparams);
                    ModifyButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String labelid = (String) v.getTag();
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("kullanicilar").child(emailhesabi).child("Labels").child(labelid);
                            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String labeladi = snapshot.child("name").getValue(String.class);
                                    String labelaciklamasi = snapshot.child("desc").getValue(String.class);
                                    LabelName.setText(labeladi);
                                    LabelDesc.setText(labelaciklamasi);
                                    ShowLabels();
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    });
                    yataylayout.addView(labelnameanddesc);
                    yataylayout.addView(DeletButton);
                    yataylayout.addView(ModifyButton);

                    linearLayout.addView(yataylayout);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}