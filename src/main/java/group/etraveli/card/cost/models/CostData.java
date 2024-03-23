package group.etraveli.card.cost.models;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class CostData {
    @NotNull(message = "Cost required")
    protected BigDecimal cost;

    public CostData() {
    }

    public CostData(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getCost() {
        return this.cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }
}
