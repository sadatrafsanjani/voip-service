package com.jotno.voip.repository;

import com.jotno.voip.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;

@Repository
@Transactional
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Modifying
    @Query("UPDATE Appointment A SET A.status =: true WHERE A.id = : id")
    Appointment updateAppointmentStatus(long id);
}
