package pl.workshop.device;

import com.kenshoo.jooq.DataTable;
import com.kenshoo.pl.entity.AbstractEntityType;
import com.kenshoo.pl.entity.EntityField;
import com.kenshoo.pl.entity.SupportedChangeOperation;
import com.kenshoo.pl.entity.annotation.Required;
import com.kenshoo.pl.entity.converters.EnumAsStringValueConverter;

import static com.kenshoo.pl.entity.SupportedChangeOperation.CREATE_UPDATE_AND_DELETE;
import static com.kenshoo.pl.entity.annotation.RequiredFieldType.RELATION;


public class DeviceEntity extends AbstractEntityType<DeviceEntity> {

    private DeviceEntity() { super("DeviceEntity"); }
    public static final DeviceEntity INSTANCE = new DeviceEntity();

    @Override
    public DataTable getPrimaryTable() {
        return DeviceTable.INSTANCE;
    }

    @Override
    public SupportedChangeOperation getSupportedOperation() {
        return CREATE_UPDATE_AND_DELETE;
    }

    public final static EntityField<DeviceEntity, Integer> ID = INSTANCE.field(DeviceTable.id);
    @Required(RELATION)
    public final static EntityField<DeviceEntity, Integer> CAMPAIGN_ID = INSTANCE.field(DeviceTable.campaign_id);
    public final static EntityField<DeviceEntity, DeviceType> DEVICE = INSTANCE.field(DeviceTable.device, new EnumAsStringValueConverter<>(DeviceType.class));

}
