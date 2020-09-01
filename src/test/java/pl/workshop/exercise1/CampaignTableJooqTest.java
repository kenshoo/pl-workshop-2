package pl.workshop.exercise1;

import com.kenshoo.jooq.DataTableUtils;
import org.jooq.DSLContext;
import org.junit.Test;
import pl.workshop.campaign.CampaignTable;
import pl.workshop.database.JooqProvider;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static pl.workshop.campaign.CampaignType.ECOM;


public class CampaignTableJooqTest {

    private final CampaignTable CAMPAIGNS = CampaignTable.INSTANCE;
    private final DSLContext jooq = JooqProvider.create();

    @Test
    public void testThatYouCanDefineCampaignTableFields() {

        //
        // TODO: go to class CampaignTable to define the expected table fields.
        //

        DataTableUtils.createTable(jooq, CAMPAIGNS);

        var fields = jooq.fetch("desc CAMPAIGNS").intoMap("Field");

        assertThat(fields.get("id").get("Type"), is("int(11)"));
        assertThat(fields.get("id").get("Key"), is("PRI"));
        assertThat(fields.get("id").get("Extra"), is("auto_increment"));
        assertThat(fields.get("account_id").get("Type"), is("int(11)"));
        assertThat(fields.get("name").get("Type"), is("varchar(20)"));
        assertThat(fields.get("type").get("Type"), is("varchar(20)"));
    }

    @Test
    public void testThatYouCanInsertCampaignAndSelectIt() {
        DataTableUtils.createTable(jooq, CAMPAIGNS);

        //
        // TODO: use jooq to insert a new campaign with name "my campaign" and type "ECOM"
        //

        // jooq.insertInto(...) ...

        //
        // TODO: use jooq to fetch campaigns name and type into a map.
        //
        Map<String, String> nameToType = null; // TODO:  jooq.select(...)

        assertThat(nameToType.get("Tablets in USA"), is(ECOM.name()));
    }


}
