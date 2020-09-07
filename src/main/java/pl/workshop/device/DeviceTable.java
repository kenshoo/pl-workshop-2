package pl.workshop.device;

import com.kenshoo.jooq.AbstractDataTable;
import org.jooq.Record;
import org.jooq.TableField;
import org.jooq.impl.SQLDataType;
import pl.workshop.campaign.CampaignTable;

public class DeviceTable extends AbstractDataTable<DeviceTable> {

    public final static DeviceTable INSTANCE = new DeviceTable("devices");

    DeviceTable(String tableName) { super(tableName); }
    DeviceTable(DeviceTable aliased, String alias) { super(aliased, alias); }
    @Override public DeviceTable as(String alias) { return new DeviceTable(this, alias); }


    public final static TableField<Record, Integer> id = INSTANCE.createPKField("id", SQLDataType.INTEGER.identity(true));
    public final static TableField<Record, Integer> campaign_id = INSTANCE.createFKField("campaign_id", CampaignTable.id);
    public final static TableField<Record, String> device = INSTANCE.createField("device", SQLDataType.VARCHAR(20));

}
