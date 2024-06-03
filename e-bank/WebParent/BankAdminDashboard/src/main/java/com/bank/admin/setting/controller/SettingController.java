package com.bank.admin.setting.controller;

import com.bank.admin.setting.GeneralSettingBag;
import com.bank.admin.setting.service.SettingService;
import com.bank.common.entity.Currency;
import com.bank.common.entity.Setting;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
public class SettingController {

    @Autowired
    private SettingService service;


    @GetMapping("/settings")
    public String listAll(Model model) {
        List<Setting> listSettings = service.listAllSettings();
        List<Currency> listCurrencies = service.listCurrency();

        model.addAttribute("listCurrencies", listCurrencies);

        for (Setting setting : listSettings) {
            model.addAttribute(setting.getKey(), setting.getValue());
        }

        return "settings/settings";
    }

    @PostMapping("/settings/save_general")
    public String saveGeneralSettings(HttpServletRequest request, RedirectAttributes ra) throws IOException {
        GeneralSettingBag settingBag = service.getGeneralSettings();
        saveCurrencySymbol(request, settingBag);

        updateSettingValuesFromForm(request, settingBag.list());

        ra.addFlashAttribute("message", "General settings have been saved.");

        return "redirect:/settings";
    }

    private void saveCurrencySymbol(HttpServletRequest request, GeneralSettingBag settingBag) {
        Integer currencyId = Integer.parseInt(request.getParameter("CURRENCY_ID"));
        Optional<Currency> findByIdResult = service.findCurrencyById(currencyId);

        if (findByIdResult.isPresent()) {
            Currency currency = findByIdResult.get();
            settingBag.updateCurrencySymbol(currency.getSymbol());
        }
    }

    private void updateSettingValuesFromForm(HttpServletRequest request, List<Setting> listSettings) {
        for (Setting setting : listSettings) {
            String value = request.getParameter(setting.getKey());
            if (value != null) {
                setting.setValue(value);
            }
        }

        service.saveAll(listSettings);
    }

    @PostMapping("/settings/save_mail_server")
    public String saveMailServerSettings(HttpServletRequest request, RedirectAttributes ra){
        List<Setting> mailServerSettings = service.getMailServerSettings();
        updateSettingValuesFromForm(request, mailServerSettings);

        ra.addFlashAttribute("message", "Mail server settings have been saved");

        return "redirect:/settings";
    }

    @PostMapping("/settings/save_mail_templates")
    public String saveMailTemplateSettings(HttpServletRequest request, RedirectAttributes ra){
        List<Setting> mailTemplateSettings = service.getMailTemplatesSettings();
        updateSettingValuesFromForm(request, mailTemplateSettings);

        ra.addFlashAttribute("message", "Mail template settings have been saved");

        return "redirect:/settings";
    }
}
