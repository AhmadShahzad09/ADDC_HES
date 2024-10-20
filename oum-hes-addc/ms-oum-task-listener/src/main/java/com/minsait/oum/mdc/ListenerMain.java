
package com.minsait.oum.mdc;

import org.modelmapper.ModelMapper;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = { "com.minsait.com.oum.mdc.repository" })
@ComponentScan(basePackages = { "com.minsait" })
public class ListenerMain {
	public static void main(String[] args) {
		new SpringApplicationBuilder(ListenerMain.class).web(WebApplicationType.NONE).run(args);
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
}