package com.disi.travelpoints.service;

import com.disi.travelpoints.dto.visit.ReviewDTO;
import com.disi.travelpoints.dto.visit.VisitDTO;
import com.disi.travelpoints.dto.visit.VisitMapper;
import com.disi.travelpoints.model.entity.TravelUser;
import com.disi.travelpoints.model.entity.Visit;
import com.disi.travelpoints.repository.TravelUserRepository;
import com.disi.travelpoints.repository.VisitRepository;
import com.disi.travelpoints.service.exception.ConstraintViolationException;
import com.disi.travelpoints.service.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class VisitService {
    private final VisitRepository visitRepository;
    private final TravelUserRepository travelUserRepository;

    public void createNewVisit(VisitDTO visitDTO, TravelUser user) {
        TravelUser travelUser = travelUserRepository.findByEmail(user.getEmail()).orElseThrow(()->new ResourceNotFoundException("user","no user found", user.getEmail()));
        visitDTO.setTravelUserId(travelUser.getId());
        LocalDate date = visitDTO.getTimestamp().toLocalDateTime().toLocalDate();
        LocalDateTime localDateTime = date.atTime(visitDTO.getHour(),0);
        visitDTO.setTimestamp(Timestamp.valueOf(localDateTime));
        Visit visit = VisitMapper.toEntity(visitDTO);
        try {
            visitRepository.save(visit);
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            throw new ConstraintViolationException("An error occurred while attempting to create a new visit:" +
                    dataIntegrityViolationException.getMessage());
        }
    }

    public List<ReviewDTO> getReviewsByTouristicAttraction(UUID touristicAttractionId) {
        List<Visit> visits = visitRepository.findAllByTouristicAttractionId(touristicAttractionId);
        return visits.stream().map(v -> VisitMapper.toReviewDTO(v)).collect(Collectors.toList());
    }
}
