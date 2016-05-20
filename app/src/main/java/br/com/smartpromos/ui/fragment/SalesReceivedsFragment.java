package br.com.smartpromos.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

public class SalesReceivedsFragment extends Fragment {

    private ClienteResponse cliente;
    private RecyclerView mRecyclerView;
    private ListaCuponsResponse listaCupons;
    private List<CupomResponse> cupons;
    private static SmartRepo smartRepo = ServiceGenerator.createService(SmartRepo.class, BuildConfig.REST_SERVICE_URL, 45);

    private UIDialogsFragments uiDialogs;

    private View view;

    private SwipeRefreshLayout swipeRefresh;

    private int PageLoaded = 0;

    public SalesReceivedsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_sales, container, false);

        cliente = SmartSharedPreferences.getUsuarioCompleto(getContext());

        cupons = new ArrayList<>();

        mRecyclerView  = (RecyclerView) view.findViewById(R.id.listCoupons);
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
                ListCouponsAdapter mAdapter = (ListCouponsAdapter) mRecyclerView.getAdapter();

                if(Util.isNetworkAvailable()){

                    if(cupons.size() == mLayoutManager.findLastCompletelyVisibleItemPosition()+1){

                        getMoreCupons(mAdapter);
                    }
                }else{
                    Util.showNetworkInfo(view, getContext());
                }


            }
        });

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        if(Util.isNetworkAvailable()){

            getCupons();
        }

        swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefresh);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if(Util.isNetworkAvailable()){

                    getCupons();

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            SystemClock.sleep(2000);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    swipeRefresh.setRefreshing(false);
                                }
                            });
                        }
                    }).start();
                }else{

                    swipeRefresh.setRefreshing(false);
                    Util.showNetworkInfo(view, getContext());
                }

            }
        });

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

    private void getCupons(){

        uiDialogs = new UIDialogsFragments();
        uiDialogs.uiGetActivity(getActivity());
        uiDialogs.showLoading();

        smartRepo.cuponsByEmailListAll(cliente.getEmail(), PageLoaded, new Callback<ListaCuponsResponse>() {
            @Override
            public void success(ListaCuponsResponse listaCuponsResponse, Response response) {

                listaCupons = listaCuponsResponse;

                if(listaCupons != null && listaCupons.getCupons().size() > 0){


                    for (CupomResponse r : listaCupons.getCupons()) {
                        ImageHandler.generateFeedfileImage(r.getPath_img(), String.valueOf(r.getId_coupon()));
                        cupons.add(r);
                    }

                    ListCouponsAdapter mAdapter = new ListCouponsAdapter(cupons, getContext(), getActivity(), cliente);
                    mRecyclerView.setAdapter(mAdapter);

                    PageLoaded = PageLoaded+3;
                }else{
                    Toast.makeText(getContext(), getActivity().getResources().getString(R.string.txt_no_sales_received), Toast.LENGTH_LONG).show();
                }

                uiDialogs.loadingDialog.dismiss();

            }

            @Override
            public void failure(RetrofitError error) {

                uiDialogs.loadingDialog.dismiss();

            }
        });

    }

    private void getMoreCupons(final ListCouponsAdapter mAdapter){

        uiDialogs = new UIDialogsFragments();
        uiDialogs.uiGetActivity(getActivity());
        uiDialogs.showLoading();

        smartRepo.cuponsByEmailListAll(cliente.getEmail(), PageLoaded, new Callback<ListaCuponsResponse>() {
            @Override
            public void success(ListaCuponsResponse listaCuponsResponse, Response response) {

                listaCupons = listaCuponsResponse;

                if(listaCupons != null && listaCupons.getCupons().size() > 0){

                    for (CupomResponse r : listaCupons.getCupons()) {
                        ImageHandler.generateFeedfileImage(r.getPath_img(), String.valueOf(r.getId_coupon()));
                        mAdapter.addListItem(r, cupons.size());
                    }

                }else{
                    Toast.makeText(getContext(), getActivity().getResources().getString(R.string.txt_no_sales_received), Toast.LENGTH_LONG).show();
                }

                uiDialogs.loadingDialog.dismiss();
                PageLoaded = PageLoaded+3;
            }

            @Override
            public void failure(RetrofitError error) {

                uiDialogs.loadingDialog.dismiss();

            }
        });

    }

}
