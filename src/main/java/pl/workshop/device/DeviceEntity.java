package pl.workshop.device;

import com.kenshoo.jooq.DataTable;
import com.kenshoo.pl.entity.AbstractEntityType;
import com.kenshoo.pl.entity.EntityField;
import com.kenshoo.pl.entity.converters.EnumAsStringValueConverter;
import pl.workshop.campaign.CampaignTable;


public class DeviceEntity extends AbstractEntityType<DeviceEntity> {

    private DeviceEntity() { super("DeviceEntity"); }
    public static final DeviceEntity INSTANCE = new DeviceEntity();

    @Override
    public DataTable getPrimaryTable() {
        return DeviceTable.INSTANCE;
    }

    public final static EntityField<DeviceEntity, Integer> CAMPAIGN_ID = INSTANCE.field(DeviceTable.campaign_id);
    public final static EntityField<DeviceEntity, DeviceType> DEVICE = INSTANCE.field(DeviceTable.device, new EnumAsStringValueConverter<>(DeviceType.class));

}
