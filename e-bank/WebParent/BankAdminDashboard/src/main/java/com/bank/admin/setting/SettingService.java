package com.bank.admin.setting;

import com.bank.common.entity.Setting;
import com.bank.common.entity.SettingCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SettingService {
    @Autowired
    private SettingRepository repo;

    public List<Setting> listAllSettings() {
        return (List<Setting>) repo.findAll();
    }

    public GeneralSettingBag getGeneralSettings() {
        List<Setting> settings = new ArrayList<>();

        List<Setting> generalSettings = repo.findByCategory(SettingCategory.GENERAL);


        settings.addAll(generalSettings);


        return new GeneralSettingBag(settings);
    }

    public void saveAll(Iterable<Setting> settings) {
        repo.saveAll(settings);
    }

    public List<Setting> getMailServerSettings(){
        return repo.findByCategory(SettingCategory.MAIL_SERVER);
    }

    public List<Setting> getMailTemplatesSettings(){
        return repo.findByCategory(SettingCategory.MAIL_TEMPLATES);
    }
}
