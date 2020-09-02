package pl.workshop.validators;

import com.kenshoo.pl.entity.EntityField;
import com.kenshoo.pl.entity.FieldsValueMap;
import com.kenshoo.pl.entity.ValidationError;
import com.kenshoo.pl.entity.spi.FieldsCombinationValidator;
import pl.workshop.campaign.CampaignEntity;

import java.util.stream.Stream;

public class MonthlyBudgetIsGreaterThanDailyBudgetValidator implements FieldsCombinationValidator<CampaignEntity> {

    @Override
    public Stream<EntityField<CampaignEntity, ?>> validatedFields() {
        return Stream.of(CampaignEntity.MONTHLY_BUDGET, CampaignEntity.DAILY_BUDGET);
    }

    @Override
    public ValidationError validate(FieldsValueMap<CampaignEntity> fieldsValueMap) {
        return fieldsValueMap.get(CampaignEntity.MONTHLY_BUDGET) <= fieldsValueMap.get(CampaignEntity.DAILY_BUDGET) ?
                new ValidationError("Monthly budget hould be greater than the daily budget")
                : null;
    }
}
