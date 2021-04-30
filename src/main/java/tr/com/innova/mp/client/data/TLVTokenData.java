package tr.com.innova.mp.client.data;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TLVTokenData {

    private String clientId;

    private Integer timezone;

    private LocalDateTime dateTime = LocalDateTime.now();

    private String msisdn;

    private String requestReferenceNumber;

    private String userId;

    private boolean msisdnValidated;

    private ValidationType validationType; // 0: NONE, 1: OTP, 2: MPIN, 3: MPIN+OTP, 4: SECURE3D

    public static enum ValidationType {
        NONE,
        OTP,
        MPIN,
        MPIN_OTP,
        SECURE3D
    }
}
