package tr.com.innova.mp.client.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tr.com.innova.mp.client.repository.Config;
import tr.com.innova.mp.client.repository.ConfigRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConfigService {

    private final ConfigRepository repository;

    public Map<Integer, String> getConfigForList() {

        return repository.findAll().stream().collect(Collectors.toMap(Config::getId, config -> String.format("%d - %s / %s", config.getId(), config.getClientId(), config.getMerchantId())));

    }

    public List<Config> getConfigs() {
        return repository.findAll();
    }

    public Config getConfig(Integer id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("config not found"));
    }

    public Config addConfig(String clientId, String merchantId, String url, String encKey, String macKey) {
        return repository.saveAndFlush(Config.builder().clientId(clientId).merchantId(merchantId).url(url).encKey(encKey).macKey(macKey).build());
    }

    public Config updateConfig(Integer configId, String clientId, String merchantId, String url, String encKey, String macKey) {

        var config = repository.findById(configId).orElseThrow(() -> new IllegalArgumentException("config not found"));

        config.setClientId(clientId);
        config.setMerchantId(merchantId);
        config.setUrl(url);
        config.setMacKey(macKey);
        config.setEncKey(encKey);

        return repository.saveAndFlush(config);
    }

    public void deleteConfig(Integer configId) {

        repository.deleteById(configId);
    }
}
