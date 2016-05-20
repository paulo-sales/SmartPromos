package br.com.smartpromos.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.smartpromos.BuildConfig;
import br.com.smartpromos.R;
import br.com.smartpromos.api.general.ServiceGenerator;
import br.com.smartpromos.api.general.SmartRepo;
import br.com.smartpromos.api.general.response.ClienteResponse;
import br.com.smartpromos.api.general.response.CupomResponse;
import br.com.smartpromos.services.handler.ImageHandler;
import br.com.smartpromos.ui.activity.CouponDetailsUsableActivity;
import br.com.smartpromos.util.UIDialogsFragments;

/**
 * Created by Paulo on 20/04/2016.
 */
public class ListCouponsAdapter extends RecyclerView.Adapter<ListCouponsAdapter.ViewHolder>   {

    private static Context context;
    private static List<CupomResponse> cupons;
    private int lastPosition = -1;
    private static ClienteResponse cliente;
    private static FragmentActivity fragmentActivity;
    private static UIDialogsFragments uiDialogs;

    public ListCouponsAdapter(List<CupomResponse> cupons, Context context, FragmentActivity fragmentActivity, ClienteResponse clienteResponse){

        if(this.cupons == null)
            this.cupons = new ArrayList<>();

        this.cupons = cupons;
        this.context = context;
        this.cliente = clienteResponse;
        this.fragmentActivity = fragmentActivity;
        uiDialogs = new UIDialogsFragments();
        uiDialogs.uiGetActivity(fragmentActivity);

    }

    @Override
    public ListCouponsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(context).inflate(R.layout.item_coupon, parent, false);
        return new ViewHolder(itemLayoutView);
    }

    @Override
    public int getItemCount() {
        return cupons.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void updateList(List<CupomResponse> cupons) {
        this.cupons = cupons;
        notifyDataSetChanged();
    }

    public void addListItem(CupomResponse cupom, int position) {

        Log.v("Insert "+cupom.getName(), "All positions: "+(cupons.size()-1)+". New Position: "+position);

        cupons.add(cupom);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(CupomResponse cupom) {
        int position = cupons.indexOf(cupom);
        cupons.remove(position);
        notifyItemRemoved(position);
    }

    private void setAnimation(View viewToAnimate, int position){
        // If the bound view wasn't previously displayed on screen, it's animated
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.animate_bottom_to_up);
        viewToAnimate.startAnimation(animation);
    }

    @Override
    public void onBindViewHolder(final ListCouponsAdapter.ViewHolder holder, int position) {

        final CupomResponse cupomResponse = cupons.get(position);

        if( cupomResponse != null){

            String localizacao = cupomResponse.getSale().getEstablishment().getNeighborwood() +" - "+ cupomResponse.getSale().getEstablishment().getState()+". 9km";

            holder.tituCoupon.setText(cupomResponse.getName());
            holder.txtTotal.setText(String.valueOf(cupomResponse.getQuantity()));
            holder.txtShortDescription.setText(cupomResponse.getMensage());
            holder.txtDate.setText("Válida até "+cupomResponse.getSale().getOver_date());
            holder.txtLocal.setText(localizacao);
            holder.txtUtilizados.setText("10 utilizados");

            if( !cupomResponse.getPath_img().equalsIgnoreCase("") && cupomResponse.getPath_img() != null ){

                Bitmap bitmap = ImageHandler.getImageBitmap(String.valueOf(cupomResponse.getId_coupon()), cupomResponse.getPath_img());
                Drawable drawable = new BitmapDrawable(context.getResources(), bitmap);

                holder.imageCupom.setBackground(drawable);
            }


        }
        //setAnimation(holder, position);
        holder.itemView.setTag(cupons.get(position));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView imageCupom;
        private TextView tituCoupon;
        private TextView txtTotal;
        private TextView txtShortDescription;
        private TextView txtLocal;
        private TextView txtUtilizados;
        private TextView txtDate;

        public ViewHolder(View itemView) {
            super(itemView);

            imageCupom          = (ImageView) itemView.findViewById(R.id.imageCupom);
            tituCoupon          = (TextView) itemView.findViewById(R.id.tituCoupon);
            tituCoupon          = (TextView) itemView.findViewById(R.id.tituCoupon);
            txtTotal            = (TextView) itemView.findViewById(R.id.txtTotal);
            txtShortDescription = (TextView) itemView.findViewById(R.id.txtShortDescription);
            txtLocal            = (TextView) itemView.findViewById(R.id.txtLocal);
            txtUtilizados       = (TextView) itemView.findViewById(R.id.txtUtilizados);
            txtDate             = (TextView) itemView.findViewById(R.id.txtDate);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            final CupomResponse cupomResponse = cupons.get(getPosition());
            goToCoupon(cupomResponse);
        }

        public void goToCoupon(CupomResponse cupomResponse){
            Intent intent = new Intent(context, CouponDetailsUsableActivity.class);
            intent.putExtra("cupomid", String.valueOf(cupomResponse.getId_coupon()));
            context.startActivity(intent);
        }
    }
}
