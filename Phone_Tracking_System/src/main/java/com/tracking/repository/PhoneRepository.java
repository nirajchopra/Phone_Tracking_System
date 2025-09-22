package com.tracking.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tracking.entity.Phone;
import com.tracking.entity.User;

@Repository
public interface PhoneRepository extends JpaRepository<Phone, Long> {
	List<Phone> findByOwner(User owner);

	Optional<Phone> findByDeviceId(String deviceId);

	Optional<Phone> findByPhoneNumber(String phoneNumber);

	@Query("SELECT p FROM Phone p WHERE p.owner = :owner AND p.deviceName LIKE %:deviceName%")
	List<Phone> findByOwnerAndDeviceNameContaining(@Param("owner") User owner, @Param("deviceName") String deviceName);
}
