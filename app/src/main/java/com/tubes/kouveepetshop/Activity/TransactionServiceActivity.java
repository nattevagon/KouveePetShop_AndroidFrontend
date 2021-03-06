package com.tubes.kouveepetshop.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tubes.kouveepetshop.API.ApiClient;
import com.tubes.kouveepetshop.API.ApiInterface;
import com.tubes.kouveepetshop.Fragment.AddTransactionServiceFragment;
import com.tubes.kouveepetshop.Model.TransactionServiceDAO;
import com.tubes.kouveepetshop.R;
import com.tubes.kouveepetshop.RecyclerAdapter.TransactionServiceRecyclerAdapter;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionServiceActivity extends AppCompatActivity {
    private ImageButton btnBack;
    private Button btnHistory;
    private SearchView searchView;
    private FloatingActionButton btnAdd;
    private ShimmerFrameLayout mShimmerViewContainer;

    private List<TransactionServiceDAO> transactionServiceList;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefresh;
    private TransactionServiceRecyclerAdapter recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_service);

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);
        mShimmerViewContainer.startShimmerAnimation();

        btnBack = findViewById(R.id.btnBack);
        btnHistory = findViewById(R.id.btnHistory);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = TransactionServiceActivity.this.getSupportFragmentManager();
                AddTransactionServiceFragment dialog = new AddTransactionServiceFragment();
                dialog.show(manager, "dialog");
            }
        });

        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TransactionServiceActivity.this, HistoryTransactionServiceActivity.class);
                startActivity(i);
            }
        });

        searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.tsRecyclerView);
        load();

        swipeRefresh = findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mShimmerViewContainer.startShimmerAnimation();
                load();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        mShimmerViewContainer.startShimmerAnimation();
        load();
    }

    public void load(){
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<List<TransactionServiceDAO>> call = apiService.getAllTransactionService();

        call.enqueue(new Callback<List<TransactionServiceDAO>>() {
            @Override
            public void onResponse(Call<List<TransactionServiceDAO>> call, Response<List<TransactionServiceDAO>> response) {
                for(int i=0;i<response.body().size();i++) {
                    String id = response.body().get(i).getId_tl();

                    if(id.equalsIgnoreCase("false"))
                    {
                        Toast.makeText(TransactionServiceActivity.this, "Transaksi Kosong", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        generateDataList(response.body());
                    }
                }
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<TransactionServiceDAO>> call, Throwable t) {
                Toast.makeText(TransactionServiceActivity.this, "Kesalahan Jaringan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generateDataList(List<TransactionServiceDAO> transactionServiceList) {
        recyclerAdapter = new TransactionServiceRecyclerAdapter(this, transactionServiceList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String queryString) {
                recyclerAdapter.getFilter().filter(queryString);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String queryString) {
                recyclerAdapter.getFilter().filter(queryString);
                return false;
            }
        });
    }
}
