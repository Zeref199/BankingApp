package com.bank.admin.Settings;

import com.bank.admin.setting.repository.SettingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class SettingRepositoryTests {
    @Autowired
    SettingRepository repo;

    @Test
    public void testCreateGeneralSettings() {
//        Setting siteName = new Setting("SITE_NAME", "Shopme", SettingCategory.GENERAL);
//        Setting siteLogo = new Setting("SITE_LOGO", "Shopme.png", SettingCategory.GENERAL);
//        Setting copyright = new Setting("COPYRIGHT", "Copyright (C) 2021 Shopme Ltd.", SettingCategory.GENERAL);
//
//        repo.saveAll(List.of(siteName, siteLogo, copyright));
//
//        Iterable<Setting> iterable = repo.findAll();
//
//        assertThat(iterable).size().isGreaterThan(0);
    }

}
