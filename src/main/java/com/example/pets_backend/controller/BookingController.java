package com.example.pets_backend.controller;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.example.pets_backend.entity.Booking;
import com.example.pets_backend.entity.User;
import com.example.pets_backend.service.BookingService;
import com.example.pets_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static com.example.pets_backend.ConstantValues.TEMPLATE_BOOKING_CONFIRM;
import static com.example.pets_backend.ConstantValues.TEMPLATE_BOOKING_INVITE;

@RestController
@RequiredArgsConstructor
@RequestMapping
@Slf4j
public class BookingController {

    private final BookingService bookingService;
    private final UserService userService;

    @PostMapping("/user/booking/invite")
    public Booking inviteBooking(@RequestBody Booking booking) {
        booking.setBooking_id(NanoIdUtils.randomNanoId());
        booking.setUser(userService.findByUid(booking.getUid()));
        booking = bookingService.save(booking);
        bookingService.sendEmail(booking, TEMPLATE_BOOKING_INVITE);
        return booking;
    }

    @Transactional
    @PostMapping("/user/booking/confirm")
    public Booking confirmBooking(@RequestBody Map<String, String> mapIn) {
        String booking_id = mapIn.get("booking_id");
        Booking booking = bookingService.findById(booking_id);
        booking.setStatus("confirmed");
        bookingService.sendEmail(booking, TEMPLATE_BOOKING_CONFIRM);
        return booking;
    }

    @Transactional
    @PostMapping("/user/booking/reject")
    public Booking rejectBooking(@RequestBody String booking_id) {
        Booking booking = bookingService.findById(booking_id);
        booking.setStatus("rejected");
        /*
        send reject email
         */
        return booking;
    }

    @Transactional
    @PostMapping("/user/booking/cancel")
    public Booking cancelBooking(@RequestBody String booking_id) {
        Booking booking = bookingService.findById(booking_id);
        booking.setStatus("cancelled");
        /*
        send cancel email
         */
        return booking;
    }

    @PostMapping("/user/booking/get/by_id")
    public Booking get1Booking(@RequestBody String booking_id) {
        return bookingService.findById(booking_id);
    }

    @PostMapping("/user/booking/get")
    public List<Booking> getBookings(@RequestBody String uid) {
        User user = userService.findByUid(uid);
        return user.getBookingList()
                .stream()
                .filter(b -> b.getStatus().equals("pending") || b.getStatus().equals("confirmed"))
                .toList();
    }
}
