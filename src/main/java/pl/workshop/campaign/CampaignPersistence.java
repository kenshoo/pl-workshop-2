package pl.workshop.campaign;

import com.kenshoo.pl.entity.*;
import java.util.Collection;


public class CampaignPersistence {

    private final PersistenceLayer<CampaignEntity> pl;
    private final PLContext plContext;

    public CampaignPersistence(PLContext plContext) {
        this.pl = new PersistenceLayer<>(plContext);
        this.plContext = plContext;
    }

    private ChangeFlowConfig.Builder<CampaignEntity> flowBuilder() {
        throw new UnsupportedOperationException("TODO: use ChangeFlowConfigBuilderFactory");
    }

    public CreateResult<CampaignEntity, Identifier<CampaignEntity>> create(Collection<CreateEntityCommand<CampaignEntity>> commands) {
        throw new UnsupportedOperationException("TODO: use pl and flowBuilder");
    }

    public <ID extends Identifier<CampaignEntity>>
    UpdateResult<CampaignEntity, ID> update(Collection<UpdateEntityCommand<CampaignEntity, ID>> commands) {
        throw new UnsupportedOperationException("TODO: use pl and flowBuilder");
    }

}
