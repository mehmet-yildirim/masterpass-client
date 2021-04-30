package tr.com.innova.mp.client.controller;

import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.DecoderException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import tr.com.innova.mp.client.data.TLVTokenData;
import tr.com.innova.mp.client.repository.Config;
import tr.com.innova.mp.client.service.ConfigService;
import tr.com.innova.mp.client.service.TLVTokenService;

import java.time.LocalDateTime;
import java.util.Random;

@Controller
@SessionAttributes("selectedConfig")
@RequiredArgsConstructor
public class IndexController {

    private final ConfigService configService;

    private final TLVTokenService tokenService;

    @GetMapping("/")
    public String setConfig(@RequestParam(value = "msisdn", required = false) String msisdn,
                            @RequestParam(value = "config", required = false) Integer config,
                            Model model) throws DecoderException {
        if (msisdn == null || config == null) {
            model.addAttribute("configList", configService.getConfigForList());

            return "step-01";
        }

        Config selConfig = configService.getConfig(config);

        String requestReferenceNumber = Integer.toString(new Random().nextInt());
        TLVTokenData tokenData = TLVTokenData.builder()
                .dateTime(LocalDateTime.now())
                .clientId(selConfig.getClientId())
                .msisdn(msisdn)
                .msisdnValidated(false)
                //.requestReferenceNumber("00000000")
                .requestReferenceNumber(requestReferenceNumber)
                .timezone(3)
                .validationType(TLVTokenData.ValidationType.OTP)
                .userId("VFTR_" + msisdn)
                .build();

        model.addAttribute("msisdn", msisdn);
        model.addAttribute("reqRefNumber", requestReferenceNumber);
        model.addAttribute("token", tokenService.generateToken(tokenData, selConfig.getEncKey(), selConfig.getMacKey()));
        model.addAttribute("config", selConfig);


        return "step-02-listcard";
    }

}
