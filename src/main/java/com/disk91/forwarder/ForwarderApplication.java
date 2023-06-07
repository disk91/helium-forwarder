package com.disk91.forwarder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableScheduling
@EnableAsync
@EnableWebMvc
@Configuration
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.disk91.forwarder.jpa")
public class ForwarderApplication implements CommandLineRunner, ExitCodeGenerator {

	@Autowired
	protected ForwarderConfig forwarderConfig;

	public static boolean requestingExitForStartupFailure = false;

	public static ApplicationContext context;

	public static void main(String[] args) {
		context = SpringApplication.run(ForwarderApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		long pid = ProcessHandle.current().pid();
		System.out.println("-------------- GO ("+pid+") (v: "+forwarderConfig.getVersion()+" )--------------");

		if (ForwarderApplication.requestingExitForStartupFailure) exit();
	}

	public static void exit() {
		int exitCode = SpringApplication.exit(context,new ExitCodeGenerator() {
			@Override
			public int getExitCode() {
				return 0;
			}
		});
		// Bug in springboot, calling exit is create a deadlock
		//System.exit(exitCode);
		System.out.println("------------- GONE --------------");
	}

	public int getExitCode() {
		return 0;
	}

}
