package com.example.snapapp;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MyProfil extends AppCompatActivity {


    //Viser tekst
    private TextView nameTextView;
    //redigere i tekst
    private EditText nameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profil);

        //vi skal lave en forbindelse til textview her.
        //R kompiler hele tiden, så f.eks hvergang der bliver lavet en knap rekompiler den klassen.
        //Hver gang vi laver noget nyt, bliver et nyt nr(id) klar
        nameTextView = findViewById(R.id.nameText);
        nameEditText = findViewById(R.id.nameText);

    }

    //Denne knap skal føre dig til homepage.
    public void HomePagePressed(View view) {
        //nu siger vi at vi er færdige med denne side og henviser os tilbage til start
        finish();
    }

    public void SaveProfilPressed(View view){
        //gemmer den text som vi har skrevet ind og erstatter den gamle.
        nameTextView.setText(nameEditText.getText());
    }


}