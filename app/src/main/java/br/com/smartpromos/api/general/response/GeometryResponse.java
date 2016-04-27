package br.com.smartpromos.api.general.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Paulo on 27/04/2016.
 */
public class GeometryResponse {

    @SerializedName("location")
    private LocationResponse location;

    public GeometryResponse(LocationResponse location) {
        this.location = location;
    }

    public LocationResponse getLocation() {
        return location;
    }

    public void setLocation(LocationResponse location) {
        this.location = location;
    }
}
