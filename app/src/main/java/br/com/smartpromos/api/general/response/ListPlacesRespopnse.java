package br.com.smartpromos.api.general.response;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by Paulo on 27/04/2016.
 */
public class ListPlacesRespopnse {

    @SerializedName("html_attributions")
    private JSONArray html_attributions;

    @SerializedName("next_page_token")
    private String next_page_token;

    @SerializedName("results")
    private List<PlaceListResponse> results;

    @SerializedName("results")
    private String status;

    public ListPlacesRespopnse(JSONArray html_attributions, String next_page_token, List<PlaceListResponse> results, String status) {
        this.html_attributions = html_attributions;
        this.next_page_token = next_page_token;
        this.results = results;
        this.status = status;
    }

    public JSONArray getHtml_attributions() {
        return html_attributions;
    }

    public void setHtml_attributions(JSONArray html_attributions) {
        this.html_attributions = html_attributions;
    }

    public String getNext_page_token() {
        return next_page_token;
    }

    public void setNext_page_token(String next_page_token) {
        this.next_page_token = next_page_token;
    }

    public List<PlaceListResponse> getResults() {
        return results;
    }

    public void setResults(List<PlaceListResponse> results) {
        this.results = results;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
