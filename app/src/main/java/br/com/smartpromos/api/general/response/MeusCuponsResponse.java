package br.com.smartpromos.api.general.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Leandro on 12/04/2016.
 */
public class MeusCuponsResponse {

    @SerializedName("id")
    private int id_my_coupons;

    @SerializedName("status")
    private int status;

    @SerializedName("dataEnvio")
    private String send_date;

    @SerializedName("dataUtilizacao")
    private String use_date;

    @SerializedName("dataExpiracao")
    private String expiration_date;

    @SerializedName("customer")
    private int customer;

    @SerializedName("coupon")
    private int coupon;

    public MeusCuponsResponse(int id_my_coupons, int status, String send_date, String use_date, String expiration_date, int customer, int coupon) {
        this.id_my_coupons = id_my_coupons;
        this.status = status;
        this.send_date = send_date;
        this.use_date = use_date;
        this.expiration_date = expiration_date;
        this.customer = customer;
        this.coupon = coupon;
    }

    public int getId_my_coupons() {
        return id_my_coupons;
    }

    public void setId_my_coupons(int id_my_coupons) {
        this.id_my_coupons = id_my_coupons;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSend_date() {
        return send_date;
    }

    public void setSend_date(String send_date) {
        this.send_date = send_date;
    }

    public String getUse_date() {
        return use_date;
    }

    public void setUse_date(String use_date) {
        this.use_date = use_date;
    }

    public String getExpiration_date() {
        return expiration_date;
    }

    public void setExpiration_date(String expiration_date) {
        this.expiration_date = expiration_date;
    }

    public int getCustomer() {
        return customer;
    }

    public void setCustomer(int customer) {
        this.customer = customer;
    }

    public int getCoupon() {
        return coupon;
    }

    public void setCoupon(int coupon) {
        this.coupon = coupon;
    }
}
