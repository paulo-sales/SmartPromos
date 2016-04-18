package br.com.smartpromos.api.general.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Leandro on 12/04/2016.
 */
public class PlaceResponse {

    @SerializedName("id_Place")
    private long id_place;

    @SerializedName("place_id")
    private String place_id;

    @SerializedName("endereco")
    private String adr_address;

    @SerializedName("enderecoFormatado")
    private String formatted_address;

    @SerializedName("telefone")
    private String formatted_phone_number;

    @SerializedName("icon")
    private String icon;

    @SerializedName("InterTelefone")
    private String international_phone_number;

    @SerializedName("nome")
    private String name;

    public PlaceResponse(long id_place, String place_id, String adr_address, String formatted_address, String formatted_phone_number, String icon, String international_phone_number, String name) {
        this.id_place = id_place;
        this.place_id = place_id;
        this.adr_address = adr_address;
        this.formatted_address = formatted_address;
        this.formatted_phone_number = formatted_phone_number;
        this.icon = icon;
        this.international_phone_number = international_phone_number;
        this.name = name;
    }

    public long getId_place() {
        return id_place;
    }

    public void setId_place(long id_place) {
        this.id_place = id_place;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public String getAdr_address() {
        return adr_address;
    }

    public void setAdr_address(String adr_address) {
        this.adr_address = adr_address;
    }

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    public String getFormatted_phone_number() {
        return formatted_phone_number;
    }

    public void setFormatted_phone_number(String formatted_phone_number) {
        this.formatted_phone_number = formatted_phone_number;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getInternational_phone_number() {
        return international_phone_number;
    }

    public void setInternational_phone_number(String international_phone_number) {
        this.international_phone_number = international_phone_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
