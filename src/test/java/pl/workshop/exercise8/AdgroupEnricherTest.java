package pl.workshop.exercise8;

import com.kenshoo.jooq.DataTableUtils;
import com.kenshoo.pl.entity.*;
import org.jooq.DSLContext;
import org.jooq.lambda.Seq;
import org.junit.Before;
import org.junit.Test;
import pl.workshop.account.AccountTable;
import pl.workshop.adgroup.AdgroupEntity;
import pl.workshop.adgroup.AdgroupId;
import pl.workshop.adgroup.AdgroupPersistence;
import pl.workshop.adgroup.AdgroupTable;
import pl.workshop.campaign.CampaignTable;
import pl.workshop.database.JooqProvider;
import pl.workshop.device.DeviceTable;

import java.util.List;

import static com.kenshoo.pl.entity.PLCondition.trueCondition;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.jooq.lambda.Seq.seq;
import static pl.workshop.adgroup.AdgroupEntity.*;
import static pl.workshop.utils.SyntacticSugars.first;
import static pl.workshop.utils.SyntacticSugars.second;


public class AdgroupEnricherTest {

    private final CampaignTable CAMPAIGNS = CampaignTable.INSTANCE;
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
                { 500   ,  10           ,  "adgroup1"  ,  2              }
        };

        DataTableUtils.populateTable(jooq, ADGROUPS, DATA);
    }


    @Test
    public void testWhenAdGroupNameIsMissingThenItShallBeTheCampaignNamePlusRandomSuffix() {

        // TODO: [1] implement missing methods in AdgroupPersistence
        // TODO: [2] Add enricher that does what this test expects
        //
        // See example - https://github.com/kenshoo/persistence-layer/wiki/Enrichers
        //
        // Tips:
        //
        // * Enricher shall declare that the campaign NAME field should be fetched from DB so that
        //   campaign name value shall be available in the ChangeContext.
        //
        // * Field AdgroupEntity.CAMPAIGN_ID shall be annotated with @Required(RELATION)
        //

        var adgroupPL = new AdgroupPersistence(plContext);

        var commandToEnrich = new CreateEntityCommand<>(AdgroupEntity.INSTANCE);
        var commandToRemain = new CreateEntityCommand<>(AdgroupEntity.INSTANCE);

        commandToEnrich.set(CAMPAIGN_ID, 10);

        commandToRemain.set(CAMPAIGN_ID, 10);
        commandToRemain.set(NAME, "Don't change my name");

        var results = adgroupPL.create(List.of(commandToEnrich, commandToRemain));

        var adgroupsFromDB = fetchAdgroupsFromDB().toMap(a -> a.get(ID));

        assertThat(adgroupsFromDB.get(first(getIds(results))).get(NAME), startsWith("shoes"));
        assertThat(adgroupsFromDB.get(second(getIds(results))).get(NAME), is("Don't change my name"));
    }

    @Test
    public void testAdgroupNameShall_NOT_beEnrichedForUpdateCommand() {
        var adgroupPL = new AdgroupPersistence(plContext);
        var updateCommand = new UpdateEntityCommand<>(AdgroupEntity.INSTANCE, new AdgroupId(500));
        adgroupPL.update(List.of(updateCommand));
        var adgroupsFromDB = fetchAdgroupsFromDB().toMap(a -> a.get(ID));
        assertThat(adgroupsFromDB.get(500).get(NAME), is("adgroup1"));
    }

    @Test
    public void testWhenAdGroupDefaultBidIsMissingAndCampaignIncludesMobileDeviceThenDefaultBidShallBe10() {

        var adgroupPL = new AdgroupPersistence(plContext);

        var commandToEnrich = new CreateEntityCommand<>(AdgroupEntity.INSTANCE);
        var commandToRemain = new CreateEntityCommand<>(AdgroupEntity.INSTANCE);

        commandToEnrich.set(CAMPAIGN_ID, 10);
        commandToRemain.set(CAMPAIGN_ID, 20);

        var results = adgroupPL.create(List.of(commandToEnrich, commandToRemain));

        var adgroupsFromDB = fetchAdgroupsFromDB().toMap(a -> a.get(ID));

        assertThat(adgroupsFromDB.get(first(getIds(results))).get(DEFAULT_BID), is(10));
        assertThat(adgroupsFromDB.get(second(getIds(results))).get(DEFAULT_BID), nullValue());
    }

    private List<Integer> getIds(ChangeResult<AdgroupEntity, ?, ?> plResults) {
        return plResults
                .getChangeResults()
                .stream()
                .map(res -> res.getIdentifier().get(ID))
                .collect(toList());
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
