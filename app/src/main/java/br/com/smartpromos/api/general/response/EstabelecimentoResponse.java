package br.com.smartpromos.api.general.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Leandro on 12/04/2016.
 */
public class EstabelecimentoResponse {

    @SerializedName("id")
    private int id_establishment;

    @SerializedName("bairro")
    private String neighborwood;

    @SerializedName("cidade")
    private String city;

    @SerializedName("estado")
    private String state;

    @SerializedName("cep")
    private String zip_code;

    @SerializedName("cnae")
    private String cnae;

    @SerializedName("cnpj")
    private String register_number;

    @SerializedName("diaInscricao")
    private int day_inscription;

    @SerializedName("mesInscricao")
    private int month_inscription;

    @SerializedName("anoInscricao")
    private int year_inscription;

    @SerializedName("email")
    private String email;

    @SerializedName("naturezaJuridica")
    private String legal_nature;

    @SerializedName("nomeFantasia")
    private String fantasy_name;

    @SerializedName("numero")
    private String number_address;

    @SerializedName("placeID")
    private String place_id;

    @SerializedName("razaoSocial")
    private String company_name;

    @SerializedName("rua")
    private String street;

    @SerializedName("status")
    private int status;

    @SerializedName("telefone")
    private String phone;

    @SerializedName("celular")
    private String cellphone;

    @SerializedName("complemento")
    private String complement;

    @SerializedName("dataAdesao")
    private String accession_date;

    public EstabelecimentoResponse(int id_establishment, String neighborwood, String city, String state, String zip_code, String cnae, String register_number, int day_inscription, int month_inscription, int year_inscription, String email, String legal_nature, String fantasy_name, String number_address, String place_id, String company_name, String street, int status, String phone, String cellphone, String complement, String accession_date) {
        this.id_establishment = id_establishment;
        this.neighborwood = neighborwood;
        this.city = city;
        this.state = state;
        this.zip_code = zip_code;
        this.cnae = cnae;
        this.register_number = register_number;
        this.day_inscription = day_inscription;
        this.month_inscription = month_inscription;
        this.year_inscription = year_inscription;
        this.email = email;
        this.legal_nature = legal_nature;
        this.fantasy_name = fantasy_name;
        this.number_address = number_address;
        this.place_id = place_id;
        this.company_name = company_name;
        this.street = street;
        this.status = status;
        this.phone = phone;
        this.cellphone = cellphone;
        this.complement = complement;
        this.accession_date = accession_date;
    }

    public int getId_establishment() {
        return id_establishment;
    }

    public String getNeighborwood() {
        return neighborwood;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZip_code() {
        return zip_code;
    }

    public String getCnae() {
        return cnae;
    }

    public String getRegister_number() {
        return register_number;
    }

    public int getDay_inscription() {
        return day_inscription;
    }

    public int getMonth_inscription() {
        return month_inscription;
    }

    public int getYear_inscription() {
        return year_inscription;
    }

    public String getEmail() {
        return email;
    }

    public String getLegal_nature() {
        return legal_nature;
    }

    public String getFantasy_name() {
        return fantasy_name;
    }

    public String getNumber_address() {
        return number_address;
    }

    public String getPlace_id() {
        return place_id;
    }

    public String getCompany_name() {
        return company_name;
    }

    public String getStreet() {
        return street;
    }

    public int getStatus() {
        return status;
    }

    public String getPhone() {
        return phone;
    }

    public String getCellphone() {
        return cellphone;
    }

    public String getComplement() {
        return complement;
    }

    public String getAccession_date() {
        return accession_date;
    }

    public void setId_establishment(int id_establishment) {
        this.id_establishment = id_establishment;
    }

    public void setNeighborwood(String neighborwood) {
        this.neighborwood = neighborwood;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setZip_code(String zip_code) {
        this.zip_code = zip_code;
    }

    public void setCnae(String cnae) {
        this.cnae = cnae;
    }

    public void setRegister_number(String register_number) {
        this.register_number = register_number;
    }

    public void setDay_inscription(int day_inscription) {
        this.day_inscription = day_inscription;
    }

    public void setMonth_inscription(int month_inscription) {
        this.month_inscription = month_inscription;
    }

    public void setYear_inscription(int year_inscription) {
        this.year_inscription = year_inscription;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLegal_nature(String legal_nature) {
        this.legal_nature = legal_nature;
    }

    public void setFantasy_name(String fantasy_name) {
        this.fantasy_name = fantasy_name;
    }

    public void setNumber_address(String number_address) {
        this.number_address = number_address;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public void setAccession_date(String accession_date) {
        this.accession_date = accession_date;
    }
}
