package com.example.loginproject;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import io.grpc.Context;


public class AddPhotoFragment extends Fragment {

    Button TakePicture;
    ImageView GelenResim;
    LinearLayout linearLayout;
    List<String> Secilenler;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_add_photo, container, false);
        GelenResim = view.findViewById(R.id.image);
        TakePicture = view.findViewById(R.id.picturebtn);
        linearLayout = view.findViewById(R.id.linear_photo_layout);
        Secilenler = new ArrayList<>();
        ShowLabels();

        TakePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Secilenler.isEmpty()){
                    Toast.makeText(getActivity(),"Lütfen En Az Bir Etiketi Seçin",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.CAMERA) !=  PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(getActivity(),"Camera Kullamnak için Yetlkiniz Yoktur",Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(requireActivity(),new String[]{Manifest.permission.CAMERA},100);
                } else if (ContextCompat.checkSelfPermission(requireContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE) !=  PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(),"Resimi Telfonda Kayıd Etmek için Yetlkiniz Yoktur",Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(requireActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},101);
                }else{
                    Intent ResimCek = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if (ResimCek.resolveActivity(requireActivity().getPackageManager()) != null){
                        startActivityForResult(ResimCek , 103);
                    }
                }
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
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String LabelsName = dataSnapshot.child("name").getValue(String.class);

                    LinearLayout yataylayout = new LinearLayout(getActivity());
                    yataylayout.setOrientation(LinearLayout.HORIZONTAL);
                    yataylayout.setPadding(15,15,15,15);

                    TextView labelname = new TextView(getActivity());
                    labelname.setText(LabelsName);
                    labelname.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,100,2f
                    ));

                    CheckBox checkBox = new CheckBox(getActivity());
                    checkBox.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
                    ));
                    checkBox.setTag(LabelsName);

                    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked){
                                Secilenler.add((String) buttonView.getTag());
                            }
                            else {
                                Secilenler.remove((String) buttonView.getTag());
                            }
                        }
                    });
                    yataylayout.addView(labelname);
                    yataylayout.addView(checkBox);

                    View ayiran = new View(getActivity());
                    ayiran.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,3
                    ));
                    ayiran.setBackgroundResource(R.color.black);

                    linearLayout.addView(ayiran);
                    linearLayout.addView(yataylayout);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 103 && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBithali = (Bitmap) extras.get("data");

            GelenResim.setImageBitmap(imageBithali);
            AddToStorage(imageBithali);

        }
    }

    public void AddToStorage(Bitmap Resim){
        Intent i = getActivity().getIntent();
        String EmailH = i.getStringExtra("Email");

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Resim.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] Resimverileri = byteArrayOutputStream.toByteArray();

        for (String Secilen : Secilenler){
            String ResimAdi = Secilen + "_" + System.currentTimeMillis();
            StorageReference storageReference = FirebaseStorage.getInstance().getReference(EmailH).child(Secilen);
            UploadTask yukle = storageReference.child(ResimAdi).putBytes(Resimverileri);
            yukle.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    Toast.makeText(getActivity(),"Resim Hafızaya Başarlı Bir Şekilde Eklenmiştir",Toast.LENGTH_SHORT).show();
                }
            });
            yukle.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(),"Resim Hafızaya Yüklenmemiş ",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}