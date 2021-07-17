package com.webbook.webbook.repository;

import com.webbook.webbook.models.Device;
import com.webbook.webbook.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {

    Optional<Device> findByRefreshToken(String refreshToken);

    @Modifying
    @Query("update Device d set d.refreshToken = :refreshToken,d.expirationDate=:expirationDate where d.deviceInfo = :deviceInfo and d.user= :user")
    @Transactional
    void updateRefreshToken(@Param("refreshToken") String refreshToken,
                            @Param("deviceInfo")String deviceInfo, @Param("user") User user,@Param("expirationDate") Date expirationDate);

    @Query("SELECT d.rememberMe FROM Device d where d.deviceInfo = :deviceInfo and d.user= :user")
    Boolean findRememberMeStateByRefreshToken(@Param("deviceInfo")String deviceInfo, @Param("user") User user);

    @Modifying
    @Query("delete from Device d where d.deviceInfo = :deviceInfo and d.user= :user")
    @Transactional
    void deleteDevice(@Param("deviceInfo")String deviceInfo, @Param("user") User user);

    boolean existsByUserAndDeviceInfo(User user,String deviceInfo);
}
