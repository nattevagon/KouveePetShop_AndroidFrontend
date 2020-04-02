package com.tubes.kouveepetshop.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.tubes.kouveepetshop.API.ApiClient;
import com.tubes.kouveepetshop.API.ApiInterface;
import com.tubes.kouveepetshop.Model.SupplierDAO;
import com.tubes.kouveepetshop.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddSupplierActivity extends AppCompatActivity {
    private ImageButton btnBack;
    private EditText etName, etAddress, etPhoneNumber;
    private Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_supplier);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        etName = findViewById(R.id.etName);
        etAddress = findViewById(R.id.etAddress);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        btnAdd = findViewById(R.id.btnAdd);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etName.getText().toString().equalsIgnoreCase(""))
                {
                    Toast.makeText(AddSupplierActivity.this, "Kosong", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Add();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void Add() {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<SupplierDAO> add = apiService.addSupplier(etName.getText().toString(),etPhoneNumber.getText().toString(), etAddress.getText().toString());

        add.enqueue(new Callback<SupplierDAO>(){
            @Override
            public void onResponse(Call<SupplierDAO> call, Response<SupplierDAO> response) {
                Toast.makeText(AddSupplierActivity.this, "Success", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }

            @Override
            public void onFailure(Call<SupplierDAO> call, Throwable t) {
                Toast.makeText(AddSupplierActivity.this, "Fail", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
