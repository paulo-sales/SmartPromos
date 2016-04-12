package br.com.smartpromos.api.general.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Leandro on 12/04/2016.
 */
public class ListaCuponsSolicitacao {

    @SerializedName("solicitacoes")
    private List<SolicitacaoResponse> solicitacoes;

    public ListaCuponsSolicitacao(List<SolicitacaoResponse> solicitacoes) {
        this.solicitacoes = solicitacoes;
    }

    public List<SolicitacaoResponse> getCupons() {
        return solicitacoes;
    }

    public void setCupons(List<SolicitacaoResponse> solicitacoes) {
        this.solicitacoes = solicitacoes;
    }
}
