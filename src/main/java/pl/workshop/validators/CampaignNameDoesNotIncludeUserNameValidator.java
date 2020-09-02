package pl.workshop.validators;

import com.kenshoo.pl.entity.CurrentEntityState;
import com.kenshoo.pl.entity.EntityField;
import com.kenshoo.pl.entity.ValidationError;
import com.kenshoo.pl.entity.spi.FieldComplexValidator;
import pl.workshop.account.AccountEntity;
import pl.workshop.campaign.CampaignEntity;

import java.util.stream.Stream;

public class CampaignNameDoesNotIncludeUserNameValidator implements FieldComplexValidator<CampaignEntity, String> {

    @Override
    public EntityField<CampaignEntity, String> validatedField() {
        return CampaignEntity.NAME;
    }

    @Override
    public ValidationError validate(String fieldValue, CurrentEntityState currentState) {
        return fieldValue.contains(currentState.get(AccountEntity.USER_NAME).toLowerCase()) ?
                new ValidationError("Campaign name shouldn't include the account user name")
                : null;
    }

    @Override
    public Stream<EntityField<?, ?>> fetchFields() {
        return Stream.of(CampaignEntity.NAME, AccountEntity.USER_NAME);
    }
}
