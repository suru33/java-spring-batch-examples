package com.suru.springbatch.writingtodb.configuration;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.suru.springbatch.writingtodb.domain.Customer;

@Configuration
public class WriteToDatabaseJobConfiguration {

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private DataSource dataSource;

	@Bean
	public FlatFileItemReader<Customer> flatFileItemReader() {
		FlatFileItemReader<Customer> reader = new FlatFileItemReader<>();
		reader.setResource(new ClassPathResource("/data/random_data.csv"));
		reader.setLinesToSkip(1);

		DefaultLineMapper<Customer> lineMapper = new DefaultLineMapper<>();

		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setNames(new String[] { "id", "firstName", "lastName", "random" });

		lineMapper.setLineTokenizer(tokenizer);
		lineMapper.setFieldSetMapper(new CustomerFieldSetMapper());
		lineMapper.afterPropertiesSet();

		reader.setLineMapper(lineMapper);
		return reader;
	}

	@Bean
	public JdbcBatchItemWriter<Customer> customerItemWriter() {
		JdbcBatchItemWriter<Customer> writer = new JdbcBatchItemWriter<>();
		writer.setDataSource(dataSource);
		writer.setSql("INSERT INTO  customer VALUES(:id, :firstName, :lastName, :random)");
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Customer>());
		writer.afterPropertiesSet();
		return writer;
	}

	@Bean
	public Step step1() {
		 return stepBuilderFactory.get("step-1")
				 .<Customer, Customer>chunk(10)
				 .reader(flatFileItemReader())
				 .writer(customerItemWriter())
				 .build();
	}

	@Bean
	public Job job() {
		return jobBuilderFactory.get("jdbc-writer-job")
				.start(step1())
				.build();
	}
	
}
