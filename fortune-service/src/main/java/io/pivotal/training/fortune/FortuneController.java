package io.pivotal.training.fortune;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FortuneController {

  private final Logger logger = LoggerFactory.getLogger(FortuneController.class);

  private final FortuneService fortuneService;

  public FortuneController(FortuneService fortuneService) {
    this.fortuneService = fortuneService;
  }

  @GetMapping("/")
  public Map<String, String> getFortune() {
    String fortune = fortuneService.getFortune();
    logger.info("retrieving fortune '{}'", fortune);

    Map<String, String> map = new HashMap<>();
    map.put("fortune", fortune);
    return map;
  }
}
