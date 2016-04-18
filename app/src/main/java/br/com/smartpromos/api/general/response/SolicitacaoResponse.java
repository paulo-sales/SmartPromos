package br.com.smartpromos.api.general.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Leandro on 12/04/2016.
 */
public class SolicitacaoResponse {

    @SerializedName("id")
    private long id_request;

    @SerializedName("statusEstab")
    private int flag_establishment;

    @SerializedName("statusCliente")
    private int flag_customer;

    @SerializedName("place")
    private long place;

    @SerializedName("customer")
    private long customer;

    @SerializedName("establishment")
    private long establishment;

    public SolicitacaoResponse(long id_request, int flag_establishment, int flag_customer, long place, long customer, long establishment) {
        this.id_request = id_request;
        this.flag_establishment = flag_establishment;
        this.flag_customer = flag_customer;
        this.place = place;
        this.customer = customer;
        this.establishment = establishment;
    }

    public long getId_request() {
        return id_request;
    }

    public void setId_request(long id_request) {
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

    public long getPlace() {
        return place;
    }

    public void setPlace(long place) {
        this.place = place;
    }

    public long getCustomer() {
        return customer;
    }

    public void setCustomer(long customer) {
        this.customer = customer;
    }

    public long getEstablishment() {
        return establishment;
    }

    public void setEstablishment(long establishment) {
        this.establishment = establishment;
    }
}
