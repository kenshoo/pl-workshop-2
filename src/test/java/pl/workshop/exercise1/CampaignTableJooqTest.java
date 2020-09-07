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

        DataTableUtils.createTable(jooq, CAMPAIGNS);

        var fields = jooq.fetch("desc " + CAMPAIGNS.getName()).intoMap("Field");

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

        jooq.insertInto(CAMPAIGNS, CampaignTable.name, CampaignTable.type).values("Tablets in USA", "ECOM").execute();

        Map<String, String> nameToType = jooq.select(CampaignTable.name, CampaignTable.type)
                .from(CAMPAIGNS)
                .fetchMap(CampaignTable.name, CampaignTable.type);

        assertThat(nameToType.get("Tablets in USA"), is(ECOM.name()));
    }
}
