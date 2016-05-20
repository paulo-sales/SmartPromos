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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.smartpromos.BuildConfig;
import br.com.smartpromos.R;
import br.com.smartpromos.api.general.ServiceGenerator;
import br.com.smartpromos.api.general.SmartRepo;
import br.com.smartpromos.api.general.response.ClienteResponse;
import br.com.smartpromos.api.general.response.CupomResponse;
import br.com.smartpromos.api.general.response.MensagemResponse;
import br.com.smartpromos.api.general.response.PlaceResponse;
import br.com.smartpromos.api.general.response.SolicitacaoResponse;
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
public class ListRequestAdapter extends RecyclerView.Adapter<ListRequestAdapter.ViewHolder>   {

    private static Context context;
    private List<PlaceResponse> places;
    private long nextItem = 0;

    public ListRequestAdapter(List<PlaceResponse> places, Context context){

        if(this.places == null)
            this.places = new ArrayList<>();

        this.places = places;
        this.context = context;

    }

    @Override
    public ListRequestAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(context).inflate(R.layout.item_solicitacao, parent, false);
        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        PlaceResponse place = places.get(position);

        if( place != null){

            holder.tituEmpresa.setText(place.getName());
            holder.endEmpresa.setText(place.getAdr_address());

            if(!place.getIcon().equals("") && place.getIcon() != null){
                holder.containerIcon.setVisibility(View.VISIBLE);
                Bitmap bitmap = ImageHandler.getImageBitmap(String.valueOf(place.getPlace_id()), place.getIcon());
                holder.iconPlace.setImageBitmap(bitmap);
            }

        }
        //setAnimation(holder.container);
    }

    @Override
    public int getItemCount() {

        return places.size();
    }

    /**
     * Here is the key method to apply the animation
     */
    private void setAnimation(View viewToAnimate){

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.animate_bottom_to_up);

        nextItem = animation.getStartOffset();
        nextItem = nextItem + 200;
        animation.setStartOffset(nextItem);

        viewToAnimate.startAnimation(animation);
    }

    public void addListItem(PlaceResponse place, int position) {

        places.add(place);
        notifyItemInserted(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        private LinearLayout container;
        private TextView tituEmpresa;
        private TextView endEmpresa;
        private LinearLayout containerIcon;
        private ImageView iconPlace;

        public ViewHolder(View itemView) {
            super(itemView);

            container           = (LinearLayout) itemView.findViewById(R.id.container);
            tituEmpresa         = (TextView) itemView.findViewById(R.id.tituEmpresa);
            endEmpresa          = (TextView) itemView.findViewById(R.id.endEmpresa);
            containerIcon       = (LinearLayout) itemView.findViewById(R.id.containerIcon);
            iconPlace           = (ImageView) itemView.findViewById(R.id.iconPlace);

        }
    }
}
