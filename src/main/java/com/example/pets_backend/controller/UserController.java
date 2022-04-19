package com.example.pets_backend.controller;

import com.example.pets_backend.entity.CalendarDate;
import com.example.pets_backend.entity.Pet;
import com.example.pets_backend.entity.User;
import com.example.pets_backend.service.PetService;
import com.example.pets_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.example.pets_backend.ConstantValues.*;

@RestController
@RequiredArgsConstructor
@RequestMapping
@Slf4j
public class UserController {

    private final UserService userService;
    private final PetService petService;

    @PostMapping(REGISTER)
    public LinkedHashMap<String, Object> register(@RequestBody Map<String, Object> mapIn) {
        User user = new User((String) mapIn.get("email"), (String) mapIn.get("password"), (String) mapIn.get("firstName"), (String) mapIn.get("lastName"));
        User savedUser = userService.save(user);
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        map.put("uid", savedUser.getUid());
        return map;
    }

    @DeleteMapping("/user/delete")
    public void deleteUser(@RequestBody Map<String, Object> mapIn) {
        userService.deleteById((String) mapIn.get("uid"));
    }

    @PostMapping("/user/all")
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @PostMapping("/user/dashboard")
    public LinkedHashMap<String, Object> getUserDashboard(@RequestBody Map<String, Object> mapIn) {
        User user = userService.getById((String) mapIn.get("uid"));
        LinkedHashMap<String, Object> mapOut = new LinkedHashMap<>();
        mapOut.put("firstName", user.getFirstName());
        mapOut.put("lastName", user.getLastName());
        mapOut.put("image", user.getImage());
        mapOut.put("petList", user.getPetListAb());
        mapOut.put("folderList", user.getFolderListAb());
        if (user.getCalendarDateList().size() != 0) {
            //TODO: need to get today's calendarDate object
            CalendarDate calendarDate = user.getCalendarDateList().get(0);
            mapOut.put("calendarDate", calendarDate.getCalDateAb());
        } else {
            mapOut.put("calendarDate", null);
        }
        return mapOut;
    }

    @PostMapping("/user/profile")
    public LinkedHashMap<String, Object> getUserProfile(@RequestBody Map<String, Object> mapIn) {
        User user = userService.getById((String) mapIn.get("uid"));
        LinkedHashMap<String, Object> mapOut = new LinkedHashMap<>();
        mapOut.put("firstName", user.getFirstName());
        mapOut.put("lastName", user.getLastName());
        mapOut.put("image", user.getImage());
        mapOut.put("email", user.getEmail());
        mapOut.put("phone", user.getPhone());
        mapOut.put("address", user.getAddress());
        mapOut.put("isPetSitter", user.isPetSitter());
        mapOut.put("petList", user.getPetListAb());
        return mapOut;
    }

    @PostMapping("/user/profile/update")
    @Transactional
    public void updateUserProfile(@RequestBody Map<String, Object> mapIn) {
        User user = userService.getById((String) mapIn.get("uid"));
        user.setFirstName((String) mapIn.get("firstName"));
        user.setLastName((String) mapIn.get("lastName"));
        user.setPhone((String) mapIn.get("phone"));
        user.setAddress((String) mapIn.get("address"));
        user.setPetSitter((boolean) mapIn.get("isPetSitter"));
    }

    @PostMapping("/user/profile/image/update")
    @Transactional
    public void updateUserProfileImage(@RequestBody Map<String, Object> mapIn) {
        User user = userService.getById((String) mapIn.get("uid"));
        user.setImage((String) mapIn.get("image"));
    }

    @PostMapping("/user/pet/add")
    @Transactional
    public LinkedHashMap<String, Object> addPet(@RequestBody Map<String, Object> mapIn) {
        User user = userService.getById((String) mapIn.get("uid"));
        String petAvatar = (String) mapIn.get("petAvatar");
        if (petAvatar == null || petAvatar.length() == 0) petAvatar = DEFAULT_IMAGE_PET;
        Pet pet = new Pet(user, (String) mapIn.get("petName"), petAvatar,
                (String) mapIn.get("species"), (String) mapIn.get("breed"), (int) mapIn.get("gender"), (String) mapIn.get("petDob"));
        if (mapIn.containsKey("weight")) pet.setWeight((int) mapIn.get("weight"));
        if (mapIn.containsKey("height")) pet.setHeight((int) mapIn.get("height"));
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        Pet savedPet = petService.save(pet);
        map.put("petId", savedPet.getPetId());
        return map;
    }

    @PostMapping("/user/pet/profile")
    public LinkedHashMap<String, Object> getPet(@RequestBody Map<String, Object> mapIn) {
        Pet pet = petService.findByPetId((String) mapIn.get("petId"));
        String uid = (String) mapIn.get("uid");
        if (!uid.equals(pet.getUser().getUid())) {
            log.error("Pet {} does not belongs to user {}", pet.getPetId(), mapIn.get("uid"));
            throw new IllegalArgumentException("Pet " + pet.getPetId() + " does not belongs to user " + mapIn.get("uid"));
        }
        LinkedHashMap<String, Object> mapOut = new LinkedHashMap<>();
        mapOut.put("petName", pet.getPetName());
        mapOut.put("petAvatar", pet.getPetAvatar());
        mapOut.put("gender", pet.getGender());
        mapOut.put("petDob", pet.getPetDob());
        mapOut.put("species", pet.getSpecies());
        mapOut.put("breed", pet.getBreed());
        mapOut.put("weight", pet.getWeight());
        mapOut.put("height", pet.getHeight());
        return mapOut;
    }

    @PostMapping("/user/pet/profile/update")
    @Transactional
    public void updatePet(@RequestBody Map<String, Object> mapIn) {
        Pet pet = petService.findByPetId((String) mapIn.get("petId"));
        if (!mapIn.get("uid").equals(pet.getUser().getUid())) {
            log.error("Pet {} does not belongs to user {}", pet.getPetId(), mapIn.get("uid"));
            throw new IllegalArgumentException("Pet " + pet.getPetId() + " does not belongs to user " + mapIn.get("uid"));
        }
        pet.setPetName((String) mapIn.get("petName"));
        pet.setPetAvatar((String) mapIn.get("petAvatar"));
        pet.setGender((int) mapIn.get("gender"));
        pet.setSpecies((String) mapIn.get("species"));
        pet.setBreed((String) mapIn.get("breed"));
        pet.setPetDob((String) mapIn.get("petDob"));
        if (mapIn.containsKey("weight")) pet.setWeight((int) mapIn.get("weight"));
        if (mapIn.containsKey("height")) pet.setHeight((int) mapIn.get("height"));
    }

    @DeleteMapping("/user/pet/delete")
    public void deletePet(@RequestBody Map<String, Object> mapIn) {
        String petId = (String) mapIn.get("petId");
        Pet pet = petService.findByPetId(petId);
        if (!mapIn.get("uid").equals(pet.getUser().getUid())) {
            log.error("Pet {} does not belongs to user {}", pet.getPetId(), mapIn.get("uid"));
            throw new IllegalArgumentException("Pet " + pet.getPetId() + " does not belongs to user " + mapIn.get("uid"));
        }
        petService.deleteByPetId(petId);
    }

}
