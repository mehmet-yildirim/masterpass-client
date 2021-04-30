package tr.com.innova.mp.client.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.stereotype.Component;
import tr.com.innova.mp.client.data.TLVTokenData;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.Formatter;

@Slf4j
@Component
public class TLVTokenService {

    public static final DateTimeFormatter DT_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    static final Integer RADIX = 16;

    static final Integer TAG_LENGTH = 2;

    static final String TAG_CLIENT_ID = "FF01";
    static final String TAG_TIMEZONE = "FF02";
    static final String TAG_DATETIME = "FF03";
    static final String TAG_MSISDN = "FF04";
    static final String TAG_REQ_REF_NUMBER = "FF05";
    static final String TAG_USER_ID = "FF06";
    static final String TAG_CLIENT_VALIDATED_MSISDN = "FF07";
    static final String TAG_VALIDATION_TYPE = "FF08";
    static final String TAG_MERCHANT_TYPE = "FF09";
    static final String TAG_VPOS_CURRENCY_CODE = "FF0A";
    static final String TAG_VPOS_MERCHANT_ID = "FF0B";
    static final String TAG_VPOS_MERCHANT_TERMINAL_ID = "FF0C";
    static final String TAG_VPOS_MERCHANT_EMAIL = "FF0D";
    static final String TAG_VPOS_TERMINAL_USER_ID = "FF0E";
    static final String TAG_VPOS_PROVISION_USER_ID = "FF0F";
    static final String TAG_VPOS_PROVISION_PASSWORD = "FF10";
    static final String TAG_VPOS_STORE_KEY = "FF11";
    static final String TAG_VPOS_POSNET_ID = "FF12";
    static final String TAG_BANK_ICA = "FF13";

    public String generateToken(TLVTokenData tokenData, String encKey, String macKey) throws DecoderException {

        Integer size = 0;
        ByteBuffer bbConcat = ByteBuffer.allocate(512);

        //client_id
        if (tokenData.getClientId() != null) {
            byte[] clientId = toTLVString(TAG_CLIENT_ID, tokenData.getClientId());
            bbConcat.put(clientId);
            size = size + clientId.length;
        }

        //timezone
        if (tokenData.getTimezone() != null) {
            byte[] timezone = toTLVInteger(TAG_TIMEZONE, tokenData.getTimezone());
            bbConcat.put(timezone);
            size = size + timezone.length;

        }

        //date_time
        if (tokenData.getDateTime() != null) {
            byte[] dateTime = toTLVString(TAG_DATETIME, tokenData.getDateTime().format(DT_FORMAT));
            bbConcat.put(dateTime);
            size = size + dateTime.length;
        }

        //msisdn
        if (tokenData.getMsisdn() != null) {
            byte[] msisdn = toTLVString(TAG_MSISDN, tokenData.getMsisdn());
            bbConcat.put(msisdn);
            size = size + msisdn.length;
        }

        //req_ref_no
        if (tokenData.getRequestReferenceNumber() != null) {
            byte[] reqRefNo = toTLVString(TAG_REQ_REF_NUMBER, tokenData.getRequestReferenceNumber());
            bbConcat.put(reqRefNo);
            size = size + reqRefNo.length;
        }

        //userid
        if (tokenData.getUserId() != null) {
            byte[] userId = toTLVString(TAG_USER_ID, tokenData.getUserId());
            bbConcat.put(userId);
            size = size + userId.length;
        }

        //msisdn_validated)
        byte[] msisdnValidated = toTLVInteger(TAG_CLIENT_VALIDATED_MSISDN, (tokenData.isMsisdnValidated()) ? 1 : 0);
        bbConcat.put(msisdnValidated);
        size = size + msisdnValidated.length;

        //validation_type
        if (tokenData.getValidationType() != null) {
            byte[] validationType = toTLVInteger(TAG_VALIDATION_TYPE, tokenData.getValidationType().ordinal());
            bbConcat.put(validationType);
            size = size + validationType.length;
        }

        int s = 0;

        int n = (size / 16) * 16;
        int x = n + 16;
        int j = (x - size) % 16;

        if (j > 0) {
            bbConcat.put(new byte[]{(byte) 0x80});
            size = size + 1;
            for (int i = j - 1; i > 0; i--) {
                bbConcat.put(new byte[]{(byte) 0x00});
                s++;
            }
            size = size + s;
        }

        ByteBuffer bbRaw = ByteBuffer.allocate(size);
        bbRaw.put(bbConcat.array(), 0, size);

        byte[] enc = Hex.decodeHex(encKey);

        SecretKeySpec eks = new SecretKeySpec(enc, "AES");
        Cipher c = null;
        byte[] es = null;
        try {
            c = Cipher.getInstance("AES/CBC/NoPadding");
            c.init(Cipher.ENCRYPT_MODE, eks, new IvParameterSpec(new byte[16]));
            es = c.doFinal(bbRaw.array());
        } catch (Exception e) {
            e.printStackTrace();
        }


        byte[] mac = macKey.getBytes();

        String hexStr = byteToHex(es).toUpperCase();

        String hmacStr = new HmacUtils(HmacAlgorithms.HMAC_SHA_1, mac).hmacHex(hexStr).toUpperCase();

        return String.format("%s%s", hexStr, hmacStr);

    }

    private byte[] toTLVString(String field, String value) throws DecoderException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(TAG_LENGTH + 1 + value.length());

        byteBuffer.put(Hex.decodeHex(field.toCharArray()));
        byteBuffer.put(Integer.valueOf(value.length()).byteValue());
        byteBuffer.put(value.getBytes(StandardCharsets.UTF_8));

        return byteBuffer.array();
    }

    private byte[] toTLVInteger(String field, Integer value) throws DecoderException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(TAG_LENGTH + 1 + 1);

        byteBuffer.put(Hex.decodeHex(field.toCharArray()));
        byteBuffer.put(new BigInteger(String.valueOf(String.valueOf(value).length()), 16).byteValue());
        byteBuffer.put(value.byteValue());

        return byteBuffer.array();
    }

    @Deprecated
    private String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
}
