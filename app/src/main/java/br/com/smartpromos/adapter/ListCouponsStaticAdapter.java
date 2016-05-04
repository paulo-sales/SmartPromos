package br.com.smartpromos.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.smartpromos.R;
import br.com.smartpromos.api.general.response.CupomResponse;
import br.com.smartpromos.services.handler.ImageHandler;
import br.com.smartpromos.ui.activity.DetailsCouponStaticActivity;

/**
 * Created by Paulo on 20/04/2016.
 */
public class ListCouponsStaticAdapter extends RecyclerView.Adapter<ListCouponsStaticAdapter.ViewHolder>   {
    private static Context context;
    private List<CupomResponse> cupons;
    private int lastPosition = -1;

    public ListCouponsStaticAdapter(List<CupomResponse> cupons, Context context){

        if(this.cupons == null)
            this.cupons = new ArrayList<>();

        this.cupons = cupons;
        this.context = context;

    }

    @Override
    public ListCouponsStaticAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(context).inflate(R.layout.item_coupon_discarded, null);
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

            //Bitmap bitmap = ImageHandler.loadImagem(cupomResponse.getPath_img());
            Bitmap bitmap = ImageHandler.getImageBitmap(String.valueOf(cupomResponse.getId_coupon()), cupomResponse.getPath_img());
            Drawable drawable = new BitmapDrawable(context.getResources(), bitmap);

            holder.containerImgCoupon.setBackground(drawable);

            holder.btnEnviar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToCoupon(cupomResponse);
                }
            });

        }
        //setAnimation(holder.container, position);
    }

    @Override
    public int getItemCount() {

        return cupons.size();
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
        Intent intent = new Intent(context, DetailsCouponStaticActivity.class);
        intent.putExtra("cupomid", String.valueOf(cupomResponse.getId_coupon()));
        context.startActivity(intent);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private CardView container;
        private TextView tituEmpresa;
        private RelativeLayout containerImgCoupon;
        private TextView tituCoupon;
        private TextView txtTotal;
        private TextView txtShortDescription;
        private TextView txtInicio;
        private TextView txtFim;
        private Button btnEnviar;

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
        }
    }
}
