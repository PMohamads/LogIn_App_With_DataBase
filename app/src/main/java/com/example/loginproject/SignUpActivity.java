package com.example.loginproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    EditText Ad,SoyAd,Email,Sifre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Ad = findViewById(R.id.adi);
        SoyAd = findViewById(R.id.soyadi);
        Email = findViewById(R.id.email2);
        Sifre = findViewById(R.id.sifre1);
    }

    public void HesabEkle(View view) {
        String GirilenAd = Ad.getText().toString().trim();
        String GirilenSoyAd = SoyAd.getText().toString().trim();
        String GirilenEmail = Email.getText().toString().trim();
        String GirilenSifre = Sifre.getText().toString().trim();

        if(TextUtils.isEmpty(GirilenAd) || TextUtils.isEmpty(GirilenSoyAd) || TextUtils.isEmpty(GirilenEmail) || TextUtils.isEmpty(GirilenSifre)){
            Toast.makeText(this,"Lütfen Boşlukları Doldurunuz...",Toast.LENGTH_LONG).show();
            return;
        }
        if(GirilenSifre.length() >= 8){
            if (GirilenSifre.matches(".*[a-zA-Z].*") && GirilenSifre.matches(".*\\d.*")) {
                    DatabaseReference veritabani = FirebaseDatabase.getInstance().getReference();
                    String modifyedemail = GirilenEmail.replace(".",",");
                    veritabani.child("kullanicilar").child(modifyedemail).child("name").setValue(GirilenAd);
                    veritabani.child("kullanicilar").child(modifyedemail).child("surname").setValue(GirilenSoyAd);
                    veritabani.child("kullanicilar").child(modifyedemail).child("password").setValue(GirilenSifre);

                    Toast.makeText(SignUpActivity.this,"Hesabınız Başarlı Bir Şekilde Oluşturlmuştur ",Toast.LENGTH_LONG).show();
                }
            else {
                Toast.makeText(this,"Şifrenizin içindeki karakterler En Az 1 Harf Ve 1 Rakam Bulunmalıdır.. ",Toast.LENGTH_SHORT).show();
                 }
            }
        else{
            Toast.makeText(this,"Şifreniz En Az 8 Karakterlerden Oluşmalıdır.. ",Toast.LENGTH_SHORT).show();
        }

    }

    public void LoggingPage(View view) {
        startActivity(new Intent(SignUpActivity.this,LogInActivity.class));
    }
}