package group.etraveli.card.cost.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CardData {
    @NotNull(message = "Card number required")
    @Size(min = 6, max = 19, message = "Invalid card number")
    @Digits(integer = 19, fraction = 0, message = "Invalid card number")
    @JsonProperty("card_number")
    private String pan;
    private String iin;

    CardData() {
    }

    CardData(String cardNumber) {
        setPan(cardNumber);
    }

    public void setPan(String pan) {
        this.pan = pan;
        this.iin = this.pan.substring(0, 6);
    }

    public String getIIN() {
        return iin;
    }
}
