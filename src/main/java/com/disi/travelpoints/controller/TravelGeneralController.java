package com.disi.travelpoints.controller;

import com.disi.travelpoints.config.security.TokenProvider;
import com.disi.travelpoints.controller.constants.Constants;
import com.disi.travelpoints.dto.ResetPasswordDTO;
import com.disi.travelpoints.dto.authentication.CredentialsDTO;
import com.disi.travelpoints.dto.authentication.InfoRegisterDTO;
import com.disi.travelpoints.dto.authentication.ResponseDTO;
import com.disi.travelpoints.dto.httpresponse.PostResponseDTO;
import com.disi.travelpoints.model.entity.TravelUser;
import com.disi.travelpoints.model.enums.TravelUserRole;
import com.disi.travelpoints.service.UserService;
import com.disi.travelpoints.service.mail.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class TravelGeneralController {

    private final AuthenticationManager authManager;
    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final EmailService emailService;

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> login(@RequestBody @Valid CredentialsDTO credentials) throws Exception {

        doAuthentication(credentials.getEmail(), credentials.getPassword());

        String token = tokenProvider.provideToken((TravelUser) userService.loadUserByUsername(credentials.getEmail()));
        ResponseDTO responseDTO = ResponseDTO.builder().token(token).build();
        return ResponseEntity.ok(responseDTO);
    }

    @PostMapping("/register")
    public ResponseEntity<PostResponseDTO> register(@RequestBody @Valid InfoRegisterDTO infoRegisterDTO) {

        //create an account
        String token = userService.registerNewUser(infoRegisterDTO, TravelUserRole.TOURIST);
        //construct email
        String link = String.format(Constants.VALIDATION_LINK_FORMAT, token);
        try {
            emailService.sendSimpleMessage(infoRegisterDTO.getEmail(), "Validate account", link);
        } catch (Exception exc) {
            //delete account
        }

        PostResponseDTO postResponseDTO = new PostResponseDTO(Constants.SUCCESS_REGISTER);
        return ResponseEntity.status(HttpStatus.CREATED).body(postResponseDTO);
    }

    @GetMapping("/validate")
    public RedirectView register(@RequestParam String token) throws Exception {
        //create an account
        RedirectView redirectView;
        try {
            userService.validateAccount(token);
            redirectView = new RedirectView(Constants.LOGIN_LINK);
        } catch (RuntimeException invalidValidationToken) {
            redirectView = new RedirectView(Constants.REGISTER_LINK);
        }
        return redirectView;
    }

    @PostMapping("/resetpassword")
    public ResponseEntity<PostResponseDTO> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) {
        String token = userService.createTokenForPasswordValidation(resetPasswordDTO);

        PostResponseDTO responseDTO = new PostResponseDTO("An email for confirmation was sent to you!");
        String link = String.format(Constants.RESET_PASSWORD_FORMAT, token);
        try {
            emailService.sendSimpleMessage(resetPasswordDTO.getEmail(), "Reset password", link);
        } catch (Exception exc) {
            //delete account
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new PostResponseDTO("Could not be sent the email"));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping("reset-password/confirm")
    public RedirectView resetPasswordConfirmation(@RequestParam String token) throws Exception {
        //
        RedirectView redirectView;
        try {
            userService.confirmResetPassword(token);
            redirectView = new RedirectView(Constants.LOGIN_LINK);
        } catch (RuntimeException invalidValidationToken) {
            redirectView = new RedirectView(Constants.REGISTER_LINK);
        }
        return redirectView;
    }

    private void doAuthentication(String email, String password) throws Exception {
        try {
            authManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

}
