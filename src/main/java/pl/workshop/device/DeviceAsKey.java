package pl.workshop.device;

import com.kenshoo.pl.entity.SingleUniqueKeyValue;

public class DeviceAsKey extends SingleUniqueKeyValue<DeviceEntity, DeviceType> {
    public DeviceAsKey(DeviceType value) {
        super(DeviceEntity.DEVICE, value);
    }
}
