package pl.workshop.exercise9;

import com.kenshoo.jooq.DataTableUtils;
import com.kenshoo.pl.entity.*;
import com.kenshoo.pl.entity.spi.FieldValueSupplier;
import com.kenshoo.pl.entity.spi.NotSuppliedException;
import com.kenshoo.pl.entity.spi.ValidationException;
import org.jooq.DSLContext;
import org.jooq.lambda.Seq;
import org.junit.Before;
import org.junit.Test;
import pl.workshop.account.AccountTable;
import pl.workshop.adgroup.AdgroupEntity;
import pl.workshop.adgroup.AdgroupId;
import pl.workshop.adgroup.AdgroupPersistence;
import pl.workshop.adgroup.AdgroupTable;
import pl.workshop.campaign.CampaignEntity;
import pl.workshop.campaign.CampaignId;
import pl.workshop.campaign.CampaignTable;
import pl.workshop.database.JooqProvider;
import pl.workshop.device.DeviceTable;

import java.util.List;
import java.util.stream.Stream;

import static com.kenshoo.pl.entity.PLCondition.trueCondition;
import static com.kenshoo.pl.entity.spi.FieldValueSupplier.fromOldValue;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.jooq.lambda.Seq.seq;
import static pl.workshop.adgroup.AdgroupEntity.*;
import static pl.workshop.campaign.CampaignEntity.DAILY_BUDGET;
import static pl.workshop.campaign.CampaignEntity.MONTHLY_BUDGET;
import static pl.workshop.utils.SyntacticSugars.first;
import static pl.workshop.utils.SyntacticSugars.second;


public class AdgroupSupplierTest {

    private final CampaignTable CAMPAIGNS = CampaignTable.INSTANCE;
    private final AccountTable ACCOUNTS = AccountTable.INSTANCE;
    private final DeviceTable DEVICES = DeviceTable.INSTANCE;
    private final AdgroupTable ADGROUPS = AdgroupTable.INSTANCE;
    private final DSLContext jooq = JooqProvider.create();
    private final PLContext plContext = new PLContext.Builder(jooq).withFeaturePredicate(any -> true).build();
    private final AdgroupPersistence adgroupPL = new AdgroupPersistence(plContext);


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
    public void createCampaignTable() {
        DataTableUtils.createTable(jooq, CAMPAIGNS);

        final Object[][] DATA = {
             // +------+--------------+-----------+----------+
             // | id   | account_id   | name      |  type    |
             // +------+--------------+-----------+----------+
                {  10  ,  1           , "shoes"   , "SEARCH" },
                {  20  ,  1           , "dolls"   , "SEARCH" },
        };

        DataTableUtils.populateTable(jooq, CAMPAIGNS, DATA);
    }

    @Before
    public void createDeviceTable() {
        DataTableUtils.createTable(jooq, DEVICES);

        final Object[][] DATA = {
             // +--------------+-------------+
             // | campaign_id  | device      |
             // +--------------+-------------+
                {  10          , "MOBILE"    },
                {  10          , "DESKTOP"   },
                {  20          , "DESKTOP"   },
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
                { 500   ,  10           ,  "adgroup1"  ,  2              },
                { 600   ,  20           ,  "adgroup2"  ,  4              },
        };

        DataTableUtils.populateTable(jooq, ADGROUPS, DATA);
    }

    @Test
    public void testAddOneDollarToAllDefaultBidsUsingSupplier() {

        var command1 = new UpdateEntityCommand<>(AdgroupEntity.INSTANCE, new AdgroupId(500));
        var command2 = new UpdateEntityCommand<>(AdgroupEntity.INSTANCE, new AdgroupId(600));

        // TODO: Instead of setting a fixed value into the command, you can set a FieldValueSupplier
        //       into the command. The supplier acts as a function to produce the desired value based
        //       on an existing state.
        //       The easiest way to create a FieldValueSupplier is by calling static method "fromOldValue".
        //
        // command1.set(DEFAULT_BID, ... your supplier ...);
        // command2.set(DEFAULT_BID, ... your supplier ...);

        adgroupPL.update(List.of(command1, command2));

        var adgroupsFromDB = fetchAdgroupsFromDB().toMap(a -> a.get(ID));

        assertThat(adgroupsFromDB.get(500).get(DEFAULT_BID), is(3));
        assertThat(adgroupsFromDB.get(600).get(DEFAULT_BID), is(5));
    }

    private Seq<CurrentEntityState> fetchAdgroupsFromDB() {
        return seq(plContext
                .select(allFieldsOf(AdgroupEntity.INSTANCE))
                .from(AdgroupEntity.INSTANCE)
                .where(trueCondition())
                .fetch());
    }

    private EntityField[] allFieldsOf(EntityType<?> entity) {
        return entity.getFields().toArray(EntityField[]::new);
    }
}
