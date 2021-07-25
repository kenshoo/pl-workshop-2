package pl.workshop.account;

import com.kenshoo.jooq.AbstractDataTable;

public class AccountTable extends AbstractDataTable<AccountTable> {

    public final static AccountTable INSTANCE = new AccountTable("accounts");

    AccountTable(String tableName) { super(tableName); }
    AccountTable(AccountTable aliased, String alias) { super(aliased, alias); }
    @Override public AccountTable as(String alias) { return new AccountTable(this, alias); }


    //
    // TODO: define fields.
    //
    // Field 'id' should be defined as a Primary Key (PK) by using method createPKField rather than createField.
    //
    // public final static TableField<Record, ...> id = ...
    // public final static TableField<Record, ...> user_name = ...
    // public final static TableField<Record, ...> status = ...

}
