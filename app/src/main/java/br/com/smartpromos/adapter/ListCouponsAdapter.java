package br.com.smartpromos.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import br.com.smartpromos.BuildConfig;
import br.com.smartpromos.R;
import br.com.smartpromos.api.general.ServiceGenerator;
import br.com.smartpromos.api.general.SmartRepo;
import br.com.smartpromos.api.general.response.ClienteResponse;
import br.com.smartpromos.api.general.response.CupomResponse;
import br.com.smartpromos.api.general.response.MensagemResponse;
import br.com.smartpromos.services.handler.ImageHandler;
import br.com.smartpromos.ui.activity.CouponDetailsActivity;
import br.com.smartpromos.ui.fragment.NewSalesFragment;
import br.com.smartpromos.ui.fragment.SalesDiscardedFragment;
import br.com.smartpromos.util.UIDialogsFragments;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Paulo on 20/04/2016.
 */
public class ListCouponsAdapter extends RecyclerView.Adapter<ListCouponsAdapter.ViewHolder>   {

    private static Context context;
    private List<CupomResponse> cupons;
    private int lastPosition = -1;
    private static ClienteResponse cliente;
    private static SmartRepo smartRepo = ServiceGenerator.createService(SmartRepo.class, BuildConfig.REST_SERVICE_URL, 45);
    private static FragmentActivity fragmentActivity;
    private static UIDialogsFragments uiDialogs;
    private static Button btnActionOne;
    private static Button btnActionTwo;
    private static Button btnActionTree;
    private static TextView HeaderTitle;

    public ListCouponsAdapter(List<CupomResponse> cupons, Context context, FragmentActivity fragmentActivity, ClienteResponse clienteResponse){

        if(this.cupons == null)
            this.cupons = new ArrayList<>();

        this.cupons = cupons;
        this.context = context;
        this.cliente = clienteResponse;
        this.fragmentActivity = fragmentActivity;
        uiDialogs = new UIDialogsFragments();
        uiDialogs.uiGetActivity(fragmentActivity);

        HeaderTitle = (TextView) fragmentActivity.findViewById(R.id.HeaderTitle);
        btnActionOne = (Button) fragmentActivity.findViewById(R.id.btnActionOne);
        btnActionTwo = (Button) fragmentActivity.findViewById(R.id.btnActionTwo);
        btnActionTree = (Button) fragmentActivity.findViewById(R.id.btnActionTree);

    }

    @Override
    public ListCouponsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(context).inflate(R.layout.item_coupon, null);
        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        final CupomResponse cupomResponse = cupons.get(position);

        if( cupomResponse != null){

            holder.tituEmpresa.setText(cupomResponse.getSale().getEstablishment().getFantasy_name());
            holder.tituCoupon.setText(cupomResponse.getName());
            holder.txtTotal.setText(String.valueOf(cupomResponse.getQuantity()));
            holder.txtShortDescription.setText(cupomResponse.getMensage());
            holder.txtInicio.setText(cupomResponse.getSale().getStart_date());
            holder.txtFim.setText(cupomResponse.getSale().getOver_date());

            Bitmap bitmap = ImageHandler.loadImagem(cupomResponse.getPath_img());
            Drawable drawable = new BitmapDrawable(context.getResources(), bitmap);
            holder.containerImgCoupon.setBackground(drawable);

            holder.btnEnviar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToCoupon(cupomResponse);
                }
            });

            holder.btnAceitar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setStatus(1, cupomResponse);
                }
            });

            holder.btnRecusar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setStatus(2, cupomResponse);
                }
            });

        }
        setAnimation(holder.container, position);
    }

    @Override
    public int getItemCount() {

        return cupons.size();
    }

    public void setStatus(final int status, CupomResponse cupom){

        smartRepo.cupomStatus(cupom.getId_coupon(), cliente.getDoc_id(), status, new Callback<MensagemResponse>() {
            @Override
            public void success(MensagemResponse mensagemResponse, Response response) {
                uiDialogs.showDialog(mensagemResponse.getMensagem(), "");

                if(status == 1 ){
                    NewSalesFragment salesfrag = new NewSalesFragment();

                    FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.fragment_container, salesfrag);
                    transaction.commit();

                    HeaderTitle.setText(context.getResources().getString(R.string.txt_coupons));

                    btnActionOne.setEnabled(false);
                    btnActionOne.setTextColor(context.getResources().getColor(R.color.colorWhite));
                    btnActionTwo.setEnabled(true);
                    btnActionTwo.setTextColor(context.getResources().getColor(R.color.colorBlack));
                    btnActionTree.setEnabled(true);
                    btnActionTree.setTextColor(context.getResources().getColor(R.color.colorBlack));

                    btnActionOne.setText("Novos");
                    btnActionTwo.setText("Usados");
                    btnActionTree.setText("Expirados");
                }

                if(status == 2 ){

                    SalesDiscardedFragment salesfrag = new SalesDiscardedFragment();

                    FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.fragment_container, salesfrag);
                    transaction.commit();

                    HeaderTitle.setText(context.getResources().getString(R.string.txt_sales));

                    btnActionOne.setEnabled(true);
                    btnActionOne.setTextColor(context.getResources().getColor(R.color.colorBlack));
                    btnActionTwo.setEnabled(true);
                    btnActionTwo.setTextColor(context.getResources().getColor(R.color.colorBlack));
                    btnActionTree.setEnabled(false);
                    btnActionTree.setTextColor(context.getResources().getColor(R.color.colorWhite));

                    btnActionOne.setText("Recebidas");
                    btnActionTwo.setText("Solicitadas");
                    btnActionTree.setText("Descartadas");
                }
            }

            @Override
            public void failure(RetrofitError error) {
                uiDialogs.showDialog("Erro interno", "Ocorreu um erro ao tentar selecionar o cupom.");
            }
        });

    }

    /**
     * Here is the key method to apply the animation
     */
    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.animate_bottom_to_up);
        viewToAnimate.startAnimation(animation);
    }

    public void goToCoupon(CupomResponse cupomResponse){
        Intent intent = new Intent(context, CouponDetailsActivity.class);
        intent.putExtra("cupomid", String.valueOf(cupomResponse.getId_coupon()));
        context.startActivity(intent);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private CardView container;
        private TextView tituEmpresa;
        private RelativeLayout containerImgCoupon;
        private TextView txtTotal;
        private TextView tituCoupon;
        private TextView txtShortDescription;
        private TextView txtInicio;
        private TextView txtFim;
        private Button btnEnviar;
        private ImageButton btnAceitar;
        private ImageButton btnRecusar;


        public ViewHolder(View itemView) {
            super(itemView);

            container           = (CardView) itemView.findViewById(R.id.container);
            tituEmpresa         = (TextView) itemView.findViewById(R.id.tituEmpresa);
            containerImgCoupon  = (RelativeLayout) itemView.findViewById(R.id.containerImgCoupon);
            tituCoupon          = (TextView) itemView.findViewById(R.id.tituCoupon);
            txtTotal            = (TextView) itemView.findViewById(R.id.txtTotal);
            txtShortDescription = (TextView) itemView.findViewById(R.id.txtShortDescription);
            txtInicio           = (TextView) itemView.findViewById(R.id.txtInicio);
            txtFim              = (TextView) itemView.findViewById(R.id.txtFim);
            btnEnviar           = (Button) itemView.findViewById(R.id.btnEnviar);
            btnAceitar          = (ImageButton) itemView.findViewById(R.id.btnAceitar);
            btnRecusar          = (ImageButton) itemView.findViewById(R.id.btnRecusar);
        }
    }
}
