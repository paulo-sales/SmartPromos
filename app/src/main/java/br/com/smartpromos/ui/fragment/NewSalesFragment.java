package br.com.smartpromos.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.smartpromos.BuildConfig;
import br.com.smartpromos.R;
import br.com.smartpromos.adapter.ListCouponsToUseAdapter;
import br.com.smartpromos.api.general.ServiceGenerator;
import br.com.smartpromos.api.general.SmartRepo;
import br.com.smartpromos.api.general.response.ClienteResponse;
import br.com.smartpromos.api.general.response.CupomResponse;
import br.com.smartpromos.api.general.response.ListaCuponsResponse;
import br.com.smartpromos.services.handler.ImageHandler;
import br.com.smartpromos.util.SmartSharedPreferences;
import br.com.smartpromos.util.UIDialogsFragments;
import br.com.smartpromos.util.Util;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class NewSalesFragment extends Fragment {

    private ClienteResponse cliente;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ListaCuponsResponse listaCupons;
    private List<CupomResponse> cupons;
    private static SmartRepo smartRepo = ServiceGenerator.createService(SmartRepo.class, BuildConfig.REST_SERVICE_URL, 45);

    private UIDialogsFragments uiDialogs;
    private View view;

    public NewSalesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_new_sales, container, false);

        uiDialogs = new UIDialogsFragments();
        uiDialogs.uiGetActivity(getActivity());

        cliente = SmartSharedPreferences.getUsuarioCompleto(getContext());

        if(Util.isNetworkAvailable()){

            mRecyclerView  = (RecyclerView) view.findViewById(R.id.listCoupons);
            mRecyclerView.setHasFixedSize(true);

            cupons = new ArrayList<>();

            uiDialogs.showLoading();

            getCupons(1);

            mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);

        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(!Util.isNetworkAvailable()){
            Util.showNetworkInfo(view, getContext());
        }
    }

    private void getCupons(int status){

        smartRepo.cuponsByEmailAndStatus(cliente.getEmail(), status, new Callback<ListaCuponsResponse>() {
            @Override
            public void success(ListaCuponsResponse listaCuponsResponse, Response response) {

                listaCupons = listaCuponsResponse;

                if(listaCupons != null && listaCupons.getCupons().size() > 0){

                    for (CupomResponse r : listaCupons.getCupons()) {
                        ImageHandler.generateFeedfileImage(r.getPath_img(), String.valueOf(r.getId_coupon()));
                        cupons.add(r);
                    }

                    mAdapter = new ListCouponsToUseAdapter(cupons, getContext(), getActivity());
                    mRecyclerView.setAdapter(mAdapter);
                }else{
                    Toast.makeText(getContext(), getActivity().getResources().getString(R.string.txt_no_coupons_found), Toast.LENGTH_LONG).show();
                }

                uiDialogs.loadingDialog.dismiss();

            }

            @Override
            public void failure(RetrofitError error) {

                uiDialogs.loadingDialog.dismiss();

            }
        });


    }

}
