package ru.skypro.homework.service.impl;

import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.Ad;
import ru.skypro.homework.dto.CreateOrUpdateAd;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.service.AdService;

@Service
public class AdServiceImpl implements AdService {

    private final AdRepository adRepository;
    private final UserDetailsManager manager;

    public AdServiceImpl(AdRepository adRepository, UserDetailsManager manager) {
        this.adRepository = adRepository;
        this.manager = manager;
    }

    public void addAd(CreateOrUpdateAd createAd) {

        //adRepository.save(createAd);
    }

}
