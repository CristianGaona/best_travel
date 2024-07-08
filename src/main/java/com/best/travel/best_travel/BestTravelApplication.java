package com.best.travel.best_travel;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.best.travel.best_travel.domain.entity.ReservationEntity;
import com.best.travel.best_travel.domain.entity.TicketEntity;
import com.best.travel.best_travel.domain.entity.TourEntity;
import com.best.travel.best_travel.domain.repository.CustomerRepository;
import com.best.travel.best_travel.domain.repository.FlyRepository;
import com.best.travel.best_travel.domain.repository.HotelRepository;
import com.best.travel.best_travel.domain.repository.ReservationRepository;
import com.best.travel.best_travel.domain.repository.TicketRepository;
import com.best.travel.best_travel.domain.repository.TourRepository;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class BestTravelApplication implements CommandLineRunner{

	private final HotelRepository hotelRepository;

	private final FlyRepository flyRepository;

	private final TicketRepository ticketRepository;

	private final ReservationRepository reservationRepository;

	private final TourRepository tourRepository;

	private final CustomerRepository customerRepository;

	public BestTravelApplication(HotelRepository hotelRepository, FlyRepository flyRepository,
			TicketRepository ticketRepository, ReservationRepository reservationRepository,
			TourRepository tourRepository, CustomerRepository customerRepository) {
	
		this.hotelRepository = hotelRepository;
		this.flyRepository = flyRepository;
		this.ticketRepository = ticketRepository;
		this.reservationRepository = reservationRepository;
		this.tourRepository = tourRepository;
		this.customerRepository = customerRepository;
	}



	public static void main(String[] args) {
		SpringApplication.run(BestTravelApplication.class, args);
	}



	@Override
	public void run(String... args) throws Exception {
		/* var fly = flyRepository.findById(15L).get();

		var hotel = hotelRepository.findById(7L).get();

		var ticket = ticketRepository.findById(UUID.fromString("12345678-1234-5678-2236-567812345678")).get();

		var reservation = reservationRepository.findById(UUID.fromString("12345678-1234-5678-1234-567812345678")).get();

		var customer = customerRepository.findById("BBMB771012HMCRR022").get();

		log.info(String.valueOf(fly));
		log.info(String.valueOf(hotel));
		log.info(String.valueOf(ticket));
		log.info(String.valueOf(reservation));
		log.info(String.valueOf(customer)); */

		/* this.flyRepository.selectLessPrice(BigDecimal.valueOf(20)).forEach(f -> System.out.println(f));
		this.flyRepository.selectBetweenPrices(BigDecimal.valueOf(10), BigDecimal.valueOf(15)).forEach(System.out::println);
		this.flyRepository.selectOriginDestiny("Grecia", "Mexico").forEach(System.out::println);
 */



		var customer = customerRepository.findById("VIKI771012HMCRG093").orElseThrow();

		log.info("Client Name: " + customer.getFullName());

		var fly = flyRepository.findById(11L).orElseThrow();
		log.info("Origin: "+ fly.getOriginName() + " - Destiny: "+ fly.getDestinyName());
		var tour = TourEntity.builder().customer(customer).build();
		var hotel = hotelRepository.findById(3L).orElseThrow();
		log.info("Hotel: "+ hotel.getName());

		var ticket = TicketEntity.builder()
			.id(UUID.randomUUID())
			.price(fly.getPrice().multiply(BigDecimal.TEN))
			.arrivalDate(LocalDate.now())
			.departureDate(LocalDate.now())
			.purchaseDate(LocalDate.now())
			.customer(customer)
			.tour(tour)
			.fly(fly)
			.build();


		var reservation = ReservationEntity.builder()
		.id(UUID.randomUUID())
		.dateTimeReservation(LocalDateTime.now())
		.dateEnd(LocalDate.now().plusDays(2))
		.dateStart(LocalDate.now().plusDays(1))
		.hotel(hotel)
		.customer(customer)
		.tour(tour)
		.totalDays(1)
		.price(hotel.getPrice().multiply(BigDecimal.TEN))
		.build();

		tour.addReservation(reservation);
		tour.updateReservation();

		tour.addTicket(ticket);

		var tourSaved = this.tourRepository.save(tour);

		this.tourRepository.deleteById(tourSaved.getId());


		
	}



}
