package tr.com.innova.mp.client.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created By : uunsal on  12.04.2021
 **/
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
class OauthTokenServiceTest {


    @Autowired
    private OauthTokenService oauthService;


    @Test
    void retriveOauthToken() {

        var token = oauthService.retriveOauthToken();

        log.info("Token Value : {}", token);

        Assertions.assertNotNull(token);
    }
}
