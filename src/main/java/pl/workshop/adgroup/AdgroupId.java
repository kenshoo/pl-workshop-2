package pl.workshop.adgroup;

import com.kenshoo.pl.entity.SingleUniqueKeyValue;

public class AdgroupId extends SingleUniqueKeyValue<AdgroupEntity, Integer> {
    public AdgroupId(int value) {
        super(AdgroupEntity.ID, value);
    }
}
