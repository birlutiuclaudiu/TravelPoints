package com.disi.travelpoints.controller;

import com.disi.travelpoints.dto.TouristicAttractionDTO;
import com.disi.travelpoints.dto.httpresponse.PostResponseDTO;
import com.disi.travelpoints.dto.visit.TouristicAttractionVisitDTO;
import com.disi.travelpoints.model.entity.TravelUser;
import com.disi.travelpoints.model.enums.Category;
import com.disi.travelpoints.service.TouristicAttractionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/touristic-attractions")
public class TouristicAttractionController {
    private final TouristicAttractionService touristicAttractionService;

    @GetMapping
    public ResponseEntity<List<TouristicAttractionDTO>> getTouristicAttractions(@RequestParam(required = false) String location,
                                                                                @RequestParam(required = false) Category category,
                                                                                Authentication authentication) {
        TravelUser travelUser = (TravelUser) authentication.getPrincipal();
        List<TouristicAttractionDTO> touristicAttractionDTOS = touristicAttractionService.getTouristicAttractions(location, category, travelUser);
        return ResponseEntity.ok(touristicAttractionDTOS);
    }

    @PostMapping
    public TouristicAttractionDTO addTouristicAttraction(@RequestBody TouristicAttractionDTO touristicAttractionDTO) {
        return touristicAttractionService.addTouristicAttraction(touristicAttractionDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTouristicAttractionById(@PathVariable UUID id) {
        touristicAttractionService.deleteTouristicAttractionById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public TouristicAttractionDTO modifyTouristicAttractionById(@PathVariable UUID id, @RequestBody TouristicAttractionDTO touristicAttractionDTO) {
        return touristicAttractionService.modifyTouristicAttractionById(id, touristicAttractionDTO);
    }

    @GetMapping("/{id}")
    public TouristicAttractionDTO getTouristicAttractionById(@PathVariable UUID id) {
        return touristicAttractionService.getTouristicAttractionDTOById(id);
    }

    @GetMapping("/most-visited")
    public List<TouristicAttractionVisitDTO> getMostVisitedTouristicAttractions() {
        return touristicAttractionService.getMostVisitedTouristicAttractions();
    }

    @GetMapping("/day-frequency-visits/{touristicAttractionId}/{date}")
    public HashMap<Integer, Integer> getDayFrequencyVisitsForTouristicAttraction(@PathVariable UUID touristicAttractionId,
                                                                                 @PathVariable Date date) {
        Timestamp timestamp = new Timestamp(date.getTime());
        return touristicAttractionService.getDayFrequencyVisitsForTouristicAttraction(touristicAttractionId, timestamp);
    }

    @GetMapping("/month-frequency-visits/{touristicAttractionId}/{year}")
    public HashMap<String, Integer> getMonthFrequencyVisitsForTouristicAttraction(@PathVariable UUID touristicAttractionId,
                                                                                  @PathVariable Date year) {
        Timestamp timestamp = new Timestamp(year.getTime());
        return touristicAttractionService.getMonthFrequencyVisitsForTouristicAttraction(touristicAttractionId, timestamp);
    }

    @PostMapping("/wishlist/{touristicAttractionId}")
    public ResponseEntity<PostResponseDTO> addToWishlist(@PathVariable UUID touristicAttractionId, Authentication authentication) {
        TravelUser travelUser = (TravelUser) authentication.getPrincipal();
        touristicAttractionService.addTouristicAttractionToWishlist(touristicAttractionId, travelUser.getEmail());
        return ResponseEntity.ok(new PostResponseDTO("Success"));
    }

    @GetMapping("/wishlist")
    public ResponseEntity<List<TouristicAttractionDTO>> getWishlist(Authentication authentication) {
        TravelUser travelUser = (TravelUser) authentication.getPrincipal();
        List<TouristicAttractionDTO> l = touristicAttractionService.getWishlist(travelUser);
        return ResponseEntity.ok(l);
    }

    @DeleteMapping("/wishlist/{touristicAttractionId}")
    public ResponseEntity<PostResponseDTO> removeFromWishlist(@PathVariable UUID touristicAttractionId, Authentication authentication) {
        TravelUser travelUser = (TravelUser) authentication.getPrincipal();
        touristicAttractionService.deleteTouristicAttractionFromWishlist(touristicAttractionId, travelUser.getEmail());
        return ResponseEntity.ok(new PostResponseDTO("Success"));
    }
}
