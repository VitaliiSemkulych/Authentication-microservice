package com.webbook.webbook.controllers;



import com.webbook.webbook.dto.UserRegisterDTO;
import com.webbook.webbook.exceptions.enrollExeption.BadActivationCodeException;
import com.webbook.webbook.exceptions.enrollExeption.UserAlreadyExistException;
import com.webbook.webbook.services.EnrollService;
import javassist.tools.web.BadHttpRequest;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;


@RestController
@AllArgsConstructor
public class RegistrationController {

    private final EnrollService enrollService;

//    @GetMapping("/enroll")
//    public Boolean isEmailExist(@RequestParam String email) {
//        return  enrollService.isEmailExist(email);
//    }

    @PostMapping("/enroll")
    public String enroll(@RequestBody Optional<UserRegisterDTO> userRegisterDTO) throws BadHttpRequest {
        var userRequest=userRegisterDTO.orElseThrow(BadHttpRequest::new);
        if(enrollService.isUserExist(userRequest.getUserEmail()))
            throw new UserAlreadyExistException(String.format("User with email: %s already enrolled.",userRequest.getUserEmail()));
        enrollService.enroll(userRequest);
        return "User: "+userRegisterDTO.get().getUserName()+" enrolled!";
    }

    @GetMapping("/activate")
    public String activate(@RequestParam String activationCode) throws BadActivationCodeException {
            enrollService.activateUser(activationCode);
        return "User activated";
    }




}
