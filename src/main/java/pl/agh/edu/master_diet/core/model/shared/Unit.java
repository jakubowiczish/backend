package pl.agh.edu.master_diet.core.model.shared;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.agh.edu.master_diet.exception.UnitSymbolNotFoundException;

@Getter
@AllArgsConstructor
public enum Unit {
    GRAMS("g"),
    MILLILITERS("ml"),
    MINUTES("min");

    private final String symbol;

    public static Unit fromSymbol(String symbol) {
        symbol = symbol.trim().toLowerCase();
        for (Unit unit : Unit.values()) {
            if (unit.getSymbol().equals(symbol)) {
                return unit;
            }
        }
        throw new UnitSymbolNotFoundException(symbol);
    }

    @JsonValue
    public String getSymbol() {
        return this.symbol;
    }
}
