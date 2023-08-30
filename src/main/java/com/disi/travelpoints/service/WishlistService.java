package com.disi.travelpoints.service;

import com.disi.travelpoints.model.entity.TouristicAttraction;
import com.disi.travelpoints.model.entity.TravelUser;
import com.disi.travelpoints.model.entity.Wishlist;
import com.disi.travelpoints.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class WishlistService {
    private final WishlistRepository wishlistRepository;

    public void addWishlist(TouristicAttraction touristicAttraction, TravelUser travelUser) {
        Wishlist wishlist = new Wishlist();
        wishlist.setTouristicAttraction(touristicAttraction);
        wishlist.setUser(travelUser);
        wishlistRepository.save(wishlist);
    }

    public void deleteWishlist(TouristicAttraction touristicAttraction, TravelUser travelUser) {
        Wishlist wishlist = wishlistRepository.findByTouristicAttractionAnUser(touristicAttraction, travelUser);
        wishlistRepository.deleteById(wishlist.getId());
    }
}
