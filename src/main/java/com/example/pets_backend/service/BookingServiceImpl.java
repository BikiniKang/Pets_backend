package com.example.pets_backend.service;


import com.example.pets_backend.entity.Booking;
import com.example.pets_backend.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BookingServiceImpl implements BookingService{

    private final BookingRepository bookingRepository;


    @Override
    public Booking save(Booking booking) {
        booking = bookingRepository.save(booking);
        log.info("New booking '{}' saved into database", booking.getBooking_id());
        return booking;
    }

    @Override
    public Booking findById(String booking_id) {
        Optional<Booking> booking = bookingRepository.findById(booking_id);
        if (booking.isPresent()) {
            log.info("Booking '{}' found in database", booking_id);
            return booking.get();
        } else {
            log.info("Booking '{}' not found in database", booking_id);
            throw new EntityNotFoundException("Booking " + booking_id + " not found in database");
        }
    }
}
