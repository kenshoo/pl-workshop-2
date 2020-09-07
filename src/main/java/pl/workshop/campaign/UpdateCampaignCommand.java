package pl.workshop.campaign;

import com.kenshoo.pl.entity.*;
import com.kenshoo.pl.entity.internal.MissingChildrenSupplier;

public class UpdateCampaignCommand extends UpdateEntityCommand<CampaignEntity, CampaignId> {

    public UpdateCampaignCommand(int id) {
        super(CampaignEntity.INSTANCE, new CampaignId(id));
    }

    public <T> UpdateCampaignCommand with(EntityField<CampaignEntity, T> field, T value) {
        super.set(field, value);
        return this;
    }

    public <CHILD extends EntityType<CHILD>> UpdateCampaignCommand withChild(ChangeEntityCommand<CHILD> childCmd) {
        super.addChild(childCmd);
        return this;
    }

    public UpdateCampaignCommand with(MissingChildrenSupplier<? extends EntityType> missingChildrenSupplier) {
        super.add(missingChildrenSupplier);
        return this;
    }

}
