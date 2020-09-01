package pl.workshop.campaign;

import com.kenshoo.jooq.AbstractDataTable;
import org.jooq.Record;
import org.jooq.TableField;
import org.jooq.impl.SQLDataType;

public class CampaignBudgetTable extends AbstractDataTable<CampaignBudgetTable> {

    public final static CampaignBudgetTable INSTANCE = new CampaignBudgetTable("campaign_budgets");

    CampaignBudgetTable(String tableName) { super(tableName); }
    CampaignBudgetTable(CampaignBudgetTable aliased, String alias) { super(aliased, alias); }
    @Override public CampaignBudgetTable as(String alias) { return new CampaignBudgetTable(this, alias); }


    public final static TableField<Record, Integer> campaign_id = INSTANCE.createPKAndFKField("campaign_id", SQLDataType.INTEGER, CampaignTable.id);
    public final static TableField<Record, Integer> daily_budget = INSTANCE.createField("daily_budget", SQLDataType.INTEGER);
    public final static TableField<Record, Integer> monthly_budget = INSTANCE.createField("monthly_budget", SQLDataType.INTEGER);

}
