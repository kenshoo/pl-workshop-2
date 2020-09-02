package pl.workshop.campaign;

import com.kenshoo.pl.entity.*;
import pl.workshop.validators.CampaignNameDoesNotIncludeUserNameValidator;
import pl.workshop.validators.MonthlyBudgetIsGreaterThanDailyBudgetValidator;
import pl.workshop.validators.PositiveDailyBudgetValidator;

import java.util.Collection;

import static com.kenshoo.pl.entity.spi.helpers.CompoundChangesValidatorFactory.buildChangesValidator;
import static java.util.Arrays.asList;


public class CampaignPersistence {

    private final PersistenceLayer<CampaignEntity> pl;
    private final PLContext plContext;

    public CampaignPersistence(PLContext plContext) {
        this.pl = new PersistenceLayer<>(plContext);
        this.plContext = plContext;
    }

    private ChangeFlowConfig.Builder<CampaignEntity> flowBuilder() {
        return ChangeFlowConfigBuilderFactory.newInstance(plContext, CampaignEntity.INSTANCE)
                .withValidator(buildChangesValidator(CampaignEntity.INSTANCE, asList(
                        new PositiveDailyBudgetValidator(),
                        new MonthlyBudgetIsGreaterThanDailyBudgetValidator(),
                        new CampaignNameDoesNotIncludeUserNameValidator()
                )));
    }

    public CreateResult<CampaignEntity, Identifier<CampaignEntity>> create(Collection<CreateEntityCommand<CampaignEntity>> commands) {
        return pl.create(commands, flowBuilder().build());
    }

    public <ID extends Identifier<CampaignEntity>>
    UpdateResult<CampaignEntity, ID> update(Collection<UpdateEntityCommand<CampaignEntity, ID>> commands) {
        throw new UnsupportedOperationException("TODO: use pl and flowBuilder");
    }

}
