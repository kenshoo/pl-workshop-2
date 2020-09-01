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

    public final static EntityField<CampaignEntity, Integer> ID = INSTANCE.field(CampaignTable.id);
    public final static EntityField<CampaignEntity, String> NAME = INSTANCE.field(CampaignTable.name);
    public final static EntityField<CampaignEntity, CampaignType> TYPE = INSTANCE.field(CampaignTable.type, new EnumAsStringValueConverter<>(CampaignType.class));
    public final static EntityField<CampaignEntity, Integer> ACCOUNT_ID = INSTANCE.field(CampaignTable.account_id);
    public final static EntityField<CampaignEntity, Integer> DAILY_BUDGET = INSTANCE.field(CampaignBudgetTable.daily_budget);
    public final static EntityField<CampaignEntity, Integer> MONTHLY_BUDGET = INSTANCE.field(CampaignBudgetTable.monthly_budget);

}
