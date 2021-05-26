package com.example.demo;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.hibernate.Filter;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.demo.domain.Place;
import com.example.demo.domain.Reservation;
import com.example.demo.repository.PlaceRepository;
import com.example.demo.repository.ReservationRepository;

@SpringBootTest(classes = {TestApplication.class})
@Transactional
public class JPAUnitTestCase {

	@PersistenceContext
    private EntityManager entityManager;
	
	@Autowired
    private PlaceRepository placeRepository;
	
	@Autowired
    private ReservationRepository reservationRepository;

	@BeforeEach
    public void init() {
		Place p1 = new Place();
		p1.setName("place1");
		p1.setAddress("address1");
		
		placeRepository.save(p1);
		
		Place p2 = new Place();
		p2.setName("place2");
		p2.setAddress("address2");
		
		placeRepository.save(p2);
		
		Reservation r1 = new Reservation();
		r1.setStatus("ACTIVE");
		r1.setReservationFor(p1);
		
		reservationRepository.save(r1);
		
		Reservation r2 = new Reservation();
		r2.setStatus("EXPIRED");
		r2.setReservationFor(p2);
		
		reservationRepository.save(r2);
	}
	
	@Test
    @Transactional
    public void testSearchPlace() {
		List<Place> listPlaces = placeRepository.findAll();
		assertThat(listPlaces).hasSize(2);
	}
	
	@Test
    @Transactional
	public void testSimpleSearchReservations() {
		List<Reservation> listReservations = reservationRepository.findAll();
		assertThat(listReservations).hasSize(2);
	}
	
	@Test
    @Transactional
	public void testSearchReservationsWithFilterDisabled() {
		List<Reservation> listActiveReservations = reservationRepository.findActiveReservations();
		assertThat(listActiveReservations).hasSize(1);
	}
	
	@Test
    @Transactional
	public void testSearchReservationsWithFilterEnabled() {
		Filter filter = entityManager.unwrap(Session.class).enableFilter("nameFilter");
		filter.setParameter("nameParam", "place3");
		
		List<Reservation> listActiveReservationsWithFilter = reservationRepository.findActiveReservations();
		assertThat(listActiveReservationsWithFilter).hasSize(0);
	}
}

