package pl.workshop.exercise6;

import com.kenshoo.jooq.DataTableUtils;
import com.kenshoo.pl.entity.*;
import org.jooq.DSLContext;
import org.junit.Before;
import org.junit.Test;
import pl.workshop.account.AccountTable;
import pl.workshop.campaign.*;
import pl.workshop.database.JooqProvider;

import javax.print.attribute.standard.MediaSize;
import java.util.List;
import java.util.Map;

import static com.kenshoo.pl.entity.PLCondition.trueCondition;
import static java.util.stream.Collectors.toMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static pl.workshop.campaign.CampaignEntity.*;
import static pl.workshop.campaign.CampaignType.SEARCH;


public class PersistCampaignsUsingPLTest {

    private final CampaignTable CAMPAIGNS = CampaignTable.INSTANCE;
    private final CampaignBudgetTable BUDGETS = CampaignBudgetTable.INSTANCE;
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
    public void createCampaignAndBudgetTables() {
        DataTableUtils.createTable(jooq, CAMPAIGNS);
        DataTableUtils.createTable(jooq, BUDGETS);

        final Object[][] DATA = {
                // +------+--------------+-----------+----------+
                // | id   | account_id   | name      |  type    |
                // +------+--------------+-----------+----------+
                {10, 1, "shoes", "SEARCH"}
        };

        DataTableUtils.populateTable(jooq, CAMPAIGNS, DATA);
    }


    @Test
    public void testCreatingNewCampaigns() {

        var campaignPL = new CampaignPersistence(plContext);

        var command1 = new CreateEntityCommand<>(CampaignEntity.INSTANCE);
        var command2 = new CreateEntityCommand<>(CampaignEntity.INSTANCE);

        command1.set(NAME, "TV Japan");
        command1.set(ACCOUNT_ID, 1);
        command1.set(TYPE, SEARCH);
        command1.set(DAILY_BUDGET, 400);

        command2.set(NAME, "TV Dublin");
        command2.set(ACCOUNT_ID, 1);
        command2.set(TYPE, SEARCH);
        command2.set(DAILY_BUDGET, 1600);


        campaignPL.create(List.of(command1, command2));

        var campaignByNames = fetchAllCampaignsByNames();

        assertThat(campaignByNames.get("TV Japan").get(ACCOUNT_ID), is(1));
        assertThat(campaignByNames.get("TV Japan").get(TYPE), is(SEARCH));
        assertThat(campaignByNames.get("TV Japan").get(DAILY_BUDGET), is(400));

        assertThat(campaignByNames.get("TV Dublin").get(ACCOUNT_ID), is(1));
        assertThat(campaignByNames.get("TV Dublin").get(TYPE), is(SEARCH));
        assertThat(campaignByNames.get("TV Dublin").get(DAILY_BUDGET), is(1600));
    }

    @Test
    public void testUpdateExistingCampaignById() {

        var campaignPL = new CampaignPersistence(plContext);

        var command = new UpdateEntityCommand<>(CampaignEntity.INSTANCE, new CampaignId(10));
        command.set(DAILY_BUDGET, 5050);

        campaignPL.update(List.of(command));

        assertThat(fetchAllCampaignsByNames().get("shoes").get(DAILY_BUDGET), is(5050));
    }

    @Test
    public void testUpdateExistingCampaignByNameAndAccountId() {

        var campaignPL = new CampaignPersistence(plContext);

        var command = new UpdateEntityCommand<>(CampaignEntity.INSTANCE, new PairUniqueKeyValue<>(ACCOUNT_ID, NAME, 1, "shoes"));
        command.set(DAILY_BUDGET, 8080);

        campaignPL.update(List.of(command));

        // TODO: [2] Create and execute an updated command using CampaignNameAndAccount as the identifier.

        assertThat(fetchAllCampaignsByNames().get("shoes").get(DAILY_BUDGET), is(8080));
    }

    private Map<String, CurrentEntityState> fetchAllCampaignsByNames() {
        return plContext
                .select(allFieldsOf(CampaignEntity.INSTANCE))
                .from(CampaignEntity.INSTANCE)
                .where(trueCondition())
                .fetch()
                .stream()
                .collect(toMap(c -> c.get(NAME), c -> c));
    }

    private EntityField[] allFieldsOf(EntityType<?> entity) {
        return entity.getFields().toArray(EntityField[]::new);
    }

}
