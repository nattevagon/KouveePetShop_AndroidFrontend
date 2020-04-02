package com.tubes.kouveepetshop.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tubes.kouveepetshop.API.ApiClient;
import com.tubes.kouveepetshop.API.ApiInterface;
import com.tubes.kouveepetshop.Model.ProductDAO;
import com.tubes.kouveepetshop.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailProductActivity extends AppCompatActivity {
    private TextView twName, twStock, twMinimal, twUnit, twPrice;
    private Button btnEdit;
    private ImageView btnBack, btnDelete, imgProduct;
    private String sId, sName, sPrice, sStock, sMinimal, sUnit, sImage, url;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        progressDialog = new ProgressDialog(this);
        progressDialog.show();

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        twName = findViewById(R.id.twName);
        twStock = findViewById(R.id.twStock);
        twMinimal = findViewById(R.id.twMinimal);
        twUnit = findViewById(R.id.twUnit);
        twPrice = findViewById(R.id.twPrice);
        btnDelete = findViewById(R.id.btnDelete);
        btnEdit = findViewById(R.id.btnEdit);
        imgProduct = findViewById(R.id.imgProduct);

        Intent i = getIntent();
        sId = i.getStringExtra("id");

        getData();

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DetailProductActivity.this, EditProductActivity.class);
                i.putExtra("id",sId);
                i.putExtra("name",sName);
                i.putExtra("price",sPrice);
                i.putExtra("stock",sStock);
                i.putExtra("minimal",sMinimal);
                i.putExtra("unit",sUnit);
                i.putExtra("image",sImage);
                startActivity(i);
            }
        });

        Toast.makeText(DetailProductActivity.this, "Coba hapus"+sId, Toast.LENGTH_SHORT).show();

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteItemFromList(view);
            }
        });
    }

    @Override
    public void onRestart() {
        super.onRestart();
        getData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void getData()
    {
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<List<ProductDAO>> get = apiService.getByProduct(sId);

        get.enqueue(new Callback<List<ProductDAO>>() {
            @Override
            public void onResponse(Call<List<ProductDAO>> call, Response<List<ProductDAO>> response) {

                for(int i=0;i<response.body().size();i++)
                {
                    sId = response.body().get(i).getID_PRODUK();
                    sName = response.body().get(i).getNAMA();
                    sStock = response.body().get(i).getSTOK();
                    sMinimal = response.body().get(i).getMINIMAL();
                    sUnit = response.body().get(i).getSATUAN();
                    sPrice = response.body().get(i).getHARGA();
                    sImage = response.body().get(i).getGAMBAR();
                }

                twName.setText(sName);
                twStock.setText(sStock);
                twMinimal.setText(sMinimal);
                twUnit.setText(sUnit);
                twPrice.setText(sPrice);
                url = "https://kouvee.modifierisme.com/upload/"+sImage;
                Picasso.with(DetailProductActivity.this).load(url).resize(300,300).centerCrop().into(imgProduct);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<ProductDAO>> call, Throwable t) {
                Toast.makeText(DetailProductActivity.this, "Koneksi hilang", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteItemFromList(View v) {

        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

        builder.setMessage("Hapus data ?")
                .setCancelable(false)
                .setPositiveButton("HAPUS",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                deleteData();

                            }
                        })
                .setNegativeButton("BATAL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        builder.show();

    }

    private void deleteData()
    {
        progressDialog.show();
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<ProductDAO> delete = apiService.deleteProduct(sId);

        delete.enqueue(new Callback<ProductDAO>() {
            @Override
            public void onResponse(Call<ProductDAO> call, Response<ProductDAO> response) {
                Toast.makeText(DetailProductActivity.this, "Berhasil dihapus", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                onBackPressed();
            }

            @Override
            public void onFailure(Call<ProductDAO> call, Throwable t) {
                Toast.makeText(DetailProductActivity.this, "Koneksi hilang", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
