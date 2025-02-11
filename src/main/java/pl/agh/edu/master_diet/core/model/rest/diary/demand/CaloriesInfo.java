package pl.agh.edu.master_diet.core.model.rest.diary.demand;

import lombok.Getter;

@Getter
public class CaloriesInfo extends AbstractNutrientInfo {

    @Override
    public String getDescription() {
        return "Calories";
    }

    @Override
    public String getIdentifier() {
        return "calories";
    }
}
