package com.service.utils.test.dom;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "myhost.store")
public class StoreHost extends MyHost {
}
