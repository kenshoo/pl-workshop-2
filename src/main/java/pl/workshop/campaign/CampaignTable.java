package pl.workshop.campaign;

import com.kenshoo.jooq.AbstractDataTable;
import org.jooq.Record;
import org.jooq.TableField;
import org.jooq.impl.SQLDataType;

public class CampaignTable extends AbstractDataTable<CampaignTable> {

    public final static CampaignTable INSTANCE = new CampaignTable("campaigns");

    CampaignTable(String tableName) { super(tableName); }
    CampaignTable(CampaignTable aliased, String alias) { super(aliased, alias); }
    @Override public CampaignTable as(String alias) { return new CampaignTable(this, alias); }


    //
    // TODO: add more fields in here...
    //
    public final static TableField<Record, String> name = INSTANCE.createField("name", SQLDataType.VARCHAR(20));
    // public final static TableField<Record, ...> replace_me_1 = ...
    // public final static TableField<Record, ...> replace_me_2 = ...
    // public final static TableField<Record, ...> replace_me_3 = ...

}
