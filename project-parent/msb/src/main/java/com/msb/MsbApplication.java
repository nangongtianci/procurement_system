package com.msb;

import com.msb.config.system.file.FileConfig;
import com.msb.config.system.mail.ReportConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties({FileConfig.class, ReportConfig.class})
@SpringBootApplication
public class MsbApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsbApplication.class, args);
	}

}
