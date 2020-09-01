package pl.workshop.account;

import com.kenshoo.jooq.DataTable;
import com.kenshoo.pl.entity.AbstractEntityType;


public class AccountEntity extends AbstractEntityType<AccountEntity> {

    private AccountEntity() { super("AccountEntity"); }
    public static final AccountEntity INSTANCE = new AccountEntity();

    @Override
    public DataTable getPrimaryTable() {
        return AccountTable.INSTANCE;
    }

    //
    // TODO: add more fields in here.
    //
    // public final static EntityField<AccountEntity, ...> ID = ...
    // public final static EntityField<AccountEntity, AccountStatus> STATUS = ... // TODO: define field type as enum by using EnumAsStringValueConverter
    // public final static EntityField<CampaignEntity, ...> REPLACE_ME_1 = ...;

}
