package pl.workshop.account;

import com.kenshoo.jooq.DataTable;
import com.kenshoo.pl.entity.AbstractEntityType;
import com.kenshoo.pl.entity.EntityField;
import com.kenshoo.pl.entity.converters.EnumAsStringValueConverter;
import org.jooq.Record;


public class AccountEntity extends AbstractEntityType<AccountEntity> {

    private AccountEntity() { super("AccountEntity"); }
    public static final AccountEntity INSTANCE = new AccountEntity();

    @Override
    public DataTable getPrimaryTable() {
        return AccountTable.INSTANCE;
    }


     public final static EntityField<AccountEntity, Integer> ID = INSTANCE.field(AccountTable.id);
     public final static EntityField<AccountEntity, AccountStatus> STATUS = INSTANCE.field(AccountTable.status, new EnumAsStringValueConverter<>(AccountStatus.class));
     public final static EntityField<AccountEntity, String> USER_NAME = INSTANCE.field(AccountTable.user_name);

}
