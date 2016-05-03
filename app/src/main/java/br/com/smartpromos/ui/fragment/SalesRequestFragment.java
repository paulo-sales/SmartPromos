package br.com.smartpromos.ui.fragment;

import android.app.Activity;
import android.net.Uri;
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
import br.com.smartpromos.adapter.ListCouponsToUseAdapter;
import br.com.smartpromos.adapter.ListRequestAdapter;
import br.com.smartpromos.api.general.ServiceGenerator;
import br.com.smartpromos.api.general.SmartRepo;
import br.com.smartpromos.api.general.response.ClienteResponse;
import br.com.smartpromos.api.general.response.CupomResponse;
import br.com.smartpromos.api.general.response.ListaCuponsResponse;
import br.com.smartpromos.api.general.response.ListaCuponsSolicitacao;
import br.com.smartpromos.api.general.response.PlaceResponse;
import br.com.smartpromos.api.general.response.SolicitacaoResponse;
import br.com.smartpromos.util.SmartSharedPreferences;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SalesRequestFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private LinearLayout containerNotice;
    private ClienteResponse cliente;
    private List<PlaceResponse> solitacoes;
    private static SmartRepo smartRepo = ServiceGenerator.createService(SmartRepo.class, BuildConfig.REST_SERVICE_URL, 45);

    public SalesRequestFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sales_request, container, false);

        containerNotice = (LinearLayout) view.findViewById(R.id.containerNotice);

        cliente = SmartSharedPreferences.getUsuarioCompleto(getContext());

        mRecyclerView  = (RecyclerView) view.findViewById(R.id.listRequest);
        mRecyclerView.setHasFixedSize(true);

        solitacoes = new ArrayList<>();

        getRequests();

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        return view;
    }

    private void getRequests(){

        smartRepo.getRequests(cliente.getEmail(), new Callback<ListaCuponsSolicitacao>() {
            @Override
            public void success(ListaCuponsSolicitacao listaSolicitacoes, Response response) {

                if(listaSolicitacoes != null && listaSolicitacoes.getSolicitacoes().size() > 0){

                    for (PlaceResponse p : listaSolicitacoes.getSolicitacoes()) {
                        solitacoes.add(p);
                    }

                    mAdapter = new ListRequestAdapter(solitacoes, getContext());
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
