package br.com.smartpromos.api.general;

import br.com.smartpromos.api.general.response.ClienteResponse;
import br.com.smartpromos.api.general.response.CupomResponse;
import br.com.smartpromos.api.general.response.ListPlacesResponse;
import br.com.smartpromos.api.general.response.ListaCuponsResponse;
import br.com.smartpromos.api.general.response.ListaCuponsSolicitacao;
import br.com.smartpromos.api.general.response.LocalizacaoResponse;
import br.com.smartpromos.api.general.response.MensagemResponse;
import br.com.smartpromos.api.general.response.PlaceResponse;
import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

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
    @POST("/prc/prc.php?ctl=mobile&acao=checkClienteByFacebook")
    void checkClienteByFacebook(@Field("email") String email,
                      Callback<ClienteResponse> clienteResponseCallback);

    @FormUrlEncoded
    @POST("/prc/prc.php?ctl=mobile&acao=getListaPlacesBySolicitadosClientes")
    void listaPlacesbyEmail(@Field("email") String email,
                       Callback<ListaCuponsSolicitacao> listaCuponsSolicitacaoCallback);


    @FormUrlEncoded
    @POST("/prc/prc.php?ctl=mobile&acao=getCouponsByEmail")
    void cuponsByEmailAndStatus(@Field("email") String email,
                       @Field("status") int status,
                       Callback<ListaCuponsResponse> listaCuponsResponseCallback);

    @FormUrlEncoded
    @POST("/prc/prc.php?ctl=mobile&acao=getCupomByIdMobile")
    void cuponsById(@Field("cupomId") String cupomId,
                  Callback<CupomResponse> cupomResponseCallback);

    @FormUrlEncoded
    @POST("/prc/prc.php?ctl=mobile&acao=setCupomStatus")
    void cupomStatus(@Field("myCupomId") long cupomId,
                @Field("cpf") long cpf,
                 @Field("status") int status,
                Callback<MensagemResponse> mensagem);

    @FormUrlEncoded
    @POST("/prc/prc.php?ctl=mobile&acao=descartarCuponsAceitos")
    void descartarCuponsAceitos(@Field("myCupomId") long myCupomId,
                           @Field("cpf") long cpf,
                           @Field("status") int status,
                           Callback<MensagemResponse> mensagem);

    @FormUrlEncoded
    @POST("/prc/prc.php?ctl=mobile&acao=utilizarCupon")
    void utilizarCupon(@Field("codigo") String codigo,
                       @Field("cupom") long cupom,
                       @Field("usuario") long usuario,
                  Callback<MensagemResponse> mensagem);

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
                           Callback<MensagemResponse> mensagemResponse);

    @FormUrlEncoded
    @POST("/prc/prc.php?ctl=mobile&acao=updateLocale")
    void updateLocale(@Field("locale") String locale,
                      Callback<LocalizacaoResponse> localizacaoResponseCallback);

    //
    @FormUrlEncoded
    @POST("/prc/prc.php?ctl=mobile&acao=resetPass")
    void resetPass(@Field("email") String email,
              Callback<MensagemResponse> mensagemResponsecuCallback);

    @FormUrlEncoded
    @POST("/prc/prc.php?ctl=mobile&acao=getLocalizacao")
    void getLocalizacao(@Field("customer") long customer,
                   Callback<LocalizacaoResponse> localizacaoResponse);

    @FormUrlEncoded
    @POST("/prc/prc.php?ctl=mobile&acao=getListaPlacesBySolicitadosClientes")
    void getRequests(@Field("email") String email,
                            Callback<ListaCuponsSolicitacao> lista);

    @FormUrlEncoded
    @POST("/prc/prc.php?ctl=mobile&acao=insertNearbyPlaces")
    void insertNearbyPlaces(@Field("place") String place,
                         Callback<MensagemResponse> mensagem);

    @FormUrlEncoded
    @POST("/prc/prc.php?ctl=mobile&acao=solicitar")
    void sendRequestSale(@Field("customer") long customer,
                         @Field("placeid") String placeid,
                        Callback<MensagemResponse> mensagem);

    @GET("/json")
    void getPlacesByCustomerLocation(
            @Query("location") String location,
            @Query("radius") int radius,
            @Query("type") String type,
            @Query("key") String gmapskey,
            @Query("sensor") boolean sensor,
            Callback<ListPlacesResponse> places);

    /*@FormUrlEncoded
    @POST("/json?")
    void getPlaceDetails(
            @Field("placeid") String placeid,
            @Field("key") int gmapskey);*/

}
