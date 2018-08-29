package com.slalom.example.controller;

import com.slalom.example.domain.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.slalom.example.domain.User;
import com.slalom.example.repository.UserRepository;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
  @Autowired
  private UserRepository userRepository;

  @RequestMapping(value="/{username}", method=RequestMethod.GET)
  public HttpEntity<Object> findByUsername(@PathVariable("username") final String username){
    if(!(username.length() >= 6 && username.length() <= 100)){
        return new ResponseEntity<>(new ApiResponse(400, "BAD REQUEST", "Invalid Username"), HttpStatus.BAD_REQUEST);
    }
    User user = userRepository.findByUsername(username);
    if(null == user){
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<>(user, HttpStatus.OK);
  }

  @RequestMapping(method = RequestMethod.POST)
  public HttpEntity<Object> createUser(@Valid @RequestBody final User user, @RequestHeader("api_key") String apiKey){
      if(!apiKey.equals("special-key")){
          return new ResponseEntity<>(new ApiResponse(401, "Unauthorized", "Need Authorization"), HttpStatus.UNAUTHORIZED);
      }
      User newUser = userRepository.save(user);
      return new ResponseEntity<>(newUser, HttpStatus.CREATED);
  }

  @RequestMapping(value="/{username}", method=RequestMethod.DELETE)
  public HttpEntity<Object> deleteByUsername(@PathVariable("username") final String username, @RequestHeader("api_key") String apiKey){
      if(!apiKey.equals("special-key")){
          return new ResponseEntity<>(new ApiResponse(401, "Unauthorized", "Need Authorization"), HttpStatus.UNAUTHORIZED);
      }
      if(!(username.length() >= 6 && username.length() <= 100)){
          return new ResponseEntity<>(new ApiResponse(400, "BAD REQUEST", "Invalid Username"), HttpStatus.BAD_REQUEST);
      }
      if(userRepository.findByUsername(username) == null){
          return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
      userRepository.deleteByUsername(username);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @RequestMapping(value="/{username}", method=RequestMethod.PUT)
  public HttpEntity<Object> updateUser(@PathVariable("username") final String username, @Valid @RequestBody final User user, @RequestHeader("api_key") String apiKey){
      if(!apiKey.equals("special-key")){
          return new ResponseEntity<>(new ApiResponse(401, "Unauthorized", "Need Authorization"), HttpStatus.UNAUTHORIZED);
      }
      if(!(username.length() >= 6 && username.length() <= 100)){
          return new ResponseEntity<>(new ApiResponse(400, "BAD REQUEST", "Invalid Username"), HttpStatus.BAD_REQUEST);
      }
      User curUser = userRepository.findByUsername(username);
      if(null == curUser){
          return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
      if(user.getUsername() != null){
          curUser.setUsername(user.getUsername());
      }
      if(user.getFirstName() != null){
          curUser.setFirstName(user.getFirstName());
      }
      if(user.getLastName() != null){
          curUser.setLastName(user.getLastName());
      }
      if(user.getEmail() != null){
          curUser.setEmail(user.getEmail());
      }
      if(user.getPassword() != null){
          curUser.setPassword(user.getPassword());
      }
      if(user.getPhone() != null){
          curUser.setPhone(user.getPhone());
      }
      curUser.setUserStatus(user.getUserStatus());
      curUser = userRepository.save(curUser);
      return new ResponseEntity<>(curUser, HttpStatus.OK);
  }

  /**
   * Creating Database Queries From Method Names
   */
  @RequestMapping(value="/jpa-research/method-name", method=RequestMethod.GET)
  public HttpEntity<Object> jpaResearchByMethodName(final String firstName, final String lastName){
      List<User> users = userRepository.findByFirstNameOrLastName(firstName, lastName);
      if(users == null || users.size() == 0){
          return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
      return new ResponseEntity<>(users, HttpStatus.OK);
  }

  @RequestMapping(value="/jpa-research/method-name2", method=RequestMethod.GET)
  public HttpEntity<Object> jpaResearchByMethodName2(final String firstName, final String lastName){
      Long count = userRepository.countByFirstNameOrLastName(firstName, lastName);
      if(count == null){
          return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
      return new ResponseEntity<>(count, HttpStatus.OK);
  }

  @RequestMapping(value="/jpa-research/method-name3", method=RequestMethod.GET)
  public HttpEntity<Object> jpaResearchByMethodName3(final String firstName, final String lastName){
      List<User> users = userRepository.findDistinctByFirstNameOrLastName(firstName, lastName);
      if(users == null || users.size() == 0){
          return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
      return new ResponseEntity<>(users, HttpStatus.OK);
  }

  /**
   * Creating Database Queries With the @Query Annotation
   */
  @RequestMapping(value="/jpa-research/query-annotation", method=RequestMethod.GET)
  public HttpEntity<Object> jpaResearchByQueryAnnotation(){
      List<User> users = userRepository.searchByPhonePart();
      if(users == null || users.size() == 0){
          return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
      return new ResponseEntity<>(users, HttpStatus.OK);
  }

  @RequestMapping(value="/jpa-research/query-annotation2", method=RequestMethod.GET)
  public HttpEntity<Object> jpaResearchByQueryAnnotation(final String searchTerm){
      List<User> users = userRepository.searchByUsernamePart(searchTerm);
      if(users == null || users.size() == 0){
          return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
      return new ResponseEntity<>(users, HttpStatus.OK);
  }

  /**
   * Creating Database Queries With Named Queries
   */
  @RequestMapping(value="/jpa-research/named-query", method=RequestMethod.GET)
  public HttpEntity<Object> jpaResearchWithNamedQueries(){
      List<User> users = userRepository.findBySearchTermNamedNative();
      if(users == null || users.size() == 0){
          return new ResponseEntity<>(HttpStatus.NOT_FOUND);
      }
      return new ResponseEntity<>(users, HttpStatus.OK);
  }

    @RequestMapping(value="/jpa-research/named-query2", method=RequestMethod.GET)
    public HttpEntity<Object> jpaResearchWithNamedQueries2(final String searchTerm){
        List<User> users = userRepository.findBySearchTermNamed(searchTerm);
        if(users == null || users.size() == 0){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
