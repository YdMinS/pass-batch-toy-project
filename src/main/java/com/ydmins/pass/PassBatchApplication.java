package com.ydmins.pass;

import org.springframework.batch.core.*;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;

@SpringBootApplication
public class PassBatchApplication {

	@Bean
	public Step passStep(JobRepository jobRepository, PlatformTransactionManager transactionManager){
		return new StepBuilder("passStep", jobRepository)
				.tasklet((contribution, chunkContext) -> {
					System.out.println("Execute PassStep");
					return RepeatStatus.FINISHED;
				}, transactionManager)
				.build();
	}

	@Bean
	public Job passJob(JobRepository jobRepository, Step passStep){
		return new JobBuilder("passJob", jobRepository)
				.start(passStep)
				.build();
	}

	@Bean
	public CommandLineRunner runJob(JobLauncher jobLauncher, Job passJob){
		return args -> {
			JobParameters jobParameters = new JobParametersBuilder()
					.toJobParameters();
			jobLauncher.run(passJob, jobParameters);
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(PassBatchApplication.class, args);
	}

}
