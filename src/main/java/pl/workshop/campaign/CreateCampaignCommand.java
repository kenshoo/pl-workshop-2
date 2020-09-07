package pl.workshop.campaign;

import com.kenshoo.pl.entity.ChangeEntityCommand;
import com.kenshoo.pl.entity.CreateEntityCommand;
import com.kenshoo.pl.entity.EntityField;
import com.kenshoo.pl.entity.EntityType;

public class CreateCampaignCommand extends CreateEntityCommand<CampaignEntity> {

    public CreateCampaignCommand() {
        super(CampaignEntity.INSTANCE);
    }

    public <T> CreateCampaignCommand with(EntityField<CampaignEntity, T> field, T value) {
        super.set(field, value);
        return this;
    }

    public <CHILD extends EntityType<CHILD>> CreateCampaignCommand withChild(ChangeEntityCommand<CHILD> childCmd) {
        super.addChild(childCmd);
        return this;
    }

}
