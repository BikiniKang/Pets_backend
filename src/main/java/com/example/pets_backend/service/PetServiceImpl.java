package com.example.pets_backend.service;

import com.example.pets_backend.entity.Pet;
import com.example.pets_backend.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PetServiceImpl implements PetService{

    private final PetRepository petRepository;

    @Override
    public Pet save(Pet pet) {
        return petRepository.save(pet);
    }

    @Override
    public Pet findByPetId(Long petId) {
        return petRepository.findByPetId(petId);
    }
}
