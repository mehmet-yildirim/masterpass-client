package tr.com.innova.mp.client.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import tr.com.innova.mp.client.service.ConfigService;

@Controller
@RequiredArgsConstructor
public class ConfigController {

    private final ConfigService configService;

    @GetMapping({"/configs", "/configs/{id}"})
    public String config(Model model, @PathVariable(value = "id", required = false) Integer id) {

        if (id != null) {
            model.addAttribute("form", configService.getConfig(id));
        }

        model.addAttribute("configs", configService.getConfigs());
        return "configs";

    }

    @PostMapping("/configs")
    public String configPost(Model model, @ModelAttribute ConfigParameters parameters) {

        if (parameters.getId() != null) {
            configService.updateConfig(parameters.getId(), parameters.getClientId(), parameters.getMerchantId(), parameters.getUrl(), parameters.getEncKey(), parameters.getMacKey());
        } else {
            configService.addConfig(parameters.getClientId(), parameters.getMerchantId(), parameters.getUrl(), parameters.getEncKey(), parameters.getMacKey());
        }
        model.addAttribute("configs", configService.getConfigs());

        return "configs";
    }

    @GetMapping("/configs/delete/{id}")
    public String deleteConfig(@PathVariable("id") Integer id, Model model) {

        configService.deleteConfig(id);

        model.addAttribute("configs", configService.getConfigs());

        return "redirect:/configs";
    }

    @Data
    public static class ConfigParameters {

        private Integer id;

        private String clientId;

        private String merchantId;

        private String url;

        private String encKey;

        private String macKey;

    }

}
