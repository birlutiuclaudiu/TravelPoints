package com.disi.travelpoints.service;

import com.disi.travelpoints.dto.ResetPasswordDTO;
import com.disi.travelpoints.dto.authentication.InfoRegisterDTO;
import com.disi.travelpoints.dto.authentication.UserAuthMapper;
import com.disi.travelpoints.dto.energyuser.ClientInfoDTO;
import com.disi.travelpoints.dto.energyuser.ClientToEditDTO;
import com.disi.travelpoints.dto.energyuser.UserMapper;
import com.disi.travelpoints.model.entity.AccountTokenValidation;
import com.disi.travelpoints.model.entity.TravelUser;
import com.disi.travelpoints.model.enums.AccountStatus;
import com.disi.travelpoints.model.enums.TokenType;
import com.disi.travelpoints.model.enums.TravelUserRole;
import com.disi.travelpoints.repository.TokenValidationRepository;
import com.disi.travelpoints.repository.TravelUserRepository;
import com.disi.travelpoints.service.exception.ConstraintViolationException;
import com.disi.travelpoints.service.exception.InvalidValidationToken;
import com.disi.travelpoints.service.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final TravelUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenValidationRepository tokenValidationRepository;


    /**
     * This method is called to register a new energy user in application
     *
     * @return a string with the email
     */
    public String registerNewUser(InfoRegisterDTO infoRegisterDTO, TravelUserRole travelUserRole) {
        TravelUser travelUser = UserAuthMapper.toEntity(infoRegisterDTO, travelUserRole);
        travelUser.setUserPassword(passwordEncoder.encode(infoRegisterDTO.getPassword()));
        travelUser.setAccountStatus(AccountStatus.WAITING_FOR_CONFIRMATION);
        TravelUser savedUser;
        try {
            savedUser = userRepository.saveAndFlush(travelUser);
        } catch (DataIntegrityViolationException e) {
            log.error("Could not save the new client " + e.getMessage());
            throw new ConstraintViolationException("Could not save the new user account. The email provided is used by another account");
        }
        //generate token
        String token = UUID.randomUUID().toString();
        AccountTokenValidation accountTokenValidation = AccountTokenValidation.builder()
                .token(token).type(TokenType.ACCOUNT_VALIDATION)
                .expiryDate(LocalDateTime.now().plusMinutes(AccountTokenValidation.EXPIRATION))
                .travelUser(savedUser)
                .build();
        AccountTokenValidation savedAccountToken;
        try {
            savedAccountToken = tokenValidationRepository.saveAndFlush(accountTokenValidation);
        } catch (DataIntegrityViolationException e) {
            log.error("Could not save the new client " + e.getMessage());
            throw new ConstraintViolationException("Could not save the new user account. " +
                    "The email provided is used by another account");
        }
        return savedAccountToken.getToken();
    }

    public void validateAccount(String token) {
        AccountTokenValidation accountTokenValidation = tokenValidationRepository.findByToken(token).orElseThrow(() ->
                new InvalidValidationToken("There is no associated account in our application"));
        TravelUser travelUser = accountTokenValidation.getTravelUser();
        if (accountTokenValidation.getExpiryDate().isBefore(LocalDateTime.now())) {
            userRepository.delete(travelUser);
            throw new InvalidValidationToken("The link expired! Please retry the register process");
        }
        travelUser.setAccountStatus(AccountStatus.VALID);
        tokenValidationRepository.delete(accountTokenValidation);
    }

    public void generateNewPassword() {

    }

    /**
     * Implementation based on https://www.bezkoder.com/spring-boot-pagination-filter-jpa-pageable/
     *
     * @param pageSize   items / page
     * @param pageNumber the number of page to retrieve
     * @return a map with the objects necessary for react pagination
     */
    public Map<String, Object> fetchAllClients(int pageSize, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by("email"));
        Page<TravelUser> retrievedData = userRepository.findByRole(TravelUserRole.TOURIST, pageable);

        List<ClientInfoDTO> clientInfoDTOList = retrievedData.stream().map(UserMapper::clientToDTO).collect(Collectors.toList());
        Map<String, Object> response = new HashMap<>();
        response.put("energyUsers", clientInfoDTOList);
        response.put("currentPage", retrievedData.getNumber());
        response.put("totalItems", retrievedData.getTotalElements());
        response.put("totalPages", retrievedData.getTotalPages());

        return response;
    }

    public void updateClient(ClientToEditDTO clientToEditDTO) {
        TravelUser toUpdateUser = userRepository.findById(clientToEditDTO.getId()).orElseThrow(() ->
                new ResourceNotFoundException("User", "id", clientToEditDTO.getId().toString()));
        toUpdateUser.setEmail(clientToEditDTO.getEmail());
        toUpdateUser.setFirstName(clientToEditDTO.getFirstName());
        toUpdateUser.setLastName(clientToEditDTO.getLastName());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    }


    public String createTokenForPasswordValidation(ResetPasswordDTO resetPasswordDTO) {
        TravelUser travelUser = userRepository.findByEmail(resetPasswordDTO.getEmail()).orElseThrow(() ->
                new ResourceNotFoundException("User", "email", resetPasswordDTO.getEmail())
        );

        tokenValidationRepository.deleteByTravelUser(travelUser);
        //generate a token
        String token = UUID.randomUUID().toString();
        AccountTokenValidation accountTokenValidation = AccountTokenValidation.builder()
                .token(token).type(TokenType.RESET_PASSWORD)
                .expiryDate(LocalDateTime.now().plusMinutes(AccountTokenValidation.EXPIRATION))
                .travelUser(travelUser)
                .newPassword(passwordEncoder.encode(resetPasswordDTO.getPassword()))
                .build();
        AccountTokenValidation savedAccountToken;
        try {
            savedAccountToken = tokenValidationRepository.saveAndFlush(accountTokenValidation);
        } catch (DataIntegrityViolationException e) {
            log.error("Could not save the new client " + e.getMessage());
            throw new ConstraintViolationException("Could not save the new user account. " +
                    "The email provided is used by another account");
        }
        return savedAccountToken.getToken();


    }

    public void confirmResetPassword(String token) {
        AccountTokenValidation accountTokenValidation = tokenValidationRepository.findByToken(token).orElseThrow(() ->
                new InvalidValidationToken("There is no associated account in our application"));
        TravelUser travelUser = accountTokenValidation.getTravelUser();
        if (accountTokenValidation.getExpiryDate().isBefore(LocalDateTime.now())) {
            tokenValidationRepository.deleteByTravelUser(travelUser);
            throw new InvalidValidationToken("The link expired! Please retry the register process");
        }
        travelUser.setUserPassword(accountTokenValidation.getNewPassword());
        userRepository.save(travelUser);
        tokenValidationRepository.delete(accountTokenValidation);
    }
    public Optional<TravelUser> findUserByEmailAddress(String emailAddress) {
        return userRepository.findByEmail(emailAddress);
    }
}
