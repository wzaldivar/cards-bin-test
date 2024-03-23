package group.etraveli.card.cost.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BinListCountry {
    @JsonProperty("alpha2")
    private String countryCode;

    public BinListCountry() {

    }

    public BinListCountry(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
