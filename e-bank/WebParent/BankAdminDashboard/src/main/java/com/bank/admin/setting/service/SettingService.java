package com.bank.admin.setting.service;

import com.bank.admin.setting.GeneralSettingBag;
import com.bank.admin.setting.repository.CurrencyRepository;
import com.bank.admin.setting.repository.SettingRepository;
import com.bank.common.entity.Currency;
import com.bank.common.entity.Setting;
import com.bank.common.entity.SettingCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SettingService {
    @Autowired
    private SettingRepository settingRepository;
    @Autowired
    private CurrencyRepository currencyRepo;

    public List<Setting> listAllSettings() {
        return (List<Setting>) settingRepository.findAll();
    }

    public List<Currency> listCurrency(){
        return currencyRepo.findAllByOrderByNameAsc();
    }

    public GeneralSettingBag getGeneralSettings() {
        List<Setting> settings = new ArrayList<>();

        List<Setting> generalSettings = settingRepository.findByCategory(SettingCategory.GENERAL);
        List<Setting> currencySettings = settingRepository.findByCategory(SettingCategory.CURRENCY);


        settings.addAll(generalSettings);
        settings.addAll(currencySettings);


        return new GeneralSettingBag(settings);
    }

    public void saveAll(Iterable<Setting> settings) {
        settingRepository.saveAll(settings);
    }

    public List<Setting> getMailServerSettings(){
        return settingRepository.findByCategory(SettingCategory.MAIL_SERVER);
    }

    public List<Setting> getMailTemplatesSettings(){
        return settingRepository.findByCategory(SettingCategory.MAIL_TEMPLATES);
    }

    public Optional<Currency> findCurrencyById(Integer currencyId){
        return currencyRepo.findById(currencyId);
    }
}
