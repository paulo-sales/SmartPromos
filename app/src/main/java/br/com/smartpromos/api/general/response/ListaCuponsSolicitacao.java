package br.com.smartpromos.api.general.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Leandro on 12/04/2016.
 */
public class ListaCuponsSolicitacao {

    @SerializedName("solicitacoes")
    private List<PlaceResponse> solicitacoes;

    public ListaCuponsSolicitacao(List<PlaceResponse> solicitacoes) {
        this.solicitacoes = solicitacoes;
    }

    public List<PlaceResponse> getSolicitacoes() {
        return solicitacoes;
    }

    public void setSolicitacoes(List<PlaceResponse> solicitacoes) {
        this.solicitacoes = solicitacoes;
    }
}
