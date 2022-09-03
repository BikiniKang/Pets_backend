package com.example.pets_backend.service;


import com.example.pets_backend.entity.Booking;
import com.example.pets_backend.entity.User;
import com.example.pets_backend.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.*;

import static com.example.pets_backend.ConstantValues.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BookingServiceImpl implements BookingService{

    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final SendMailService sendMailService;
    private final PetService petService;
    private final SchedulerService schedulerService;

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

    @Override
    public void sendEmail(Booking booking, String template) {
        Map<String, String> model = basicModel(booking);
        switch (template) {
            case TEMPLATE_BOOKING_INVITE:
                model.put("accept_link", WEB_PREFIX + "user/booking/accept_page/" + booking.getBooking_id());
                model.put("reject_link", WEB_PREFIX + "user/booking/reject_page/" + booking.getBooking_id());
                break;
            case TEMPLATE_BOOKING_CONFIRM:
                User invitee = userService.findByEmail(booking.getAttendee());
                if (invitee == null) {
                    model.put("invitee", booking.getAttendee());
                    model.put("avatar_invitee", DEFAULT_IMAGE);
                } else {
                    model.put("invitee", invitee.getFirstName() + " " + invitee.getLastName());
                    model.put("avatar_invitee", invitee.getImage());
                }
                model.put("cancel_link", WEB_PREFIX + "cancel?booking_id=" + booking.getBooking_id());
                break;
            case TEMPLATE_BOOKING_CANCEL:
                break;
            default:
                throw new IllegalArgumentException("Template " + template + " not found");
        }
        schedulerService.addJobToScheduler(null, new Runnable() {
            @Override
            public void run() {
                try {
                    sendMailService.sendEmail(booking.getAttendee(), model, template);
                } catch (Exception e) {
                    // todo: handle exception (i.e., notify user that email sending failed)
                    e.printStackTrace();
                }
            }
        }, LocalDateTime.now());
    }

    @Override
    public Booking findByPairBkId(String pair_bk_id) {
        Optional<Booking> booking = bookingRepository.findByPair_bk_id(pair_bk_id);
        if (booking.isPresent()) {
            log.info("Booking with pair_bk_id '{}' found in database", pair_bk_id);
            return booking.get();
        } else {
            log.info("Booking with pair_bk_id '{}' not found in database", pair_bk_id);
            return null;
        }
    }

    private Map<String, String> basicModel(Booking booking) {
        Map<String, String> model = new HashMap<>();
        User user = booking.getUser();
        model.put("sender", user.getFirstName() + " " + user.getLastName());
        model.put("avatar_sender", user.getImage());
        model.put("title", booking.getTitle());
        String start_time = booking.getStart_time();
        String end_time = booking.getEnd_time();
        if (start_time.substring(0, 10).equals(end_time.substring(0, 10))) {
            // if start & end are in the same day, use format "yyyy-MM-dd HH:mm - HH:mm"
            model.put("time_range", start_time + "-" + end_time.substring(11));
        } else {
            // if start & end are in different days, use format "yyyy-MM-dd HH:mm - yyyy-MM-dd HH:mm"
            model.put("time_range", start_time + " - " + end_time);
        }
        model.put("location", booking.getLocation());
        List<String> petNameList = new ArrayList<>();
        for (String petId: booking.getPet_id_list()) {
            petNameList.add(petService.findByPetId(petId).getPetName());
        }
        model.put("pets", String.join(", ", petNameList));
        model.put("description", booking.getDescription());
        return model;
    }

}
