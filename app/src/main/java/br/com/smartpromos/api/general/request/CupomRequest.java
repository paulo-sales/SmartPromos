package br.com.smartpromos.api.general.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Leandro on 12/04/2016.
 */
public class CupomRequest {

    @SerializedName("id")
    private int id_coupon;

    @SerializedName("nome")
    private String name;

    @SerializedName("descricao")
    private String description;

    @SerializedName("mensagemConfirmacao")
    private String confirmationmessage;

    @SerializedName("pathImg")
    private String path_img;

    @SerializedName("tipo")
    private int type;

    @SerializedName("notificacao")
    private int notification;

    @SerializedName("cabecalho")
    private String mensage;

    @SerializedName("quantidade")
    private int quantity;

    @SerializedName("sale")
    private int sale;

    @SerializedName("msg")
    private MensagemRequest mensagem;

    public CupomRequest(int id_coupon, String name, String description, String confirmationmessage, String path_img, int type, int notification, String mensage, int quantity, int sale, MensagemRequest mensagem) {
        this.id_coupon = id_coupon;
        this.name = name;
        this.description = description;
        this.confirmationmessage = confirmationmessage;
        this.path_img = path_img;
        this.type = type;
        this.notification = notification;
        this.mensage = mensage;
        this.quantity = quantity;
        this.sale = sale;
        this.mensagem = mensagem;
    }

    public MensagemRequest getMensagem() {
        return mensagem;
    }

    public void setMensagem(MensagemRequest mensagem) {
        this.mensagem = mensagem;
    }


    public int getId_coupon() {
        return id_coupon;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getConfirmationmessage() {
        return confirmationmessage;
    }

    public String getPath_img() {
        return path_img;
    }

    public int getType() {
        return type;
    }

    public int getNotification() {
        return notification;
    }

    public String getMensage() {
        return mensage;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getSale() {
        return sale;
    }

    public void setId_coupon(int id_coupon) {
        this.id_coupon = id_coupon;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setConfirmationmessage(String confirmationmessage) {
        this.confirmationmessage = confirmationmessage;
    }

    public void setPath_img(String path_img) {
        this.path_img = path_img;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setNotification(int notification) {
        this.notification = notification;
    }

    public void setMensage(String mensage) {
        this.mensage = mensage;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setSale(int sale) {
        this.sale = sale;
    }
}
