package br.com.smartpromos.api.general.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Leandro on 12/04/2016.
 */
public class OfertaResponse {

    @SerializedName("id")
    private long id_sale;

    @SerializedName("status")
    private int status;

    @SerializedName("enviado")
    private int send;

    @SerializedName("nome")
    private String name;

    @SerializedName("dataInicio")
    private String start_date;

    @SerializedName("dataFim")
    private String over_date;

    @SerializedName("dataCriacao")
    private String creation_date;

    @SerializedName("establishment")
    private long establishment;

    public OfertaResponse(long id_sale, int status, int send, String name, String start_date, String over_date, String creation_date, long establishment) {
        this.id_sale = id_sale;
        this.status = status;
        this.send = send;
        this.name = name;
        this.start_date = start_date;
        this.over_date = over_date;
        this.creation_date = creation_date;
        this.establishment = establishment;
    }

    public long getId_sale() {
        return id_sale;
    }

    public void setId_sale(long id_sale) {
        this.id_sale = id_sale;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSend() {
        return send;
    }

    public void setSend(int send) {
        this.send = send;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getOver_date() {
        return over_date;
    }

    public void setOver_date(String over_date) {
        this.over_date = over_date;
    }

    public String getCreation_date() {
        return creation_date;
    }

    public void setCreation_date(String creation_date) {
        this.creation_date = creation_date;
    }

    public long getEstablishment() {
        return establishment;
    }

    public void setEstablishment(long establishment) {
        this.establishment = establishment;
    }
}
