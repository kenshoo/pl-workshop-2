package pl.workshop.exercise2;

import com.kenshoo.jooq.DataTableUtils;
import com.kenshoo.pl.entity.CurrentEntityState;
import com.kenshoo.pl.entity.PLContext;
import org.jooq.DSLContext;
import org.junit.Before;
import org.junit.Test;
import pl.workshop.campaign.CampaignEntity;
import pl.workshop.campaign.CampaignTable;
import pl.workshop.database.JooqProvider;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static pl.workshop.campaign.CampaignType.SEARCH;
import static pl.workshop.utils.SyntacticSugars.first;


public class EntityQueryTest {

    private final CampaignTable CAMPAIGNS = CampaignTable.INSTANCE;
    private final DSLContext jooq = JooqProvider.create();
    private final PLContext plContext = new PLContext.Builder(jooq).build();

    @Before
    public void createCampaignTable() {
        DataTableUtils.createTable(jooq, CAMPAIGNS);

        final Object[][] DATA = {
             // +------+--------------+-----------+----------+
             // | id   | publisher_id | name      |  type    |
             // +------+--------------+-----------+----------+
                {  1   ,  1           , "shoes"   , "SEARCH" },
                {  2   ,  1           , "shirts"  , "SEARCH" },
        };

        DataTableUtils.populateTable(jooq, CAMPAIGNS, DATA);
    }

    @Test
    public void testThatWeCanDefineAndQueryEntityFields() {

        // TODO: [1] go to class CampaignEntity and define the entity fields.

        List<CurrentEntityState> campaigns = null; /* TODO: [2] replace 'null' with the query:
            plContext
                .select(... the fields  ...)
                .from  (... entity type ...)
                .where (... condition   ...)
                .fetch ();
        */

        assertThat(first(campaigns).get(CampaignEntity.NAME), is("shoes"));

        // TODO: uncomment these lines
        // assertThat(first(campaigns).get(CampaignEntity.ID), is(1));
        // assertThat(first(campaigns).get(CampaignEntity.PUBLISHER_ID), is(1));
        // assertThat(first(campaigns).get(CampaignEntity.TYPE), is(SEARCH));
    }


}
