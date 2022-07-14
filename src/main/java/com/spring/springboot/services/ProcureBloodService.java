package com.spring.springboot.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.spring.springboot.models.ProcureBlood;
import com.spring.springboot.repository.ProcureBloodRepository;

@Service
public class ProcureBloodService {
    
    @Autowired
    private ProcureBloodRepository procureBloodRepository;

    public ProcureBlood save(ProcureBlood procureBlood){
        return this.procureBloodRepository.save(procureBlood);
    }

    public List<ProcureBlood> getAllByUserId(Long userId){
        return this.procureBloodRepository.findAllByUserId(userId);
    }
}