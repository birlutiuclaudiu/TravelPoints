package com.disi.travelpoints.controller;

import com.disi.travelpoints.dto.OfferDTO;
import com.disi.travelpoints.service.OfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class OfferController {
    private final OfferService offerService;

    @PostMapping("/touristic-attractions/{id}/offers")
    public OfferDTO addOffer(@PathVariable UUID id, @RequestBody OfferDTO offerDTO) {
        return offerService.addOffer(offerDTO, id);
    }

    @GetMapping("/touristic-attractions/{id}/offers")
    public List<OfferDTO> getOffers(@PathVariable UUID id) {
        return offerService.getAllOffersInOrderByTouristicAttractionId(id);
    }
}
