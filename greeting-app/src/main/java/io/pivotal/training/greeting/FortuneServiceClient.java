package io.pivotal.training.greeting;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Component
public class FortuneServiceClient {
	private RestTemplate restTemplate;
	private EurekaClient eurekaClient;

	private final Logger logger = LoggerFactory
			.getLogger(FortuneServiceClient.class);

	public FortuneServiceClient(RestTemplate restTemplate,
			EurekaClient eurekaClient) {
		this.restTemplate = restTemplate;
		this.eurekaClient = eurekaClient;
	}

	@HystrixCommand(fallbackMethod = "defaultFortune")
	public String getFortune() {
		String baseUrl = lookupUrlFor("FORTUNE");
		@SuppressWarnings("unchecked")
		Map<String, String> result = restTemplate.getForObject(baseUrl,
				Map.class);
		String fortune = result.get("fortune");
		logger.info("received fortune '{}'", fortune);
		return fortune;
	}

	private String lookupUrlFor(String appName) {
		InstanceInfo instanceInfo = eurekaClient.getNextServerFromEureka(
				appName, false);
		return instanceInfo.getHomePageUrl();
	}

	public String defaultFortune() {
		logger.info("Default fortune used.");
		return "Your future is uncertain";
	}
}