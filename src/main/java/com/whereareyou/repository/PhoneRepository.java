package com.whereareyou.repository;

import com.whereareyou.entity.Phone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PhoneRepository extends JpaRepository<Phone, Long> {

    Optional<Phone> findByNumber(String number);
    Optional<Phone> findById(Long id);
}
