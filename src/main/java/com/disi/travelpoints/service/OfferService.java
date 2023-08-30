package com.disi.travelpoints.service;

import com.disi.travelpoints.dto.OfferDTO;
import com.disi.travelpoints.dto.OfferMapper;
import com.disi.travelpoints.model.entity.Offer;
import com.disi.travelpoints.model.entity.TouristicAttraction;
import com.disi.travelpoints.model.entity.TravelUser;
import com.disi.travelpoints.model.entity.Wishlist;
import com.disi.travelpoints.repository.OfferRepository;
import com.disi.travelpoints.repository.WishlistRepository;
import com.disi.travelpoints.service.mail.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.disi.travelpoints.service.Constant.WISHLIST_LINK;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class OfferService {
    private final OfferRepository offerRepository;
    private final OfferMapper offerMapper;
    private final WishlistRepository wishlistRepository;
    private final EmailService emailService;

    public OfferDTO addOffer(OfferDTO offerDTO, UUID touristicAttractionId) {
        Offer offerToAdd = offerMapper.toOfferEntity(offerDTO);
        TouristicAttraction touristicAttraction = new TouristicAttraction();
        touristicAttraction.setId(touristicAttractionId);
        offerToAdd.setTouristicAttraction(touristicAttraction);
        Offer savedOffer = offerRepository.save(offerToAdd);
        notifyUsersOnWishlist(touristicAttractionId);
        return offerMapper.toOfferDTO(savedOffer);
    }

    private void notifyUsersOnWishlist(UUID touristicAttractionId) {
        List<Wishlist> wishList = wishlistRepository.findByTouristicAttractionId(touristicAttractionId);
        for (Wishlist currentWish : wishList) {
            new Thread(() -> { // Lambda Expression
                TravelUser travelUser = currentWish.getUser();
                TouristicAttraction touristicAttraction = currentWish.getTouristicAttraction();
                String message = String.format("Dear %s, \n" +
                                " a new offer was added for the touristic attraction %s. Please check the wishlist page to view the latest offers. See: %s", travelUser.getFirstName(),
                        touristicAttraction.getName(), WISHLIST_LINK);
                emailService.sendSimpleMessage(travelUser.getEmail(), "New Offer", message);
            }).start();
        }
    }

    public List<OfferDTO> getAllOffersInOrderByTouristicAttractionId(UUID touristicAttractionId) {
        List<OfferDTO> offerDTOs = offerRepository.findByTouristicAttractionId(touristicAttractionId)
                .stream().map(p -> offerMapper.toOfferDTO(p))
                .collect(Collectors.toList());
        offerDTOs.sort(Comparator.comparing(OfferDTO::getStartDate).reversed());
        return offerDTOs;
    }

    public void deleteOfferById(UUID id) {
        offerRepository.deleteById(id);
    }
}
