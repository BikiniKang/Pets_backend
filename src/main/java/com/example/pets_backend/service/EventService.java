package com.example.pets_backend.service;


import com.example.pets_backend.entity.Event;

public interface EventService {
    Event save(Event event);

    Event findByEventId(String eventId);

    void deleteByEventId(String eventId);

    Event editEvent(String eventId, Event eventNew);
}
