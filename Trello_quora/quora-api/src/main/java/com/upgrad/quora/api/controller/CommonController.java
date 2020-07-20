package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDetailsResponse;
import com.upgrad.quora.service.business.UserService;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/")
public class CommonController {

    @Autowired
    private UserService userService;


    @Autowired
    private UserEntity userEntity;
    @RequestMapping(value = "/userprofile/{userId}", method = RequestMethod.GET)
    public ResponseEntity<UserDetailsResponse> userProfile(@PathVariable("userId") final String id, final String authorization) throws UserNotFoundException, AuthorizationFailedException {

        UserAuthTokenEntity userAuthTokenEntity = userService.getAuthtoken(authorization);

        if (userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        String role = userEntity.getRole();

        UserEntity imageEntity = userService.getUserById(id);
        if (imageEntity == null) {
            throw new UserNotFoundException("USR-001", "User with entered uuid does not exist");
        } else {
            UserDetailsResponse user = new UserDetailsResponse().firstName(userEntity.getFirstName());
            user.lastName(userEntity.getLastName());
            user.userName(userEntity.getUsername());
            user.emailAddress(userEntity.getEmail());
            user.country(userEntity.getCountry());
            user.aboutMe(userEntity.getAboutMe());
            user.dob(String.valueOf(userEntity.getDob()));
            user.contactNumber(userEntity.getContactNumber());

            return new ResponseEntity<UserDetailsResponse>(user, HttpStatus.OK);


        }

    }
}
