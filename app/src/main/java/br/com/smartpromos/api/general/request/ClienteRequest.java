package br.com.smartpromos.api.general.request;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Leandro on 12/04/2016.
 */
public class ClienteRequest {

    @SerializedName("cpf")
    private long doc_id;

    @SerializedName("nome")
    private String first_name;

    @SerializedName("sobrenome")
    private String last_name;

    @SerializedName("sexo")
    private int gender;

    @SerializedName("diaAniversario")
    private int birthday;

    @SerializedName("mesAniversario")
    private int birthday_month;

    @SerializedName("anoAniversario")
    private int birthday_yaer;

    @SerializedName("raioOferta")
    private int sale_radius;

    @SerializedName("receberOfertas")
    private int get_offers;

    @SerializedName("manterLogado")
    private int stay_logged_in = 1;

    @SerializedName("email")
    private String email;

    @SerializedName("senha")
    private String password;

    @SerializedName("telefone")
    private String phone;

    @SerializedName("msg")
    private MensagemRequest mensagem;


    public ClienteRequest(long doc_id, String first_name, String last_name, int gender, int birthday, int birthday_month, int birthday_yaer, int sale_radius, int get_offers, int stay_logged_in, String email, String password, String phone) {
        this.doc_id = doc_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.gender = gender;
        this.birthday = birthday;
        this.birthday_month = birthday_month;
        this.birthday_yaer = birthday_yaer;
        this.sale_radius = sale_radius;
        this.get_offers = get_offers;
        this.stay_logged_in = stay_logged_in;
        this.email = email;
        this.password = password;
        this.phone = phone;
    }

    public MensagemRequest getMensagem() {
        return mensagem;
    }

    public void setMensagem(MensagemRequest mensagem) {
        this.mensagem = mensagem;
    }



    public long getDoc_id() {
        return doc_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public int getGender() {
        return gender;
    }

    public int getBirthday() {
        return birthday;
    }

    public int getBirthday_month() {
        return birthday_month;
    }

    public int getBirthday_yaer() {
        return birthday_yaer;
    }

    public int getSale_radius() {
        return sale_radius;
    }

    public int getGet_offers() {
        return get_offers;
    }

    public int getStay_logged_in() {
        return stay_logged_in;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public void setDoc_id(long doc_id) {
        this.doc_id = doc_id;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setBirthday(int birthday) {
        this.birthday = birthday;
    }

    public void setBirthday_month(int birthday_month) {
        this.birthday_month = birthday_month;
    }

    public void setBirthday_yaer(int birthday_yaer) {
        this.birthday_yaer = birthday_yaer;
    }

    public void setSale_radius(int sale_radius) {
        this.sale_radius = sale_radius;
    }

    public void setGet_offers(int get_offers) {
        this.get_offers = get_offers;
    }

    public void setStay_logged_in(int stay_logged_in) {
        this.stay_logged_in = stay_logged_in;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


}
