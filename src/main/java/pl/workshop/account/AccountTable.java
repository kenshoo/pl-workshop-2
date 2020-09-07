package pl.workshop.account;

import com.kenshoo.jooq.AbstractDataTable;
import com.kenshoo.pl.entity.converters.EnumAsStringValueConverter;
import org.jooq.Record;
import org.jooq.TableField;
import org.jooq.impl.SQLDataType;
import pl.workshop.campaign.CampaignType;

public class AccountTable extends AbstractDataTable<AccountTable> {

    public final static AccountTable INSTANCE = new AccountTable("accounts");

    AccountTable(String tableName) { super(tableName); }
    AccountTable(AccountTable aliased, String alias) { super(aliased, alias); }
    @Override public AccountTable as(String alias) { return new AccountTable(this, alias); }

     public final static TableField<Record, Integer > id = INSTANCE.createPKField("id", SQLDataType.INTEGER.length(11));
     public final static TableField<Record, String > user_name = INSTANCE.createField("user_name",SQLDataType.VARCHAR(20));
     public final static TableField<Record, String> status =  INSTANCE.createField("status",SQLDataType.VARCHAR(20));

}
