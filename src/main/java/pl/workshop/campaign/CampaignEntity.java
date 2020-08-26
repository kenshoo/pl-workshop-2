package pl.workshop.campaign;

import com.kenshoo.jooq.DataTable;
import com.kenshoo.pl.entity.AbstractEntityType;
import com.kenshoo.pl.entity.EntityField;
import com.kenshoo.pl.entity.converters.EnumAsStringValueConverter;


public class CampaignEntity extends AbstractEntityType<CampaignEntity> {

    private CampaignEntity() { super("CampaignEntity"); }
    public static final CampaignEntity INSTANCE = new CampaignEntity();

    @Override
    public DataTable getPrimaryTable() {
        return CampaignTable.INSTANCE;
    }

    //
    // TODO: add more fields in here.
    //
    public final static EntityField<CampaignEntity, String> NAME = INSTANCE.field(CampaignTable.name);
    public final static EntityField<CampaignEntity, CampaignType> TYPE = null; // TODO: define field type as enum by using EnumAsStringValueConverter
    // public final static EntityField<CampaignEntity, ...> REPLACE_ME_1 = ...;
    // public final static EntityField<CampaignEntity, ...> REPLACE_ME_2 = ...;


}
