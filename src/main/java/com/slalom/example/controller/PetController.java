package com.slalom.example.controller;

import com.slalom.example.domain.ApiResponse;
import com.slalom.example.domain.Pet;
import com.slalom.example.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/pet")
public class PetController {
    @Autowired
    private PetRepository petRepository;

    @RequestMapping(value="/{petId}", method= RequestMethod.GET)
    public HttpEntity<Object> findById(@PathVariable("petId") final Long petId){
        if(petId <= 0){
            return new ResponseEntity<>(new ApiResponse(400, "BAD REQUEST", "Pet ID must be positive Integer"), HttpStatus.BAD_REQUEST);
        }
        Pet pet = petRepository.findById(petId);
        if(null == pet){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(pet, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public HttpEntity<Object> createPet(@Valid @RequestBody final Pet pet){
        String namePattern = "^[a-zA-Z0-9_ ]{2,100}$";
        Pattern pattern = Pattern.compile(namePattern);
        if(!pattern.matcher(pet.getCategory().getName()).matches()){
            return new ResponseEntity<>(new ApiResponse(405, "INVALID INPUT", "Invalid Category Name"), HttpStatus.METHOD_NOT_ALLOWED);
        }
        Pet newPet = petRepository.save(pet);
        return new ResponseEntity<>(newPet, HttpStatus.CREATED);
    }

    @RequestMapping(value="/{petId}", method=RequestMethod.DELETE)
    public HttpEntity<Object> deleteById(@PathVariable("petId") final Long petId, @RequestHeader("api_key") String apiKey){
        if(!apiKey.equals("special-key")){
            return new ResponseEntity<>(new ApiResponse(401, "Unauthorized", "Need Authorization"), HttpStatus.UNAUTHORIZED);
        }
        if(petId <= 0){
            return new ResponseEntity<>(new ApiResponse(400, "BAD REQUEST", "Pet ID must be positive Integer"), HttpStatus.BAD_REQUEST);
        }
        if(petRepository.findById(petId) == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        petRepository.deleteById(petId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value="/{petId}", method=RequestMethod.PUT)
    public HttpEntity<Object> updatePet(@PathVariable("petId") final Long petId, @Valid @RequestBody final Pet pet){
        if(petId <= 0){
            return new ResponseEntity<>(new ApiResponse(400, "BAD REQUEST", "Pet ID must be positive Integer"), HttpStatus.BAD_REQUEST);
        }
        String namePattern = "^[a-zA-Z0-9_ ]{2,100}$";
        Pattern pattern = Pattern.compile(namePattern);
        if(!pattern.matcher(pet.getCategory().getName()).matches()){
            return new ResponseEntity<>(new ApiResponse(405, "INVALID INPUT", "Invalid Category Name"), HttpStatus.METHOD_NOT_ALLOWED);
        }
        String statusPattern = "^(available|pending|sold)$";
        pattern = Pattern.compile(statusPattern);
        if(!pattern.matcher(pet.getStatus()).matches()){
            return new ResponseEntity<>(new ApiResponse(405, "INVALID INPUT", "Invalid Status"), HttpStatus.METHOD_NOT_ALLOWED);
        }
        Pet curPet = petRepository.findById(petId);
        if(null == curPet){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if(pet.getCategory() != null){
            curPet.setCategory(pet.getCategory());
        }
        if(pet.getName() != null){
            curPet.setName(pet.getName());
        }
        if(pet.getTags() != null){
            curPet.setTags(pet.getTags());
        }
        if(pet.getStatus() != null){
            curPet.setStatus(pet.getStatus());
        }
        if(pet.getPhotoUrls() != null){
            curPet.setPhotoUrls(pet.getPhotoUrls());
        }
        curPet = petRepository.save(curPet);
        return new ResponseEntity<>(curPet, HttpStatus.OK);
    }

    @RequestMapping(method=RequestMethod.GET)
    public HttpEntity<Object> getPets(final String[] status){
        String statusPattern = "^(available|pending|sold)$";
        Pattern pattern = Pattern.compile(statusPattern);
        for(String statusItem : status){
            if(!pattern.matcher(statusItem).matches()){
                return new ResponseEntity<>(new ApiResponse(400, "BAD REQUEST", "Status can only be available, pending or sold"), HttpStatus.BAD_REQUEST);
            }
        }
        List<Pet> posts = petRepository.findByStatusIn(status);

        return new ResponseEntity<>(posts, HttpStatus.OK);
    }
}
