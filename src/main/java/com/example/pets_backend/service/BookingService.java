package com.example.pets_backend.service;


import com.example.pets_backend.entity.Booking;

public interface BookingService {
    Booking save(Booking booking);

    Booking findById(String booking_id);

    void sendInviteEmail(Booking booking);
}
