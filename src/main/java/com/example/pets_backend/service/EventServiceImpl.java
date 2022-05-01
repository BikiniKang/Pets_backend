package com.example.pets_backend.service;

import com.example.pets_backend.entity.Event;
import com.example.pets_backend.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class EventServiceImpl implements EventService{

    private final EventRepository eventRepository;

    @Override
    public Event save(Event event) {
        log.info("New event saved into database");
        return eventRepository.save(event);
    }

    @Override
    public Event findByEventId(String eventId) {
        Event event = eventRepository.findByEventId(eventId);
        if (event == null) {
            log.error("Event {} not found in the database", eventId);
            throw new IllegalArgumentException("Event " + eventId + " not found in database");
        } else {
            log.info("Event {} found in the database", eventId);
            return event;
        }
    }

    @Override
    public void deleteByEventId(String eventId) {
        Event event = eventRepository.findByEventId(eventId);
        if (event == null) {
            log.error("Event {} not found in the database", eventId);
            throw new IllegalArgumentException("Event " + eventId + " not found in database");
        } else {
            log.info("Event {} found in the database", eventId);
        }
        eventRepository.deleteById(eventId);
    }

}
