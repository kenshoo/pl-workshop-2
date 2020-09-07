package pl.workshop.device;

import com.kenshoo.pl.entity.CreateEntityCommand;
import com.kenshoo.pl.entity.EntityField;


public class CreateDeviceCommand extends CreateEntityCommand<DeviceEntity> {

    public CreateDeviceCommand() {
        super(DeviceEntity.INSTANCE);
    }

    public <T> CreateDeviceCommand with(EntityField<DeviceEntity, T> field, T value) {
        super.set(field, value);
        return this;
    }

}
