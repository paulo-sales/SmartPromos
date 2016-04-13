package br.com.smartpromos.api.general;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by Dany on 12/04/2016.
 */
public interface SmartRepo {

    ///prc/prc.php?ctl=mobile

    @GET("/prc/prc.php?ctl=mobile&acao=loginCustmerUser")
    loginCliente(@Field("username") String user,
                 @Field("password") String password,
                 Callback<>);

    @GET("/prc/prc.php?ctl=mobile&acao=getListaPlacesBySolicitadosClientes")
    listaPlacesbyEmail(@Field("email") String email,
                Callback<>);

    @GET("/prc/prc.php?ctl=getCouponsByEmail")
    cuponsByEmail(@Field("email") String email,
                  @Field("status") String status,
                  Callback<>);

    @GET("/prc/prc.php?ctl=getCupomByIdMobile")
    cuponsByEmail(@Field("cupomId") String cupomId,
                     Callback<>);

    @GET("/prc/prc.php?ctl=setCupomStatus")
    cupomStatus(@Field("myCupomId") String cupomId,
                @Field("cpf") String cpf,
                Callback<>);

    @GET("descartarCuponsAceitos")
    descartarCuponsAceitos(@Field("myCupomId"),
                           @Field("cpf"),
                           @Field("status"),
                           Callback<>);

    @GET("utilizarCupon")
    utilizarCupon(@Field("codigo"),
                  @Field("cupom"),
                  @Field("usuario"),
                  Callback<>);

    @GET("sincronizar")
    sincronizarNFP(@Field("cpf") String cpf,
                   @Field("senha") String senha,
                   Callback<>);

    @GET("getPlaceByPlaceId")
    placeByPlaceId(@Field("place") String place,
                   Callback<>);

    @GET("updateCustmer")
    updateCustomer(@Field("cliente") String cliente,
                   Callback<>);

    @GET("updateCustmerSettings")
    updateCustomerSettings(@Field("cliente") String cliente,
                           Callback<>);

    @GET("updateLocale")
    updateLocale(@Field("locale") String locale,
                 Callback<>);

    @GET("resetPass")
    resetPass(@Field("email") String email,
              Callback<>);

    @GET("getLocalizacao")
    getLocalizacao(@Field("customer") String customer
                   Callback<>);


}
