package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.domain.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
	
	@Query("SELECT r "
		 + "  FROM Reservation r "
		 + "    LEFT JOIN FETCH r.reservationFor reservationFor "
		 + " WHERE r.status = 'ACTIVE' ")
	List<Reservation> findActiveReservations();
}
