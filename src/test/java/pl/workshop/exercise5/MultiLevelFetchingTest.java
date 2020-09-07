package pl.workshop.exercise5;

import com.kenshoo.jooq.DataTableUtils;
import com.kenshoo.pl.entity.CurrentEntityState;
import com.kenshoo.pl.entity.PLContext;
import org.jooq.DSLContext;
import org.junit.Before;
import org.junit.Test;
import pl.workshop.account.AccountEntity;
import pl.workshop.account.AccountTable;
import pl.workshop.adgroup.AdgroupEntity;
import pl.workshop.adgroup.AdgroupTable;
import pl.workshop.campaign.CampaignBudgetTable;
import pl.workshop.device.DeviceEntity;
import pl.workshop.device.DeviceTable;
import pl.workshop.campaign.CampaignEntity;
import pl.workshop.campaign.CampaignTable;
import pl.workshop.database.JooqProvider;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static pl.workshop.device.DeviceType.DESKTOP;
import static pl.workshop.device.DeviceType.MOBILE;
import static pl.workshop.matchers.StreamMatcher.contains;
import static pl.workshop.utils.SyntacticSugars.first;


public class MultiLevelFetchingTest {

    private final CampaignTable CAMPAIGNS = CampaignTable.INSTANCE;
    private final CampaignBudgetTable BUDGETS = CampaignBudgetTable.INSTANCE;
    private final AccountTable ACCOUNTS = AccountTable.INSTANCE;
    private final DeviceTable DEVICES = DeviceTable.INSTANCE;
    private final AdgroupTable ADGROUPS = AdgroupTable.INSTANCE;
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
                {10, 1, "shoes", "SEARCH"}
        };

        DataTableUtils.populateTable(jooq, CAMPAIGNS, DATA);
    }

    @Before
    public void createBudgetTable() {
        DataTableUtils.createTable(jooq, BUDGETS);

        final Object[][] DATA = {
                // +---------------+--------------+-----------------+
                // | campaign_id   | daily_budget | monthly_budget  |
                // +---------------+--------------+-----------------+
                {10, 1000, 30000}
        };

        DataTableUtils.populateTable(jooq, BUDGETS, DATA);
    }

    @Before
    public void createDeviceTable() {
        DataTableUtils.createTable(jooq, DEVICES);

        final Object[][] DATA = {
                // +--------------+-------------+
                // | campaign_id  | device      |
                // +--------------+-------------+
                {10, "MOBILE"},
                {10, "DESKTOP"},
        };

        DataTableUtils.populateTable(jooq, DEVICES, DATA);
    }

    @Before
    public void createAdgroupTable() {
        DataTableUtils.createTable(jooq, ADGROUPS);

        final Object[][] DATA = {
                // +-------+---------------+--------------+-----------------+
                // | id    | campaign_id   | name         | default_bid     |
                // +-------+---------------+--------------+-----------------+
                {500, 10, "adgroup1", 2}
        };

        DataTableUtils.populateTable(jooq, ADGROUPS, DATA);
    }


    @Test
    public void testFetchingAdgroupAlongWithOtherEntities() {
        CurrentEntityState adgroup =
                plContext.select(AccountEntity.USER_NAME, CampaignEntity.DAILY_BUDGET, DeviceEntity.DEVICE)
                        .from(AdgroupEntity.INSTANCE)
                        .where(AdgroupEntity.ID.eq(500))
                        .fetch()
                        .get(0);

        assertThat(adgroup.get(AccountEntity.USER_NAME), is("Joker"));
        assertThat(adgroup.get(CampaignEntity.DAILY_BUDGET), is(1000));
        assertThat(adgroup.getMany(DeviceEntity.INSTANCE).stream()
                .map(deviceEntityFieldsValueMap -> deviceEntityFieldsValueMap.get(DeviceEntity.DEVICE)),contains(MOBILE, DESKTOP));
    }

}
