package pl.workshop.campaign;

import com.kenshoo.jooq.AbstractDataTable;
import org.jooq.Record;
import org.jooq.TableField;
import org.jooq.impl.SQLDataType;
import pl.workshop.account.AccountTable;

public class CampaignTable extends AbstractDataTable<CampaignTable> {

    public final static CampaignTable INSTANCE = new CampaignTable("campaigns");

    CampaignTable(String tableName) {
        super(tableName);
    }

    CampaignTable(CampaignTable aliased, String alias) {
        super(aliased, alias);
    }

    @Override
    public CampaignTable as(String alias) {
        return new CampaignTable(this, alias);
    }

    public final static TableField<Record, Integer> id = INSTANCE.createPKField("id", SQLDataType.INTEGER.length(11).identity(true));
    public final static TableField<Record, Integer> accountId = INSTANCE.createFKField("account_id", AccountTable.id);
    public final static TableField<Record, String> name = INSTANCE.createField("name", SQLDataType.VARCHAR(20));
    public final static TableField<Record, String> type = INSTANCE.createField("type", SQLDataType.VARCHAR(20));

}
