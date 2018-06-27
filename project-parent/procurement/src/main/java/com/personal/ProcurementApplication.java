package com.personal;

import com.personal.config.system.file.FileConfig;
import com.personal.config.system.mail.ReportConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@EnableConfigurationProperties({FileConfig.class, ReportConfig.class})
@SpringBootApplication
public class ProcurementApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(ProcurementApplication.class, args);
	}
}
