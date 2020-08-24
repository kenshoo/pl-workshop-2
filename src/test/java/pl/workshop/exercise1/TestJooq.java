package pl.workshop.exercise1;

import com.kenshoo.jooq.DataTableUtils;
import org.jooq.DSLContext;
import org.junit.Test;
import pl.workshop.CampaignTable;
import pl.workshop.database.JooqProvider;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;


public class TestJooq {

    private final CampaignTable CAMPAIGNS = CampaignTable.INSTANCE;
    private final DSLContext jooq = JooqProvider.create();

    @Test
    public void testThatYouCanDefineCampaignTableFields() {

        //
        // TODO: add more table fields to class CampaignTable.
        //

        DataTableUtils.createTable(jooq, CAMPAIGNS);

        var fields = jooq.fetch("desc CAMPAIGNS").intoMap("Field");

        assertThat(fields.get("id").get("Type"), is("int(11)"));
        assertThat(fields.get("id").get("Key"), is("PRI"));
        assertThat(fields.get("id").get("Extra"), is("auto_increment"));
        assertThat(fields.get("name").get("Type"), is("varchar(20)"));
        assertThat(fields.get("type").get("Type"), is("varchar(20)"));
    }

    @Test
    public void testThatYouCanInsertCampaignAndSelectIt() {
        DataTableUtils.createTable(jooq, CAMPAIGNS);

        //
        // TODO: insert with jooq campaign with name "my campaign" and type "ECOM"
        //

        // jooq.insertInto(...) ...

        //
        // TODO: select with jooq campaign name and type into a map.
        //
        Map<String, String> nameToType = null; // TODO:  jooq.select(...)

        assertThat(nameToType.get("my campaign"), is("ECOM"));
    }


}
