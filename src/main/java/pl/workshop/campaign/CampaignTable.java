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


    //
    // TODO: define table fields as static members.
    //
    // Field `id` should be defined as a Primary Key (PK) and auto incrementing.
    // PK field can be defined by calling createPKField instead of createField.
    // Auto increment is defined by calling `identity(true)` on the SQLDataType of the field.
    //
    //
    public final static TableField<Record, Integer> id = INSTANCE.createPKField("id", SQLDataType.INTEGER.identity(true));
    public final static TableField<Record, Integer> account_id = INSTANCE.createFKField("account_id", AccountTable.id);
    public final static TableField<Record, String> name = INSTANCE.createField("name", SQLDataType.VARCHAR(20));
    public final static TableField<Record, String> type = INSTANCE.createField("type", SQLDataType.VARCHAR(20));

}
