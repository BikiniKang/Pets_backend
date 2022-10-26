package com.example.pets_backend.controller;

import com.example.pets_backend.entity.Pet;
import com.example.pets_backend.entity.User;
import com.example.pets_backend.service.PetService;
import com.example.pets_backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.example.pets_backend.ConstantValues.DEFAULT_IMAGE_PET;

@RestController
@RequiredArgsConstructor
@RequestMapping
@Slf4j
public class PetController {

    private final UserService userService;
    private final PetService petService;

    @PostMapping("/user/pet/add")
    public LinkedHashMap<String, Object> addPet(@RequestBody Map<String, Object> mapIn) {
        User user = userService.findByUid((String) mapIn.get("uid"));
        String petAvatar = (String) mapIn.get("petAvatar");
        if (petAvatar == null || petAvatar.length() == 0) petAvatar = DEFAULT_IMAGE_PET;
        Pet pet = new Pet(
                user,
                (String) mapIn.get("petName"),
                petAvatar,
                (String) mapIn.get("species"),
                (String) mapIn.get("breed"),
                (int) mapIn.get("gender"),
                (String) mapIn.get("petDob")
        );
        if (mapIn.containsKey("weight")) pet.setWeight((int) mapIn.get("weight"));
        if (mapIn.containsKey("height")) pet.setHeight((int) mapIn.get("height"));
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        Pet savedPet = petService.save(pet);
        map.put("petId", savedPet.getPetId());
        return map;
    }

    @PostMapping("/user/pet/profile")
    public LinkedHashMap<String, Object> getPetProfile(@RequestBody Map<String, Object> mapIn) {
        Pet pet = petService.findByPetId((String) mapIn.get("petId"));
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
    public void updatePetProfile(@RequestBody Map<String, Object> mapIn) {
        Pet pet = petService.findByPetId((String) mapIn.get("petId"));
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
            log.warn("Illegal Operation: Deleting pet '{}' that does not belong to the user '{}'", petId, mapIn.get("uid"));
            throw new IllegalArgumentException("Invalid Argument");
        }
        petService.deleteByPetId(petId);
    }

    @PostMapping("/user/pet/all")
    public List<LinkedHashMap<String, Object>> getPetList(@RequestBody Map<String, Object> mapIn) {
        User user = userService.findByUid((String) mapIn.get("uid"));
        return user.getPetAbList();
    }
}
