package pl.workshop.validators;

import com.kenshoo.pl.entity.EntityField;
import com.kenshoo.pl.entity.ValidationError;
import com.kenshoo.pl.entity.spi.FieldValidator;
import pl.workshop.campaign.CampaignEntity;

public class PositiveDailyBudgetValidator implements FieldValidator<CampaignEntity, Integer> {

    @Override
    public EntityField<CampaignEntity, Integer> validatedField() {
        return CampaignEntity.DAILY_BUDGET;
    }

    @Override
    public ValidationError validate(Integer fieldValue) {
        return fieldValue <= 0 ?
                new ValidationError("Daily budget should be greater than 0")
                : null;
    }
}
