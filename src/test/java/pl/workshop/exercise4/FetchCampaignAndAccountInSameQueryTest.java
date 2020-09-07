package pl.workshop.exercise4;

import com.kenshoo.jooq.DataTableUtils;
import com.kenshoo.pl.entity.CurrentEntityState;
import com.kenshoo.pl.entity.PLContext;
import org.jooq.DSLContext;
import org.junit.Before;
import org.junit.Test;
import pl.workshop.account.AccountEntity;
import pl.workshop.account.AccountTable;
import pl.workshop.campaign.*;
import pl.workshop.database.JooqProvider;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static pl.workshop.utils.SyntacticSugars.first;
import static pl.workshop.matchers.StreamMatcher.contains;


public class FetchCampaignAndAccountInSameQueryTest {

    private final CampaignTable CAMPAIGNS = CampaignTable.INSTANCE;
    private final AccountTable ACCOUNTS = AccountTable.INSTANCE;
    private final DSLContext jooq = JooqProvider.create();
    private final PLContext plContext = new PLContext.Builder(jooq).withFeaturePredicate(any -> true).build();

    @Before
    public void createAccountTable() {
        DataTableUtils.createTable(jooq, ACCOUNTS);

        final Object[][] DATA = {
                // +------+--------------+-----------+
                // | id   | user_name    | status    |
                // +------+--------------+-----------+
                {1, "Joker", "ACTIVE"}
        };

        DataTableUtils.populateTable(jooq, ACCOUNTS, DATA);
    }

    @Before
    public void createCampaignTable() {
        DataTableUtils.createTable(jooq, CAMPAIGNS);

        final Object[][] DATA = {
                // +------+--------------+-----------+----------+
                // | id   | account_id   | name      |  type    |
                // +------+--------------+-----------+----------+
                {1, 1, "shoes", "SEARCH"},
                {2, 1, "shirts", "SEARCH"},
        };

        DataTableUtils.populateTable(jooq, CAMPAIGNS, DATA);
    }

    @Test
    public void testFetchingCampaignNameAndAccountUserName() {

        // TODO: [1] go to class AccountTable and define the table fields.
        // TODO: [2] go to class AccountEntity and define the entity fields.
        // TODO: [3] go to class CampaignTable and modify field `account_id` to be a foreign key (FK) to the accounts by using 'createFKField'.

        List<CurrentEntityState> campaigns = plContext
                .select(CampaignEntity.NAME, CampaignEntity.ID, AccountEntity.USER_NAME)
                .from(CampaignEntity.INSTANCE)
                .where(CampaignEntity.ID.eq(1))
                .fetch();

        assertThat(first(campaigns).get(CampaignEntity.NAME), is("shoes"));
        // TODO: [3] uncomment this lines
        assertThat(first(campaigns).get(AccountEntity.USER_NAME), is("Joker"));
    }


    @Test
    public void testFetchingAccountWithManyCampaigns() {

        List<CurrentEntityState> accounts = plContext
                .select(CampaignEntity.ID, CampaignEntity.NAME, AccountEntity.USER_NAME)
                        .from(AccountEntity.INSTANCE)
                        .where(AccountEntity.ID.eq(1))
                        .fetch();

        // TODO: [2] uncomment this line
         assertThat(first(accounts).get(AccountEntity.USER_NAME), is("Joker"));
        assertThat(first(accounts).getMany(CampaignEntity.INSTANCE).stream().map(c -> c.get(CampaignEntity.NAME)), contains("shoes", "shirts"));
    }

}
