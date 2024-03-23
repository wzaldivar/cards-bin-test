package group.etraveli.card.cost.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity(name = "country_costs")
public class CountryCost implements Serializable {

    @NotNull(message = "Country code required")
    @NotBlank(message = "Country code required")
    @Size(min = 2, max = 2, message = "Invalid country code")
    @Id
    @Column(name = "country_code")
    @JsonProperty("country")
    private String countryCode;
    @NotNull
    private BigDecimal cost;

    public BigDecimal getCost() {
        return this.cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public CountryCost() {
    }

    public CountryCost(String countryCode, BigDecimal cost) {
        setCountryCode(countryCode);
        this.cost = cost;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode.toUpperCase();
    }
}
