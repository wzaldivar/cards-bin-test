package group.etraveli.card.cost.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

@Entity(name = "iin_country_mapping")
public class IINCountryMapping implements Serializable {
    @Id
    @NotNull(message = "IIN required")
    @NotBlank(message = "Invalid IIN")
    @Size(min = 6, max = 6, message = "Invalid IIN")
    @Digits(integer = 6, fraction = 0, message = "Invalid IIN")
    private String iin;

    @Column(name = "country_code")
    @Size(min = 2, max = 2, message = "Invalid country code")
    private String countryCode;

    public IINCountryMapping() {
    }

    public IINCountryMapping(String iin, String countryCode) {
        this.iin = iin;
        this.countryCode = countryCode;
    }

    public String getIin() {
        return this.iin;
    }

    public void setIin(String iin) {
        this.iin = iin;
    }

    public String getCountryCode() {
        return this.countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
