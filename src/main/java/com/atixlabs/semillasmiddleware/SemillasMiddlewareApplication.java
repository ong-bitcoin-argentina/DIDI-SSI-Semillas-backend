package com.atixlabs.semillasmiddleware;

import com.atixlabs.semillasmiddleware.pdfparser.util.PDFUtil;
import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class SemillasMiddlewareApplication {

	public static void main(String[] args) {
		SpringApplication.run(SemillasMiddlewareApplication.class, args);
	}

}
