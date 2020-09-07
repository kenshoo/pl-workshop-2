package pl.workshop.exercise3;

import com.kenshoo.jooq.DataTableUtils;
import com.kenshoo.pl.entity.CurrentEntityState;
import com.kenshoo.pl.entity.PLContext;
import org.jooq.DSLContext;
import org.junit.Before;
import org.junit.Test;
import pl.workshop.campaign.CampaignBudgetTable;
import pl.workshop.campaign.CampaignEntity;
import pl.workshop.campaign.CampaignTable;
import pl.workshop.database.JooqProvider;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static pl.workshop.utils.SyntacticSugars.first;


public class CampaignWithSecondaryBudgetTableTest {

    private final CampaignTable CAMPAIGNS = CampaignTable.INSTANCE;
    private final CampaignBudgetTable BUDGETS = CampaignBudgetTable.INSTANCE;
    private final DSLContext jooq = JooqProvider.create();
    private final PLContext plContext = new PLContext.Builder(jooq).build();

    @Before
    public void createCampaignTable() {
        DataTableUtils.createTable(jooq, CAMPAIGNS);

        final Object[][] DATA = {
             // +------+--------------+-----------+----------+
             // | id   | account_id   | name      |  type    |
             // +------+--------------+-----------+----------+
                {  1   ,  1           , "shoes"   , "SEARCH" },
                {  2   ,  1           , "shirts"  , "SEARCH" },
        };

        DataTableUtils.populateTable(jooq, CAMPAIGNS, DATA);
    }

    @Before
    public void createBudgetTable() {
        DataTableUtils.createTable(jooq, BUDGETS);

        final Object[][] DATA = {
             // +---------------+--------------+-----------------+
             // | campaign_id   | DAILY_BUDGET | monthly_budget  |
             // +---------------+--------------+-----------------+
                {    1          ,  1000        ,  30000          },
                {    2          ,  4000        ,  40000          },
        };

        DataTableUtils.populateTable(jooq, BUDGETS, DATA);
    }


    @Test
    public void testThatWeCanSelectBudgetFields() {

        // TODO: [1] go to class CampaignBudgetTable and define the table fields.
        // TODO: [2] go to class CampaignEntity and define the budget fields in there by referring the budget table.

        List<CurrentEntityState> campaigns =
            plContext
                .select(CampaignEntity.DAILY_BUDGET, CampaignEntity.MONTHLY_BUDGET)
                .from  (CampaignEntity.INSTANCE)
                .where (CampaignEntity.ID.eq(1))
                .fetch ();

        // TODO: uncomment these lines
         assertThat(first(campaigns).get(CampaignEntity.DAILY_BUDGET), is(1000));
         assertThat(first(campaigns).get(CampaignEntity.MONTHLY_BUDGET), is(30000));
    }


}
