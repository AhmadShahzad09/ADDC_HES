
package com.minsait.oum.mdc;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = { "com.minsait.oum.mdc.data.repository" })
@ComponentScan(basePackages = { "com.minsait" })
public class RestMain {
	public static void main(String[] args) {
		new SpringApplicationBuilder(RestMain.class).web(WebApplicationType.SERVLET).run(args);
	}
}