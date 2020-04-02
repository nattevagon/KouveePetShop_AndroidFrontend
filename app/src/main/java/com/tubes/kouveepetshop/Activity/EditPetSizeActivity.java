package com.tubes.kouveepetshop.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.tubes.kouveepetshop.API.ApiClient;
import com.tubes.kouveepetshop.API.ApiInterface;
import com.tubes.kouveepetshop.Model.PetSizeDAO;
import com.tubes.kouveepetshop.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditPetSizeActivity extends AppCompatActivity {
    private Button btnSave;
    private ImageView btnBack;
    private EditText etNama;
    private String sId, sName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pet_size);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        etNama = findViewById(R.id.etNama);
        btnBack = findViewById(R.id.btnBack);
        btnSave = findViewById(R.id.btnSave);

        Intent i = getIntent();
        sId = i.getStringExtra("id");
        sName = i.getStringExtra("name");

        etNama.setText(sName);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etNama.getText().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(EditPetSizeActivity.this, "Kosong", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Edit();
                }
            }
        });
    }
    private void Edit() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<PetSizeDAO> add = apiService.updatePetSize(sId, etNama.getText().toString());

        add.enqueue(new Callback<PetSizeDAO>(){
            @Override
            public void onResponse(Call<PetSizeDAO> call, Response<PetSizeDAO> response) {
                Toast.makeText(EditPetSizeActivity.this, "Success", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<PetSizeDAO> call, Throwable t) {
                Toast.makeText(EditPetSizeActivity.this, "Fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
