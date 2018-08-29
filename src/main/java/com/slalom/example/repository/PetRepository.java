package com.slalom.example.repository;

import com.slalom.example.domain.Pet;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PetRepository extends Repository<Pet, Long> {
    Pet findById(Long id);

    Pet save(Pet pet);

    List<Pet> findByStatusIn(String[] status);

    @Transactional
    Long deleteById(Long id);
}
