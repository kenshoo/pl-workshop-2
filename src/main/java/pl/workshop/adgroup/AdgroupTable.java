package pl.workshop.adgroup;

import com.kenshoo.jooq.AbstractDataTable;
import org.jooq.Record;
import org.jooq.TableField;
import org.jooq.impl.SQLDataType;
import pl.workshop.account.AccountTable;
import pl.workshop.campaign.CampaignTable;

public class AdgroupTable extends AbstractDataTable<AdgroupTable> {

    public final static AdgroupTable INSTANCE = new AdgroupTable("adgroups");

    AdgroupTable(String tableName) { super(tableName); }
    AdgroupTable(AdgroupTable aliased, String alias) { super(aliased, alias); }
    @Override public AdgroupTable as(String alias) { return new AdgroupTable(this, alias); }


    public final static TableField<Record, Integer> id = INSTANCE.createPKField("id", SQLDataType.INTEGER);
    public final static TableField<Record, Integer> campaign_id = INSTANCE.createFKField("campaign_id", CampaignTable.id);
    public final static TableField<Record, String> name = INSTANCE.createField("name", SQLDataType.VARCHAR(20));
    public final static TableField<Record, Integer> default_bid = INSTANCE.createField("default_bid", SQLDataType.INTEGER);

}
