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


    //
    // TODO: define fields.
    //
    // Field campaign_id should be defined as a Foreign Key (FK) to the campaign table
    // and Primary Key (PK) to the budget table..
    // Do this by using method createPKAndFKField instead of using createField.
    //
    // public final static TableField<Record, ...> campaign_id = ...
    // public final static TableField<Record, ...> daily_budget = ...
    // public final static TableField<Record, ...> monthly_budget = ...

}
