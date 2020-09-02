package pl.workshop.exercise7;

import com.kenshoo.jooq.DataTableUtils;
import com.kenshoo.pl.entity.CreateEntityCommand;
import com.kenshoo.pl.entity.PLContext;
import org.jooq.DSLContext;
import org.junit.Before;
import org.junit.Test;
import pl.workshop.account.AccountTable;
import pl.workshop.campaign.CampaignBudgetTable;
import pl.workshop.campaign.CampaignEntity;
import pl.workshop.campaign.CampaignPersistence;
import pl.workshop.campaign.CampaignTable;
import pl.workshop.database.JooqProvider;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static pl.workshop.campaign.CampaignEntity.*;


public class CampaignValidationTest {

    private final CampaignTable CAMPAIGNS = CampaignTable.INSTANCE;
    private final CampaignBudgetTable BUDGETS = CampaignBudgetTable.INSTANCE;
    private final AccountTable ACCOUNTS = AccountTable.INSTANCE;
    private final DSLContext jooq = JooqProvider.create();
    private final PLContext plContext = new PLContext.Builder(jooq).withFeaturePredicate(any -> true).build();
    private final CampaignPersistence campaignPL = new CampaignPersistence(plContext);


    @Before
    public void createAccountTable() {
        DataTableUtils.createTable(jooq, ACCOUNTS);

        final Object[][] DATA = {
             // +------+--------------+-----------+
             // | id   | user_name    | status    |
             // +------+--------------+-----------+
                {  1   ,  "Joker"     , "ACTIVE"  }
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
                {  10  ,  1           , "shoes"   , "SEARCH" }
        };

        DataTableUtils.populateTable(jooq, CAMPAIGNS, DATA);
    }


    @Test
    public void testDailyBudgetShouldBeGreaterThanZero() {

        // Here we shall validate a single field.
        // So we can implement the most simple type of validator, which is FieldValidator.
        // Create a new PositiveDailyBudgetValidator class. See wiki for "adding simple validator".
        // Don't forget to register the validator to the flowBuilder (also in the wiki).

        var commandToPass = new CreateEntityCommand<>(CampaignEntity.INSTANCE);
        var commandToFail = new CreateEntityCommand<>(CampaignEntity.INSTANCE);

        commandToPass.set(ACCOUNT_ID, 1);
        commandToPass.set(NAME, "good");
        commandToPass.set(DAILY_BUDGET, 100);
        commandToPass.set(MONTHLY_BUDGET, 1000);

        commandToFail.set(ACCOUNT_ID, 1);
        commandToFail.set(NAME, "bad");
        commandToFail.set(DAILY_BUDGET, -100); // this is not valid
        commandToFail.set(MONTHLY_BUDGET, 1000);

        var plResults = campaignPL.create(List.of(commandToPass, commandToFail));

        assertThat("commandToPass should not have errors", plResults.getErrors(commandToPass), empty());
        assertThat("commandToFail should have an error", plResults.getErrors(commandToFail), not(empty()));
    }

    @Test
    public void testMonthlyBudgetShouldBeGreaterThanDailyBudget() {

        // Now we shall validate a two fields (combination).
        // We should implement FieldsCombinationValidator.
        // Create a new PositiveDailyBudgetValidator class implementing FieldsCombinationValidator.
        // Don't forget to register the validator to the flowBuilder (also in the wiki).

        var commandToFail = new CreateEntityCommand<>(CampaignEntity.INSTANCE);
        var commandToPass = new CreateEntityCommand<>(CampaignEntity.INSTANCE);

        commandToFail.set(ACCOUNT_ID, 1);
        commandToFail.set(NAME, "bad");
        commandToFail.set(DAILY_BUDGET, 500);
        commandToFail.set(MONTHLY_BUDGET, 499);

        commandToPass.set(ACCOUNT_ID, 1);
        commandToPass.set(NAME, "good");
        commandToPass.set(DAILY_BUDGET, 500);
        commandToPass.set(MONTHLY_BUDGET, 1000);

        var plResults = campaignPL.create(List.of(commandToFail, commandToPass));

        assertThat("commandToPass should not have errors", plResults.getErrors(commandToPass), empty());
        assertThat("commandToFail should have an error", plResults.getErrors(commandToFail), not(empty()));
    }

    @Test
    public void testCampaignNameShouldNotIncludeTheAccountUserName() {

        // Here we need a field from the Campaign and another field from its parent entity (the account).
        // Do this by implementing FieldComplexValidator.
        // In order for this validator to work on CREATE, the entity field CampaignEntity.ACCOUNT_ID
        // should be annotated with @Required(RELATION).

        var commandToFail = new CreateEntityCommand<>(CampaignEntity.INSTANCE);
        var commandToPass = new CreateEntityCommand<>(CampaignEntity.INSTANCE);

        commandToFail.set(ACCOUNT_ID, 1);
        commandToFail.set(NAME, "The joker is back");
        commandToFail.set(DAILY_BUDGET, 500);
        commandToFail.set(MONTHLY_BUDGET, 600);

        commandToPass.set(ACCOUNT_ID, 1);
        commandToPass.set(NAME, "The batman is back");
        commandToPass.set(DAILY_BUDGET, 500);
        commandToPass.set(MONTHLY_BUDGET, 600);

        var plResults = campaignPL.create(List.of(commandToFail, commandToPass));

        assertThat("commandToPass should not have errors", plResults.getErrors(commandToPass), empty());
        assertThat("commandToFail should have an error", plResults.getErrors(commandToFail), not(empty()));
    }

}
