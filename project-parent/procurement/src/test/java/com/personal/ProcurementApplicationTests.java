package com.personal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProcurementApplicationTests {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	@Autowired
	private JavaMailSender sender;

	@Test
	public void contextLoads() {
		MimeMessage message = sender.createMimeMessage();

		try {
			// true表示需要创建一个multipart message
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom("yinliwen@bjscfl.com");
			helper.setTo("675886926@qq.com");
			helper.setSubject("liwentest");
			helper.setText("hello nihao , i am ylw!", true);

//			FileSystemResource file = new FileSystemResource(new File(filePath));
//			String fileName = filePath.substring(filePath.lastIndexOf(File.separator));
//			helper.addAttachment(fileName, file);

			sender.send(message);
			logger.info("带附件的邮件已经发送。");
		} catch (MessagingException e) {
			logger.error("发送带附件的邮件时发生异常！", e);
		}
	}

}
