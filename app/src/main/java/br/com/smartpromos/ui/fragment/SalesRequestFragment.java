package br.com.smartpromos.ui.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.smartpromos.BuildConfig;
import br.com.smartpromos.R;
import br.com.smartpromos.adapter.ListCouponsAdapter;
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
import br.com.smartpromos.services.handler.ImageHandler;
import br.com.smartpromos.util.SmartSharedPreferences;
import br.com.smartpromos.util.UIDialogsFragments;
import br.com.smartpromos.util.Util;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SalesRequestFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private ClienteResponse cliente;
    private List<PlaceResponse> solitacoes;
    private static SmartRepo smartRepo = ServiceGenerator.createService(SmartRepo.class, BuildConfig.REST_SERVICE_URL, 45);
    private View view;

    private int PageLoaded = 0;

    private UIDialogsFragments uiDialogs;

    public SalesRequestFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sales_request, container, false);

        cliente = SmartSharedPreferences.getUsuarioCompleto(getContext());

        solitacoes = new ArrayList<>();

        mRecyclerView  = (RecyclerView) view.findViewById(R.id.listRequest);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager mLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                ListRequestAdapter mAdapter = (ListRequestAdapter) mRecyclerView.getAdapter();

                if(Util.isNetworkAvailable()){

                    if(solitacoes.size() == mLayoutManager.findLastCompletelyVisibleItemPosition()+1){

                        getRequests(mAdapter);
                    }
                }else{
                    Util.showNetworkInfo(view, getContext());
                }


            }
        });

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        ListRequestAdapter mAdapter = new ListRequestAdapter(solitacoes, getContext());
        mRecyclerView.setAdapter(mAdapter);

        if(Util.isNetworkAvailable()){

            getRequests(mAdapter);
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

    private void getRequests(final ListRequestAdapter mAdapter){

        uiDialogs = new UIDialogsFragments();
        uiDialogs.uiGetActivity(getActivity());
        uiDialogs.showLoading();

        smartRepo.getRequests(cliente.getEmail(), PageLoaded, new Callback<ListaCuponsSolicitacao>() {
            @Override
            public void success(ListaCuponsSolicitacao listaSolicitacoes, Response response) {

                if(listaSolicitacoes != null && listaSolicitacoes.getSolicitacoes().size() > 0){

                    for (PlaceResponse p : listaSolicitacoes.getSolicitacoes()) {
                        ImageHandler.generateFeedfileImage(p.getIcon(), String.valueOf(p.getPlace_id()));
                        mAdapter.addListItem(p, solitacoes.size());
                    }

                    PageLoaded = PageLoaded+12;
                }else{

                    Toast.makeText(getContext(), getActivity().getResources().getString(R.string.txt_no_sales_requested), Toast.LENGTH_LONG).show();
                }

                uiDialogs.loadingDialog.dismiss();
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
