package tr.com.innova.mp.client.service;

import org.apache.commons.codec.DecoderException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Component;
import tr.com.innova.mp.client.data.TLVTokenData;

import java.time.LocalDateTime;

@Component
class TLVTokenServiceTest {

    @Test
    public void tlvTest() throws DecoderException {

        TLVTokenService service = new TLVTokenService();

        TLVTokenData data = TLVTokenData.builder()
                .clientId("11223344")
                .timezone(2)
                .dateTime(LocalDateTime.parse("20160120165502", TLVTokenService.DT_FORMAT))
                .msisdn("112233445566")
                .requestReferenceNumber("11223344")
                .userId("masterpass_user")
                .msisdnValidated(true)
                .validationType(TLVTokenData.ValidationType.SECURE3D)
                .build();

        Assertions.assertEquals("13A6DAD3119F29A8C4BF6D5BD11564E4E1A93F85B7F2AD9E8E97756688754DE32A23ADE41DFD9F76186D8EB25E66D0DCF458ECAA026F16463811C48FC814E50B10FF57FDFDB0C0761088D1AC4DDDAE749CC77FD402A2B8E005A43AEEC914E6F9CFBA4E9C136096E7A3D615E63B17D1517CAB8E10",
                service.generateToken(data, "0F777D55FDB154E7D8754C3C0E660A65", "23EB7D2F86190551AA3048DDBFB33F44"));

    }

}
