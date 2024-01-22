package com.example.loginproject;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


public class GalleryFragment extends Fragment {

    Spinner DropDown_Liste;
    LinearLayout linearLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_gallery, container, false);
        DropDown_Liste = view.findViewById(R.id.dropdown_liste);
        linearLayout = view.findViewById(R.id.linear_gallery_layout);

        GetLabels();
        ShowImages();

        return view;
    }


    private void GetLabels() {
        Intent i = getActivity().getIntent();
        String EmailH = i.getStringExtra("Email");

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("kullanicilar").child(EmailH).child("Labels");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> Label_listesi = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String LabelName = dataSnapshot.child("name").getValue(String.class);
                    Label_listesi.add(LabelName);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item,Label_listesi);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                DropDown_Liste.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void ShowImages() {
        Intent i = getActivity().getIntent();
        String Name = i.getStringExtra("Name");
        String SurName = i.getStringExtra("SurName");
        String EmailH = i.getStringExtra("Email");

        DropDown_Liste.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                linearLayout.removeAllViews();
                String Secilen_label = DropDown_Liste.getSelectedItem().toString();

                StorageReference storageReference = FirebaseStorage.getInstance().getReference(EmailH).child(Secilen_label);
                storageReference.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {
                        if(listResult.getItems().isEmpty()){
                            TextView dosyaBos = new TextView(getActivity());
                            dosyaBos.setText("Seçilen Dosyanın içinde Resim Bulunmadı ");
                            linearLayout.addView(dosyaBos);
                        }
                        for (StorageReference Resim : listResult.getItems()){
                            String ImageName = Resim.getName();
                            StorageReference reference = storageReference.child(ImageName);
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    LinearLayout NameAndSurnameAndLabel = new LinearLayout(getActivity());
                                    NameAndSurnameAndLabel.setOrientation(LinearLayout.VERTICAL);
                                    NameAndSurnameAndLabel.setPadding(10,10,0,0);

                                    LinearLayout NameAndButtons = new LinearLayout(getActivity());
                                    NameAndButtons.setOrientation(LinearLayout.VERTICAL);
                                    NameAndButtons.setPadding(50,0,0,0);
                                    ViewGroup.MarginLayoutParams margin = new ViewGroup.MarginLayoutParams(100,100);
                                    margin.setMargins(90,10,0,0);

                                    LinearLayout Buttons = new LinearLayout(getActivity());
                                    Buttons.setOrientation(LinearLayout.HORIZONTAL);
                                    Buttons.setPadding(50,10,0,0);


                                    LinearLayout NameAndButtonsAndImage = new LinearLayout(getActivity());
                                    NameAndButtonsAndImage.setOrientation(LinearLayout.HORIZONTAL);
                                    NameAndButtonsAndImage.setPadding(10,10,10,10);


                                    ImageView Show_Image = new ImageView(getActivity());
                                    Show_Image.setLayoutParams(new LinearLayout.LayoutParams(
                                            420,
                                            550
                                    ));
                                    Show_Image.setPadding(10,10,10,10);

                                    Glide.with(getActivity()).load(uri).into(Show_Image);

                                    TextView User_Name = new TextView(getActivity());
                                    User_Name.setText("User's Name : " + Name);
                                    User_Name.setLayoutParams(new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,20
                                    ));
                                    User_Name.setTypeface(null,Typeface.BOLD);

                                    TextView User_SurName = new TextView(getActivity());
                                    User_SurName.setText("User's SurName : " +SurName);
                                    User_SurName.setLayoutParams(new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,20
                                    ));
                                    User_SurName.setTypeface(null,Typeface.BOLD);


                                    TextView Label_Name = new TextView(getActivity());
                                    Label_Name.setText("Label's Name : " +Secilen_label);
                                    Label_Name.setLayoutParams(new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT,20
                                    ));
                                    Label_Name.setTypeface(null,Typeface.BOLD);


                                    Button LikeButton = new Button(getActivity());
                                    LikeButton.setBackgroundResource(R.drawable.like);
                                    LikeButton.setText("");
                                    LikeButton.setPadding(0, 0, 0, 0);
                                    LikeButton.setLayoutParams(new LinearLayout.LayoutParams(
                                            130,
                                            130
                                    ));
                                    ViewGroup.MarginLayoutParams likebuttonparams = new ViewGroup.MarginLayoutParams(100,100);
                                    likebuttonparams.setMargins(40,200,120,20);
                                    LikeButton.setLayoutParams(likebuttonparams);
                                    LikeButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            LikeButton.setBackgroundResource(R.drawable.liked);
                                        }
                                    });


                                    Button DislikeButton = new Button(getActivity());
                                    DislikeButton.setBackgroundResource(R.drawable.dislike);
                                    DislikeButton.setPadding(0, 0, 0, 0);
                                    DislikeButton.setText("");
                                    DislikeButton.setLayoutParams(new LinearLayout.LayoutParams(
                                            130,
                                            130
                                    ));
                                    ViewGroup.MarginLayoutParams dislikebuttonparams = new ViewGroup.MarginLayoutParams(100,100);
                                    dislikebuttonparams.setMargins(0,200,0,20);
                                    DislikeButton.setLayoutParams(dislikebuttonparams);
                                    DislikeButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            DislikeButton.setBackgroundResource(R.drawable.dislikeed);
                                        }
                                    });

                                    View ayiran = new View(getActivity());
                                    ayiran.setLayoutParams(new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,3
                                    ));
                                    ayiran.setBackgroundResource(R.color.black);

                                    NameAndSurnameAndLabel.addView(User_Name);
                                    NameAndSurnameAndLabel.addView(User_SurName);
                                    NameAndSurnameAndLabel.addView(Label_Name);
                                    Buttons.addView(LikeButton);
                                    Buttons.addView(DislikeButton);
                                    NameAndButtons.addView(NameAndSurnameAndLabel);
                                    NameAndButtons.addView(Buttons);
                                    NameAndButtonsAndImage.addView(Show_Image);
                                    NameAndButtonsAndImage.addView(NameAndButtons);

                                    linearLayout.addView(NameAndButtonsAndImage);
                                    linearLayout.addView(ayiran);



                                }
                            });
                            reference.getDownloadUrl().addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(),"Resimlerin Url Verini Getirirken Hata Olustu ",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    }
                });
                storageReference.listAll().addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(),"Dosyalari Getirirken Hata Olustu ",Toast.LENGTH_SHORT).show();
                    }
                });

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

}