package br.com.smartpromos.api.general.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Leandro on 12/04/2016.
 */
public class SolicitacaoResponse {

    @SerializedName("id")
    private int id_request;

    @SerializedName("statusEstab")
    private int flag_establishment;

    @SerializedName("statusCliente")
    private int flag_customer;

    @SerializedName("place")
    private int place;

    @SerializedName("customer")
    private int customer;

    @SerializedName("establishment")
    private int establishment;

    public SolicitacaoResponse(int id_request, int flag_establishment, int flag_customer, int place, int customer, int establishment) {
        this.id_request = id_request;
        this.flag_establishment = flag_establishment;
        this.flag_customer = flag_customer;
        this.place = place;
        this.customer = customer;
        this.establishment = establishment;
    }

    public int getId_request() {
        return id_request;
    }

    public void setId_request(int id_request) {
        this.id_request = id_request;
    }

    public int getFlag_establishment() {
        return flag_establishment;
    }

    public void setFlag_establishment(int flag_establishment) {
        this.flag_establishment = flag_establishment;
    }

    public int getFlag_customer() {
        return flag_customer;
    }

    public void setFlag_customer(int flag_customer) {
        this.flag_customer = flag_customer;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    public int getCustomer() {
        return customer;
    }

    public void setCustomer(int customer) {
        this.customer = customer;
    }

    public int getEstablishment() {
        return establishment;
    }

    public void setEstablishment(int establishment) {
        this.establishment = establishment;
    }
}
