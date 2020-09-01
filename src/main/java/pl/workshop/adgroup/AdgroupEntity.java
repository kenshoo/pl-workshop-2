package pl.workshop.adgroup;

import com.kenshoo.jooq.DataTable;
import com.kenshoo.pl.entity.AbstractEntityType;
import com.kenshoo.pl.entity.EntityField;


public class AdgroupEntity extends AbstractEntityType<AdgroupEntity> {

    private AdgroupEntity() { super("AdgroupEntity"); }
    public static final AdgroupEntity INSTANCE = new AdgroupEntity();

    @Override
    public DataTable getPrimaryTable() {
        return AdgroupTable.INSTANCE;
    }

    public final static EntityField<AdgroupEntity, Integer> ID = INSTANCE.field(AdgroupTable.id);
    public final static EntityField<AdgroupEntity, Integer> CAMPAIGN_ID = INSTANCE.field(AdgroupTable.campaign_id);
    public final static EntityField<AdgroupEntity, String> NAME = INSTANCE.field(AdgroupTable.name);
    public final static EntityField<AdgroupEntity, Integer> DEFAULT_BID = INSTANCE.field(AdgroupTable.default_bid);

}
