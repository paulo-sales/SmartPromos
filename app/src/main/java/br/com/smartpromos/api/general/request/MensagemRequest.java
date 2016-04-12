package br.com.smartpromos.api.general.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Leandro on 12/04/2016.
 */
public class MensagemRequest {

    @SerializedName("id")
    private int id;

    @SerializedName("mensagem")
    private String mensagem;

    public MensagemRequest(int id, String mensagem) {
        this.id = id;
        this.mensagem = mensagem;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
