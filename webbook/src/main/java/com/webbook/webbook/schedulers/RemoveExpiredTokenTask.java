package com.webbook.webbook.schedulers;

import com.webbook.webbook.models.Device;
import com.webbook.webbook.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RemoveExpiredTokenTask {

    private final DeviceRepository deviceRepository;

    @Autowired
    public RemoveExpiredTokenTask(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Scheduled(fixedDelayString = "${removeExpiredToken.fixedDelay.in.milliseconds}",
            initialDelayString = "${removeExpiredToken.initialDelay.in.milliseconds}")
    public void removeExpiredRefreshTokenFromDB(){
        List<Device> expiredDeviceRefreshToken = deviceRepository.findAll().stream()
                .filter(device -> device.getExpirationDate().before(new Date()))
                .collect(Collectors.toList());
        deviceRepository.deleteAll(expiredDeviceRefreshToken);
    }
}
