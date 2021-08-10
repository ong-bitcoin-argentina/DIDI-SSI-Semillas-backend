package com.atixlabs.semillasmiddleware;

import com.atixlabs.semillasmiddleware.pdfparser.util.PDFUtil;
import com.atixlabs.semillasmiddleware.security.configuration.WebSecurityConfig;
import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@EnableAspectJAutoProxy
@Import(WebSecurityConfig.class)
public class SemillasMiddlewareApplication {

	public static void main(String[] args) {
		SpringApplication.run(SemillasMiddlewareApplication.class, args);
	}

}
