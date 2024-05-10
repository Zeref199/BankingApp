package com.bank.admin.setting;

import com.bank.common.entity.Setting;
import com.bank.common.entity.SettingCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SettingRepository extends JpaRepository<Setting, String> {
    public List<Setting> findByCategory(SettingCategory category);

}
