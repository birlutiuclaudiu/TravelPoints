package com.disi.travelpoints.controller;

import com.disi.travelpoints.dto.httpresponse.PostResponseDTO;
import com.disi.travelpoints.dto.visit.ReviewDTO;
import com.disi.travelpoints.dto.visit.VisitDTO;
import com.disi.travelpoints.model.entity.TravelUser;
import com.disi.travelpoints.service.VisitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/visit")
public class VisitController {
    private final VisitService visitService;

    @PostMapping
    @PreAuthorize("hasAuthority('TOURIST')")
    public ResponseEntity<PostResponseDTO> createVisit(@Validated @RequestBody VisitDTO visitDTO, Authentication authentication) {
        TravelUser user = (TravelUser) authentication.getPrincipal();
        visitService.createNewVisit(visitDTO, user);
        return ResponseEntity.status(HttpStatus.CREATED).body(new PostResponseDTO("Success"));
    }

    @GetMapping("/{touristicAttractionId}/reviews")
    public List<ReviewDTO> getReviewsByTouristicAttraction(@PathVariable UUID touristicAttractionId) {
       return visitService.getReviewsByTouristicAttraction(touristicAttractionId);
    }


}
