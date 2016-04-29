package br.com.smartpromos.api.general.response;

import android.location.Location;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Geometry {

    @SerializedName("location")
    @Expose
    private LocationResponse location;
    @SerializedName("viewport")
    @Expose
    private Viewport viewport;

    /**
     *
     * @return
     * The location
     */
    public LocationResponse getLocation() {
        return location;
    }

    /**
     *
     * @param location
     * The location
     */
    public void setLocation(LocationResponse location) {
        this.location = location;
    }

    /**
     *
     * @return
     * The viewport
     */
    public Viewport getViewport() {
        return viewport;
    }

    /**
     *
     * @param viewport
     * The viewport
     */
    public void setViewport(Viewport viewport) {
        this.viewport = viewport;
    }

}