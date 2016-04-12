package br.com.smartpromos.api.general.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Leandro on 12/04/2016.
 */
public class ListaCuponsResponse {

    @SerializedName("cupons")
    private List<CupomResponse> cupons;

    public ListaCuponsResponse(List<CupomResponse> cupons) {
        this.cupons = cupons;
    }

    public List<CupomResponse> getCupons() {
        return cupons;
    }

    public void setCupons(List<CupomResponse> cupons) {
        this.cupons = cupons;
    }
}
