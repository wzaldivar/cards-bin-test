package group.etraveli.card.cost.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity(name = "default_cost")
public class DefaultCost {
    @Id
    @NotNull
    private int id;
    private BigDecimal cost;

    public DefaultCost() {
    }

    public DefaultCost(int id, BigDecimal cost) {
        this.id = id;
        this.cost = cost;
    }

    public int getId() {
        return this.id;
    }

    public BigDecimal getCost() {
        return this.cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }
}
