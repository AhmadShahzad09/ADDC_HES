
package com.minsait.oum.mdc;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = { "com.minsait.com.oum.mdc.repository" })

//@ComponentScan(
//		basePackages = { "com.minsait" },
//		excludeFilters = @ComponentScan.Filter(
//				type = FilterType.REGEX, 
//				pattern = "com.minsait.commands.config.*")) 
@ComponentScan(basePackages = { "com.minsait" }) 
@ImportResource({"classpath*:applicationContext.xml"})
public class Main {
	public static void main(String[] args) {
		new SpringApplicationBuilder(Main.class).web(WebApplicationType.NONE).run(args);
	}
}