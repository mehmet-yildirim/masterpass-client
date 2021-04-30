package tr.com.innova.mp.client.controller;

import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.DecoderException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import tr.com.innova.mp.client.data.TLVTokenData;
import tr.com.innova.mp.client.repository.Config;
import tr.com.innova.mp.client.service.ConfigService;
import tr.com.innova.mp.client.service.OauthTokenService;
import tr.com.innova.mp.client.service.TLVTokenService;

import java.time.LocalDateTime;
import java.util.Random;

@Controller
@RequiredArgsConstructor
public class MFSController {

    private final ConfigService configService;

    private final TLVTokenService tokenService;

    private final OauthTokenService oauthTokenService;


    @GetMapping("/register-card")
    public String registerCard(@RequestParam("msisdn") String msisdn, @RequestParam("config") Integer id, Model model) throws DecoderException {

        Config selConfig = configService.getConfig(id);

        String requestReferenceNumber = Integer.toString(new Random().nextInt(Integer.MAX_VALUE - 1));
        TLVTokenData tokenData = TLVTokenData.builder()
                .dateTime(LocalDateTime.now())
                .clientId(selConfig.getClientId())
                .msisdn(msisdn)
                .msisdnValidated(false)
                //.requestReferenceNumber("00000000")
                .requestReferenceNumber(requestReferenceNumber)
                .timezone(3)
                .validationType(TLVTokenData.ValidationType.MPIN_OTP)
                .userId("VFTR_" + msisdn)
                .build();

        model.addAttribute("msisdn", msisdn);
        model.addAttribute("reqRefNumber", requestReferenceNumber);
        model.addAttribute("token", tokenService.generateToken(tokenData, selConfig.getEncKey(), selConfig.getMacKey()));
        model.addAttribute("config", selConfig);

        return "step-10-register";
    }

    @GetMapping("/link-card")
    public String linkCard(@RequestParam("msisdn") String msisdn, @RequestParam("config") Integer id, Model model) throws DecoderException {

        Config selConfig = configService.getConfig(id);

        String requestReferenceNumber = Integer.toString(new Random().nextInt(Integer.MAX_VALUE - 1));
        TLVTokenData tokenData = TLVTokenData.builder()
                .dateTime(LocalDateTime.now())
                .clientId(selConfig.getClientId())
                .msisdn(msisdn)
                .msisdnValidated(false)
                //.requestReferenceNumber("00000000")
                .requestReferenceNumber(requestReferenceNumber)
                .timezone(3)
                .validationType(TLVTokenData.ValidationType.MPIN_OTP)
                .userId("VFTR_" + msisdn)
                .build();

        model.addAttribute("msisdn", msisdn);
        model.addAttribute("reqRefNumber", requestReferenceNumber);
        model.addAttribute("token", tokenService.generateToken(tokenData, selConfig.getEncKey(), selConfig.getMacKey()));
        model.addAttribute("config", selConfig);

        return "step-11-linkcards";
    }

    @GetMapping("/payment")
    public String createPayment(@RequestParam("msisdn") String msisdn, @RequestParam("config") Integer id, @RequestParam("aliasName") String aliasName, @RequestParam("cardNumber") String cardNumber, Model model) throws DecoderException {

        Config selConfig = configService.getConfig(id);

        String requestReferenceNumber = Integer.toString(new Random().nextInt(Integer.MAX_VALUE - 1));
        TLVTokenData tokenData = TLVTokenData.builder()
                .dateTime(LocalDateTime.now())
                .clientId(selConfig.getClientId())
                .msisdn(msisdn)
                .msisdnValidated(false)
                //.requestReferenceNumber("00000000")
                .requestReferenceNumber(requestReferenceNumber)
                .timezone(3)
                .validationType(TLVTokenData.ValidationType.MPIN_OTP)
                .userId("VFTR_" + msisdn)
                .build();


        model.addAttribute("orderNo", Integer.toString(new Random().nextInt(Integer.MAX_VALUE - 1)));
        model.addAttribute("msisdn", msisdn);
        model.addAttribute("aliasName", aliasName);
        model.addAttribute("cardNumber", cardNumber);
        model.addAttribute("reqRefNumber", requestReferenceNumber);
        model.addAttribute("token", tokenService.generateToken(tokenData, selConfig.getEncKey(), selConfig.getMacKey()));
        model.addAttribute("config", selConfig);

        return "step-12-payment";
    }

    @GetMapping("/recurring-payment")
    public String recurringPayment(@RequestParam("msisdn") String msisdn, @RequestParam("config") Integer id, @RequestParam("aliasName") String aliasName, Model model) throws DecoderException {

        Config selConfig = configService.getConfig(id);

        String requestReferenceNumber = Integer.toString(new Random().nextInt(Integer.MAX_VALUE - 1));
        TLVTokenData tokenData = TLVTokenData.builder()
                .dateTime(LocalDateTime.now())
                .clientId(selConfig.getClientId())
                .msisdn(msisdn)
                .msisdnValidated(false)
                //.requestReferenceNumber("00000000")
                .requestReferenceNumber(requestReferenceNumber)
                .timezone(3)
                .validationType(TLVTokenData.ValidationType.MPIN_OTP)
                .userId("VFTR_" + msisdn)
                .build();

        model.addAttribute("orderNo", Integer.toString(new Random().nextInt(Integer.MAX_VALUE - 1)));
        model.addAttribute("msisdn", msisdn);
        model.addAttribute("aliasName", aliasName);
        model.addAttribute("reqRefNumber", requestReferenceNumber);
        model.addAttribute("token", tokenService.generateToken(tokenData, selConfig.getEncKey(), selConfig.getMacKey()));
        model.addAttribute("config", selConfig);

        return "step-13-recurring-payment";
    }

    @GetMapping("/vposRequest")
    public String vposRequest(Model model) {
        var oAuthToken = oauthTokenService.retriveOauthToken();

        model.addAttribute("oauthToken", oAuthToken);

        return "step-14-vpos-request";
    }


}
