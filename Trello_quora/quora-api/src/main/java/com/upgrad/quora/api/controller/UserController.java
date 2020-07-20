package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.JwtTokenProvider;
import com.upgrad.quora.service.business.PasswordCryptographyProvider;
import com.upgrad.quora.service.business.UserService;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.SignOutRestrictedException;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")

public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordCryptographyProvider CryptographyProvider;
    //This controller method is called when the request pattern is of type 'question/all/{userId}' and also the incoming request is of GET type

    @RequestMapping(method = RequestMethod.POST, path = "/usersignup", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignupUserResponse> userSignup(final SignupUserRequest signupUserRequest) {

        final UserEntity userEntity = new UserEntity();

        userEntity.setUuid(UUID.randomUUID().toString());
        userEntity.setFirstName(signupUserRequest.getFirstName());
        userEntity.setLastName(signupUserRequest.getLastName());
        userEntity.setEmail(signupUserRequest.getEmailAddress());
        userEntity.setPassword(signupUserRequest.getPassword());
        userEntity.setContactNumber(signupUserRequest.getContactNumber());
        userEntity.setCountry(signupUserRequest.getCountry());
        userEntity.setAboutMe(signupUserRequest.getAboutMe());
        //userEntity.setDob(signupUserRequest.dob());
        userEntity.setSalt("1234abc");
        userEntity.setRole("nonadmin");

        final UserEntity createdUserEntity = userService.signup(userEntity);
        SignupUserResponse userResponse = new SignupUserResponse().id(createdUserEntity.getUuid()).status("USER SUCCESSFULLY REGISTERED");
        return new ResponseEntity<SignupUserResponse>(userResponse, HttpStatus.CREATED);
    }
    //This controller method is called when the request pattern is of type '/user/signin' and also the incoming request is of POST type

    @RequestMapping(value = "/user/signin", method = RequestMethod.POST)
    public ResponseEntity<SigninResponse> loginUser( @RequestHeader("Basic username:password") final String username, final String password) throws AuthenticationFailedException {
        UserEntity userEntity = userService.getUserByUsername(username);
        if (userEntity == null) {
            throw new AuthenticationFailedException("ATH-001", "This username does not exist");
        }
        final String encryptedPassword = CryptographyProvider.encrypt(password, userEntity.getSalt());
        if (!encryptedPassword.equals(userEntity.getPassword())) {
            throw new AuthenticationFailedException("ATH-002", "Password failed");
        } else {
            JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedPassword);
            UserAuthTokenEntity userAuthTokenEntity = new UserAuthTokenEntity();
            userAuthTokenEntity.setUser_id((int) userEntity.getId());
            final LocalDateTime now = LocalDateTime.now();
            final LocalDateTime expiresAt = now.plusHours(8);

            userAuthTokenEntity.setAccessToken(jwtTokenProvider.generateToken(userEntity.getUuid(), now, expiresAt));

            userAuthTokenEntity.setLoginAt(now);
            userAuthTokenEntity.setExpiresAt(expiresAt);

            userService.createAuthToken(userAuthTokenEntity);

            userService.updateUser(userEntity);
            final UserEntity signinUserEntity = userService.signin(userEntity);

            SigninResponse userResponse = new SigninResponse().id(signinUserEntity.getUuid()).message("SIGNED IN SUCCESSFULLY");
            return new ResponseEntity<SigninResponse>(userResponse, HttpStatus.OK);
        }

    }
    //This controller method is called when the request pattern is of type '/user/signout' and also the incoming request is of POST type

    @RequestMapping(value = "/user/signout", method = RequestMethod.POST)
    public ResponseEntity<SignoutResponse> signout( @RequestHeader("ACCESS_TOKEN") final String accessToken, UserEntity userEntity) throws SignOutRestrictedException {
        UserAuthTokenEntity user = userService.getAuthtoken(accessToken);
        if (user == null) {
            throw new SignOutRestrictedException("SGR-001", "User is not Signed in");
        }
         else {
            UserAuthTokenEntity userAuthTokenEntity = new UserAuthTokenEntity();
            userAuthTokenEntity.setUser_id((int) user.getId());
            final LocalDateTime now = LocalDateTime.now();


            userAuthTokenEntity.setLogoutAt(now);

            userService.createAuthToken(userAuthTokenEntity);


            final UserEntity signoutUserEntity = userService.signout(userEntity);

            SignoutResponse userResponse = new SignoutResponse().id(signoutUserEntity.getUuid()).message("SIGNED OUT SUCCESSFULLY");
            return new ResponseEntity < SignoutResponse > (userResponse,HttpStatus.CREATED);

        }
    }
}
