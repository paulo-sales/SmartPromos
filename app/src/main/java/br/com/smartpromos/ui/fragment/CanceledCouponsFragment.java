package br.com.smartpromos.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import br.com.smartpromos.BuildConfig;
import br.com.smartpromos.R;
import br.com.smartpromos.adapter.ListCouponsDiscardedAdapter;
import br.com.smartpromos.api.general.ServiceGenerator;
import br.com.smartpromos.api.general.SmartRepo;
import br.com.smartpromos.api.general.response.ClienteResponse;
import br.com.smartpromos.api.general.response.CupomResponse;
import br.com.smartpromos.api.general.response.ListaCuponsResponse;
import br.com.smartpromos.util.SmartSharedPreferences;
import br.com.smartpromos.util.Util;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CanceledCouponsFragment extends Fragment {

    private ClienteResponse cliente;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private LinearLayout containerNotice;
    private ListaCuponsResponse listaCupons;
    private List<CupomResponse> cupons;
    private static SmartRepo smartRepo = ServiceGenerator.createService(SmartRepo.class, BuildConfig.REST_SERVICE_URL, 45);
    private View view;

    public CanceledCouponsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_canceled_coupons, container, false);

        containerNotice = (LinearLayout) view.findViewById(R.id.containerNotice);

        cliente = SmartSharedPreferences.getUsuarioCompleto(getContext());

        containerNotice.setVisibility(View.INVISIBLE);

        mRecyclerView  = (RecyclerView) view.findViewById(R.id.listCoupons);
        mRecyclerView.setHasFixedSize(true);

        cupons = new ArrayList<>();

        getCupons(3);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if(!Util.isNetworkAvailable()){
            Util.showNetworkInfo(view, getContext());
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void getCupons(int status){

        smartRepo.cuponsByEmailAndStatus(cliente.getEmail(), status, new Callback<ListaCuponsResponse>() {
            @Override
            public void success(ListaCuponsResponse listaCuponsResponse, Response response) {

                listaCupons = listaCuponsResponse;

                if(listaCupons != null && listaCupons.getCupons().size() > 0){

                    for (CupomResponse r : listaCupons.getCupons()) {
                        cupons.add(r);
                    }

                    mAdapter = new ListCouponsDiscardedAdapter(cupons, getContext());
                    mRecyclerView.setAdapter(mAdapter);
                }else{
                    containerNotice.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });


    }
}
