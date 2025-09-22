package com.tracking.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tracking.entity.CurrentLocation;
import com.tracking.entity.Phone;

@Repository
public interface CurrentLocationRepository extends JpaRepository<CurrentLocation, Long> {
	Optional<CurrentLocation> findByPhone(Phone phone);
}
