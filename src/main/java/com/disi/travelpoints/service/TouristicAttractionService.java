package com.disi.travelpoints.service;

import com.disi.travelpoints.dto.OfferDTO;
import com.disi.travelpoints.dto.OfferMapper;
import com.disi.travelpoints.dto.TouristicAttractionDTO;
import com.disi.travelpoints.dto.TouristicAttractionMapper;
import com.disi.travelpoints.dto.visit.TouristicAttractionVisitDTO;
import com.disi.travelpoints.model.entity.*;
import com.disi.travelpoints.model.enums.Category;
import com.disi.travelpoints.repository.OfferRepository;
import com.disi.travelpoints.repository.TouristicAttractionRepository;
import com.disi.travelpoints.repository.VisitRepository;
import com.disi.travelpoints.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class TouristicAttractionService {
    private final TouristicAttractionRepository touristicAttractionRepository;
    private final TouristicAttractionMapper touristicAttractionMapper;
    private final LocationService locationService;
    private final OfferRepository offerRepository;
    private final OfferMapper offerMapper;
    private final VisitRepository visitRepository;

    private final OfferService offerService;

    private final UserService userService;

    private final WishlistService wishlistService;

    private final WishlistRepository wishlistRepository;

    public List<TouristicAttractionDTO> getTouristicAttractions(String location, Category category, TravelUser travelUser) {
        List<TouristicAttraction> touristicAttractions;
        if (location != null && category != null) {
            touristicAttractions = touristicAttractionRepository.findByLocationAndCategory(location, category);
        } else if (location != null) {
            touristicAttractions = touristicAttractionRepository.findByLocation(location);
        } else if (category != null) {
            touristicAttractions = touristicAttractionRepository.findByCategory(category);
        } else {
            touristicAttractions = touristicAttractionRepository.findAll();
        }

        return touristicAttractions.stream().map(t-> getTouristicAttractionDTOByUser(t, travelUser)).toList();

    }

    private TouristicAttractionDTO getTouristicAttractionDTOByUser(TouristicAttraction touristicAttraction, TravelUser travelUser) {
        TouristicAttractionDTO touristicAttractionDTO = touristicAttractionMapper.touristicAttractionToDTO(touristicAttraction);
        Offer offer = offerRepository.findTopByTouristicAttractionOrderByEndDate(touristicAttraction).orElse(null);
        OfferDTO offerDTO = null;
        if (offer != null && offer.getEndDate().before(Timestamp.from(Instant.now()))) {
            offer = null;
        }
        if (offer != null) {
            offerDTO = offerMapper.toOfferDTO(offer);
        }
        touristicAttractionDTO.setLastOffer(offerDTO);
        Double review = visitRepository.findReview(touristicAttraction);
        if (review == null)
            review = 0.0;
        touristicAttractionDTO.setReview(review);

        Set<Wishlist> wishlistOfUser = wishlistRepository.findByTravelUser(travelUser);
        boolean isOnWishlist = false;
        for (Wishlist w : wishlistOfUser) {
            if (touristicAttraction.equals(w.getTouristicAttraction())) {
                isOnWishlist = true;
                break;
            }
        }
        touristicAttractionDTO.setOnWishlist(isOnWishlist);
        return touristicAttractionDTO;
    }

    public List<TouristicAttraction> getTouristicAttractionsEntities() {
        return touristicAttractionRepository.findAll();
    }

    private TouristicAttractionDTO getTouristicAttractionDTO(TouristicAttraction touristicAttraction) {
        TouristicAttractionDTO touristicAttractionDTO = touristicAttractionMapper.touristicAttractionToDTO(touristicAttraction);
        Offer offer = offerRepository.findTopByTouristicAttractionOrderByEndDate(touristicAttraction).orElse(null);
        OfferDTO offerDTO = null;
        if (offer != null && offer.getEndDate().before(Timestamp.from(Instant.now()))) {
            offer = null;
        }
        if (offer != null) {
            offerDTO = offerMapper.toOfferDTO(offer);
        }
        touristicAttractionDTO.setLastOffer(offerDTO);
        Double review = visitRepository.findReview(touristicAttraction);
        if (review == null)
            review = 0.0;
        touristicAttractionDTO.setReview(review);

        return touristicAttractionDTO;
    }

    public TouristicAttractionDTO getTouristicAttractionDTOById(UUID id) {
        return getTouristicAttractionDTO(getTouristicAttractionById(id));
    }

    private TouristicAttraction getTouristicAttractionById(UUID id) {
        return touristicAttractionRepository.findById(id).orElseThrow(EntityExistsException::new);
    }

    public TouristicAttractionDTO addTouristicAttraction(TouristicAttractionDTO touristicAttractionDTO) {
        System.out.println("AAA " + touristicAttractionDTO.getPhoto());
        TouristicAttraction touristicAttractionToAdd = touristicAttractionMapper.touristicAttractionToEntity(touristicAttractionDTO);
        Location location = locationService.addLocation(touristicAttractionDTO.getLocation());
        touristicAttractionToAdd.setLocation(location);
        TouristicAttraction savedTouristicAttraction = touristicAttractionRepository.save(touristicAttractionToAdd);

        return touristicAttractionMapper.touristicAttractionToDTO(savedTouristicAttraction);
    }

    public void deleteTouristicAttractionById(UUID id) {
        TouristicAttraction touristicAttractionToDelete = getTouristicAttractionById(id);
        List<OfferDTO> offers = offerService.getAllOffersInOrderByTouristicAttractionId(id);
        //delete all offers related to it first
        offers.stream().forEach(o -> offerService.deleteOfferById(o.getId()));
        touristicAttractionRepository.deleteById(id);
    }

    public TouristicAttractionDTO modifyTouristicAttractionById(UUID id, TouristicAttractionDTO touristicAttractionDTO) {
        TouristicAttraction touristicAttractionToModify = getTouristicAttractionById(id);
        touristicAttractionToModify.setName(touristicAttractionDTO.getName());
        touristicAttractionToModify.setCategory(touristicAttractionDTO.getCategory());
        Location modifiedLocation = locationService.modifyLocationForTouristicAttraction(touristicAttractionDTO.getLocation(), touristicAttractionToModify);
        touristicAttractionToModify.setLocation(modifiedLocation);
        touristicAttractionToModify.setAudioDescription(touristicAttractionDTO.getAudioDescription());
        touristicAttractionToModify.setTextDescription(touristicAttractionDTO.getTextDescription());
        touristicAttractionToModify.setPhoto(touristicAttractionDTO.getPhoto());
        TouristicAttraction modifiedTouristicAttraction = touristicAttractionRepository.save(touristicAttractionToModify);
        return touristicAttractionMapper.touristicAttractionToDTO(modifiedTouristicAttraction);
    }

    public List<TouristicAttractionVisitDTO> getMostVisitedTouristicAttractions() {
        List<TouristicAttractionVisitDTO> topAttractions = getTouristicAttractionsEntities().stream()
                .map(attraction -> new TouristicAttractionVisitDTO(attraction.getName(), (long) attraction.getVisits().size()))
                .sorted((dto1, dto2) -> Long.compare(dto2.getNumberOfVisits(), dto1.getNumberOfVisits()))
                .limit(10)
                .collect(Collectors.toList());
        return topAttractions;
    }

    public HashMap<Integer, Integer> getDayFrequencyVisitsForTouristicAttraction(UUID touristicAttractionId, Timestamp date) {
        List<Visit> visits = getTouristicAttractionById(touristicAttractionId).getVisits();

        Map<Integer, Integer> frequencyMap = new HashMap<>();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date.getTime());

        for (int hour = 0; hour < 24; hour++) {
            frequencyMap.put(hour, 0);
        }

        for (Visit visit : visits) {
            Calendar visitCalendar = Calendar.getInstance();
            visitCalendar.setTimeInMillis(visit.getVisitDate().getTime());

            int visitHour = visitCalendar.get(Calendar.HOUR_OF_DAY);
            int count = frequencyMap.get(visitHour) + 1;
            frequencyMap.put(visitHour, count);
        }

        return (HashMap<Integer, Integer>) frequencyMap;
    }

    public HashMap<String, Integer> getMonthFrequencyVisitsForTouristicAttraction(UUID touristicAttractionId, Timestamp year) {
        Map<String, Integer> visitsPerMonth = new HashMap<>();
        TouristicAttraction attraction = getTouristicAttractionById(touristicAttractionId);
        // Initialize the HashMap with keys for each month of the year
        LocalDateTime localDateTime = year.toLocalDateTime();
        for (int i = 1; i <= 12; i++) {
            String month = localDateTime.withMonth(i).getMonth().toString();
            visitsPerMonth.put(month, 0);
        }

        // Iterate over the visits for the attraction and update the HashMap values
        for (Visit visit : attraction.getVisits()) {
            Timestamp visitDate = visit.getVisitDate();
            LocalDate localDate = visitDate.toLocalDateTime().toLocalDate();
            if (localDate.getYear() == year.toLocalDateTime().getYear() && attraction.equals(visit.getTouristicAttraction())) {
                // Visit falls within the specified year and attraction
                String month = localDate.getMonth().toString();
                Integer count = visitsPerMonth.getOrDefault(month, 0);
                visitsPerMonth.put(month, count + 1);
            }
        }

        return (HashMap<String, Integer>) visitsPerMonth;
    }

    public void addTouristicAttractionToWishlist(UUID touristicAttractionId, String userEmail) {
        TouristicAttraction touristicAttraction = getTouristicAttractionById(touristicAttractionId);
        Optional<TravelUser> travelUser = userService.findUserByEmailAddress(userEmail);
        if (travelUser.isEmpty()) {
            throw new EntityNotFoundException("User with email " + userEmail + " does not exist");
        } else {
            wishlistService.addWishlist(touristicAttraction, travelUser.get());
        }
    }

    public void deleteTouristicAttractionFromWishlist(UUID touristicAttractionId, String userEmail) {
        TouristicAttraction touristicAttraction = getTouristicAttractionById(touristicAttractionId);
        Optional<TravelUser> travelUser = userService.findUserByEmailAddress(userEmail);
        if (travelUser.isEmpty()) {
            throw new EntityNotFoundException("User with email " + userEmail + " does not exist");
        } else {
            wishlistService.deleteWishlist(touristicAttraction, travelUser.get());
        }
    }

    public List<TouristicAttractionDTO> getWishlist(TravelUser travelUser) {
        List<TouristicAttractionDTO> l = new ArrayList<>();
        for (Wishlist wishlist : travelUser.getWishlists())
            l.add(getTouristicAttractionDTO(wishlist.getTouristicAttraction()));
        return l;
    }
}