package pl.workshop.device;

import com.kenshoo.pl.entity.InsertOnDuplicateUpdateCommand;


public class UpsertDeviceCommand extends InsertOnDuplicateUpdateCommand<DeviceEntity, DeviceAsKey> {

    public UpsertDeviceCommand(DeviceType device) {
        super(DeviceEntity.INSTANCE, new DeviceAsKey(device));
    }

}
