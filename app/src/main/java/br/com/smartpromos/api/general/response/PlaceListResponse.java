package br.com.smartpromos.api.general.response;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;

/**
 * Created by Paulo on 27/04/2016.
 */
public class PlaceListResponse {

    @SerializedName("geometry")
    private GeometryResponse geometry;

    @SerializedName("icon")
    private String icon;

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("place_id")
    private String placeid;

    @SerializedName("rating")
    private String rating;

    @SerializedName("reference")
    private String reference;

    @SerializedName("scope")
    private String scope;

    @SerializedName("types")
    private JSONArray types;

    @SerializedName("vicinity")
    private String vicinity;

    public PlaceListResponse(GeometryResponse geometry, String icon, String id, String name, String placeid, String rating, String reference, String scope, JSONArray types, String vicinity) {
        this.geometry = geometry;
        this.icon = icon;
        this.id = id;
        this.name = name;
        this.placeid = placeid;
        this.rating = rating;
        this.reference = reference;
        this.scope = scope;
        this.types = types;
        this.vicinity = vicinity;
    }

    public GeometryResponse getGeometry() {
        return geometry;
    }

    public void setGeometry(GeometryResponse geometry) {
        this.geometry = geometry;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlaceid() {
        return placeid;
    }

    public void setPlaceid(String placeid) {
        this.placeid = placeid;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public JSONArray getTypes() {
        return types;
    }

    public void setTypes(JSONArray types) {
        this.types = types;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }
}
