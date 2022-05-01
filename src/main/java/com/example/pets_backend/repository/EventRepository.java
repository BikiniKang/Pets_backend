package com.example.pets_backend.repository;


import com.example.pets_backend.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, String> {
    Event findByEventId(String eventId);
}
