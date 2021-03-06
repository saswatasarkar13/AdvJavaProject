package com.spring.springboot.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.springboot.models.Donation;
import com.spring.springboot.repository.DonationRepository;

@Service
public class DonationService {

    @Autowired
    private DonationRepository donationRepository;

    public Donation save(Donation donation) {
        return this.donationRepository.save(donation);
    }

    public Donation findById(Long id) {
        Optional<Donation> donation = this.donationRepository.findById(id);
        System.out.println(donation);
        if (donation.isPresent())
            return donation.get();
        return null;
    }

    public List<Donation> getAllByUserId(Long userId) {
        return this.donationRepository.findAllByUserId(userId);
    }

    public Long getCount() {
        return this.donationRepository.count();
    }

    public List<Donation> getAllPendingDonations() {
        return this.donationRepository.findAllPendingDonations();
    }

    public void delete(Donation obj) {
        this.donationRepository.delete(obj);
    }
}
