package pl.workshop.exercise10;

import com.kenshoo.jooq.DataTableUtils;
import com.kenshoo.pl.entity.*;
import org.apache.commons.lang3.ArrayUtils;
import org.jooq.DSLContext;
import org.junit.Before;
import org.junit.Test;
import pl.workshop.account.AccountTable;
import pl.workshop.campaign.*;
import pl.workshop.database.JooqProvider;
import pl.workshop.device.*;

import java.util.List;
import java.util.Map;

import static com.kenshoo.pl.entity.PLCondition.trueCondition;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static pl.workshop.campaign.CampaignEntity.*;
import static pl.workshop.device.DeviceEntity.DEVICE;
import static pl.workshop.device.DeviceType.MOBILE;
import static pl.workshop.device.DeviceType.TABLET;


public class PersistCampaignWithChildrenTest {

    private final DeviceTable DEVICES = DeviceTable.INSTANCE;
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

    @Before
    public void createDeviceTable() {
        DataTableUtils.createTable(jooq, DEVICES);

        final Object[][] DATA = {
             // +-----+--------------+-------------+
             // | id  | campaign_id  | device      |
             // +-----+--------------+-------------+
                {  1  ,  10          , "MOBILE"    },
                {  2  ,  10          , "DESKTOP"   },
        };

        DataTableUtils.populateTable(jooq, DEVICES, DATA);
    }

    @Test
    public void testCreateCampaignWithMultipleDevices() {

        var newCampaign = new CreateCampaignCommand()
            .with(NAME          , "Forever Trump")
            .with(DAILY_BUDGET  , 100)
            .with(MONTHLY_BUDGET, 1000)
            .with(ACCOUNT_ID    , 1)
            // TODO: [1] Add device commands as children.
            //       To make the code pretty, use the fluent 'CreateDeviceCommand' class
            //       which has a "builder" semantic.
            // .withChild(...)
            // .withChild(...)
                ;

        // TODO: [2] Add a new Device flowConfig to the Campaign flowConfig defined in CampaignPersistence.
        //       See "Child commands" section in the wiki.

        campaignPL.create(List.of(newCampaign));

        assertThat(fetchDevicesForCampaign("Forever Trump"), containsInAnyOrder(MOBILE, TABLET));
    }

    @Test
    public void testReplaceDevicesForExistingCampaign() {

        var campaignToUpdate = new UpdateCampaignCommand(10)
                // TODO: [1] Instead of 'CreateDeviceCommand', we shall use 'UpsertDeviceCommand' because
                //           we don't know in advance which devices should be created and which ones should be updated.
                // .withChild(...)
                // .withChild(...)
                // TODO: [2] add a new 'DeletionOfOther' to remove devices we did not specified.
                // .with(...)
                ;

        campaignPL.update(List.of(campaignToUpdate));

        assertThat(fetchDevicesForCampaign("shoes"), containsInAnyOrder(TABLET));
    }


    private Map<String, CurrentEntityState> fetchAllCampaignsByNames() {
        return plContext
                .select(ArrayUtils.add(allFieldsOf(CampaignEntity.INSTANCE), DEVICE))
                .from(CampaignEntity.INSTANCE)
                .where(trueCondition())
                .fetch()
                .stream()
                .collect(toMap(c -> c.get(NAME), c -> c));
    }

    private EntityField[] allFieldsOf(EntityType<?> entity) {
        return entity.getFields().toArray(EntityField[]::new);
    }

    private List<DeviceType> fetchDevicesForCampaign(String campaignName) {
        return fetchAllCampaignsByNames()
                .get(campaignName)
                .getMany(DeviceEntity.INSTANCE)
                .stream()
                .map(__ -> __.get(DEVICE))
                .collect(toList());
    }

}
