package com.tubes.kouveepetshop.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tubes.kouveepetshop.API.ApiClient;
import com.tubes.kouveepetshop.API.ApiInterface;
import com.tubes.kouveepetshop.Model.DetailTransactionServiceDAO;
import com.tubes.kouveepetshop.R;
import com.tubes.kouveepetshop.RecyclerAdapter.HistoryDetailTransactionServiceRecyclerAdapter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryDetailTransactionServiceFragment extends DialogFragment {
  private ImageButton btnClose;
  private TextView twCode, twPet, twTotal;
  private String sId, sPet, sCode, sTotal;
  private List<DetailTransactionServiceDAO> serviceList;
  private RecyclerView recyclerView;
  private HistoryDetailTransactionServiceRecyclerAdapter recyclerAdapter;
  private ProgressDialog progressDialog;

  private Locale localeID = new Locale("in", "ID");
  private NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

  public HistoryDetailTransactionServiceFragment(String id_tl) {
    this.sId = id_tl;
  }


  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public void onStart() {
    super.onStart();
    getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_detail_transaction_service, container, false);

    progressDialog = new ProgressDialog(getContext());
    progressDialog.show();

    twCode = v.findViewById(R.id.twCode);
    twPet = v.findViewById(R.id.twPet);
    twTotal = v.findViewById(R.id.twTotal);
    btnClose = v.findViewById(R.id.btnClose);

    btnClose.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        dismiss();
      }
    });

    serviceList = new ArrayList<>();
    recyclerView = v.findViewById(R.id.restoreTSRecyclerView);
    recyclerView.setItemAnimator(new DefaultItemAnimator());

    serviceList.removeAll(serviceList);
    load();

    sCode = getArguments().getString("code", "");
    sPet = getArguments().getString("pet", "");
    sTotal = getArguments().getString("total", "");

    twCode.setText(sCode);
    twPet.setText(sPet);
    twTotal.setText(formatRupiah.format((double)Double.parseDouble(sTotal)));

    return v;
  }

  public void load(){
    ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
    Call<List<DetailTransactionServiceDAO>> call = apiService.getByIDTSDetailTS(sId);

    call.enqueue(new Callback<List<DetailTransactionServiceDAO>>() {
      @Override
      public void onResponse(Call<List<DetailTransactionServiceDAO>> call, Response<List<DetailTransactionServiceDAO>> response) {
        generateDataList(response.body());
      }

      @Override
      public void onFailure(Call<List<DetailTransactionServiceDAO>> call, Throwable t) {
        progressDialog.dismiss();
      }
    });
  }

  private void generateDataList(List<DetailTransactionServiceDAO> serviceList) {
    recyclerAdapter = new HistoryDetailTransactionServiceRecyclerAdapter(getContext(),serviceList);
    recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
    recyclerView.setAdapter(recyclerAdapter);
    progressDialog.dismiss();
  }
}
