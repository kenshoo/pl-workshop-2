package pl.workshop.exercise11;

import com.kenshoo.jooq.DataTableUtils;
import com.kenshoo.pl.entity.CurrentEntityState;
import com.kenshoo.pl.entity.EntityField;
import com.kenshoo.pl.entity.EntityType;
import com.kenshoo.pl.entity.PLContext;
import org.apache.commons.lang3.ArrayUtils;
import org.jooq.DSLContext;
import org.junit.Before;
import org.junit.Test;
import pl.workshop.account.AccountTable;
import pl.workshop.campaign.CampaignEntity;
import pl.workshop.campaign.CampaignPersistence;
import pl.workshop.campaign.CreateCampaignCommand;
import pl.workshop.database.JooqProvider;
import pl.workshop.device.CreateDeviceCommand;
import pl.workshop.device.DeviceEntity;
import pl.workshop.device.DeviceType;

import java.util.List;
import java.util.Map;

import static com.kenshoo.pl.entity.PLCondition.trueCondition;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static pl.workshop.campaign.CampaignEntity.*;
import static pl.workshop.campaign.CampaignType.ECOM;
import static pl.workshop.campaign.CampaignType.SEARCH;
import static pl.workshop.device.DeviceEntity.DEVICE;
import static pl.workshop.device.DeviceType.*;


public class ValidateChildrenTest {

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

    @Test
    public void testWeDoNotAllowMobileDeviceForECOMCampaign() {

        var ecomCampaign = new CreateCampaignCommand()
                .with(NAME          , "Cheap watches")
                .with(TYPE          , ECOM)
                .with(DAILY_BUDGET  , 2)
                .with(MONTHLY_BUDGET, 60)
                .with(ACCOUNT_ID    , 1)
                .withChild(new CreateDeviceCommand().with(DEVICE, TABLET))
                .withChild(new CreateDeviceCommand().with(DEVICE, DESKTOP))
                .withChild(new CreateDeviceCommand().with(DEVICE, MOBILE))
                ;

        campaignPL.create(List.of(ecomCampaign));

        var actualCampaignsInDB = fetchAllCampaignsByNames();

        assertFalse(actualCampaignsInDB.containsKey("Cheap watches"));
    }

    @Test
    public void testWeAllowMobileDeviceForNonECOMCampaigns() {

        var searchCampaign = new CreateCampaignCommand()
                .with(NAME          , "Natanya Hayom")
                .with(TYPE          , SEARCH)
                .with(DAILY_BUDGET  , 1)
                .with(MONTHLY_BUDGET, 30)
                .with(ACCOUNT_ID    , 1)
                .withChild(new CreateDeviceCommand().with(DEVICE, TABLET))
                .withChild(new CreateDeviceCommand().with(DEVICE, DESKTOP))
                .withChild(new CreateDeviceCommand().with(DEVICE, MOBILE))
                ;

        campaignPL.create(List.of(searchCampaign));

        var actualCampaignsInDB = fetchAllCampaignsByNames();

        assertTrue(actualCampaignsInDB.containsKey("Natanya Hayom"));
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
