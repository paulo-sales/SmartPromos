package br.com.smartpromos.api.general;

import br.com.smartpromos.api.general.response.ClienteResponse;
import br.com.smartpromos.api.general.response.CupomResponse;
import br.com.smartpromos.api.general.response.ListaCuponsResponse;
import br.com.smartpromos.api.general.response.ListaCuponsSolicitacao;
import br.com.smartpromos.api.general.response.MeusCuponsResponse;
import br.com.smartpromos.api.general.response.PlaceResponse;
import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by Dany on 12/04/2016.
 */
public interface SmartRepo {

    ///prc/prc.php?ctl=mobile
    @FormUrlEncoded
    @POST("/prc/prc.php?ctl=mobile&acao=insertCustmer")
    void criarCadastro(@Field("cliente") String cliente,
                       @Field("localizacao") String localizacao,
                        Callback<ClienteResponse> clienteResponseCallback);

    @FormUrlEncoded
    @POST("/prc/prc.php?ctl=mobile&acao=loginCustmerUser")
    void loginCliente(@Field("username") String user,
                 @Field("password") String password,
                 Callback<ClienteResponse> clienteResponseCallback);

    @FormUrlEncoded
    @POST("/prc/prc.php?ctl=mobile&acao=getListaPlacesBySolicitadosClientes")
    void listaPlacesbyEmail(@Field("email") String email,
                       Callback<ListaCuponsSolicitacao> listaCuponsSolicitacaoCallback);

    @FormUrlEncoded
    @POST("/prc/prc.php?ctl=mobile&acao=getCouponsByEmail")
    void cuponsByEmail(@Field("email") String email,
                  @Field("status") String status,
                  Callback<ListaCuponsResponse> listaCuponsResponseCallback);

    @FormUrlEncoded
    @POST("/prc/prc.php?ctl=mobile&acao=getCupomByIdMobile")
    void cuponsByEmail(@Field("cupomId") String cupomId,
                  Callback<CupomResponse> cupomResponseCallback);

    @FormUrlEncoded
    @POST("/prc/prc.php?ctl=mobile&acao=setCupomStatus")
    void cupomStatus(@Field("myCupomId") String cupomId,
                @Field("cpf") String cpf,
                Callback<CupomResponse> cupomResponseCallback);

    @FormUrlEncoded
    @POST("/prc/prc.php?ctl=mobile&acao=descartarCuponsAceitos")
    void descartarCuponsAceitos(@Field("myCupomId") String myCupomId,
                           @Field("cpf") String cpf,
                           @Field("status") String status,
                           Callback<CupomResponse> cupomResponseCallback);

    @FormUrlEncoded
    @POST("/prc/prc.php?ctl=mobile&acao=utilizarCupon")
    void utilizarCupon(@Field("cupom") String cupom,
                  Callback<CupomResponse> cupomResponseCallback);

    //@GET("/prc/prc.php?ctl=mobile&acao=sincronizar")
    //sincronizarNFP(@Field("cpf") String cpf,
    //               @Field("senha") String senha,
    //               Callback<CupomResponse>);

    @FormUrlEncoded
    @POST("/prc/prc.php?ctl=mobile&acao=getPlaceByPlaceId")
    void placeByPlaceId(@Field("place") String place,
                   Callback<PlaceResponse> placeResponseCallback);

    @FormUrlEncoded
    @POST("/prc/prc.php?ctl=mobile&acao=updateCustmer")
    void updateCustomer(@Field("cliente") String cliente,
                   Callback<ClienteResponse> clienteResponseCallback);

    @FormUrlEncoded
    @POST("/prc/prc.php?ctl=mobile&acao=updateCustmerSettings")
    void updateCustomerSettings(@Field("cliente") String cliente,
                           Callback<ClienteResponse> clienteResponseCallback);

    @FormUrlEncoded
    @POST("/prc/prc.php?ctl=mobile&acao=updateLocale")
    void updateLocale(@Field("locale") String locale,
                      Callback<ClienteResponse> clienteResponseCallback);

    //
    @FormUrlEncoded
    @POST("/prc/prc.php?ctl=mobile&acao=resetPass")
    void resetPass(@Field("email") String email,
              Callback<ClienteResponse> clienteResponsecuCallback);

    @FormUrlEncoded
    @POST("/prc/prc.php?ctl=mobile&acao=getLocalizacao")
    void getLocalizacao(@Field("customer") String customer,
                   Callback<ClienteResponse> clienteResponseCallback);

}
