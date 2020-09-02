package pl.workshop.campaign;

import com.kenshoo.pl.entity.SingleUniqueKeyValue;

public class CampaignId extends SingleUniqueKeyValue<CampaignEntity, Integer> {
    public CampaignId(int value) {
        super(CampaignEntity.ID, value);
    }
}
