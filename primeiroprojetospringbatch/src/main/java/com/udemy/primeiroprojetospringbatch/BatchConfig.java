package com.udemy.primeiroprojetospringbatch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {

    @Bean
    Job imprimiOlaJob(JobRepository jobRepository, Step step) {
		return new JobBuilder("job", jobRepository)
				.start(step)
				.incrementer(new RunIdIncrementer())
				.build();
	}

    @Bean
    Step olaMundo(JobRepository jobRepository, PlatformTransactionManager platformTransactionManager) {
    	return new StepBuilder("olaMundo", jobRepository)
    			.tasklet(imprimiOlaTasklet(null), platformTransactionManager)
    			.build();
    }

    @Bean
    @StepScope
    Tasklet imprimiOlaTasklet(@Value("#{jobParameters['nome']}") String nome) {
		return (StepContribution contribution, ChunkContext chunkContext) -> {
				System.out.println(String.format("Olá, %s!", nome));
				return RepeatStatus.FINISHED;
			};
	}

}
