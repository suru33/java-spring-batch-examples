package com.suru.springbatch.writingtofile.configuration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import com.suru.springbatch.writingtofile.domain.Customer;
import com.suru.springbatch.writingtofile.domain.CustomerLineAggregator;
import com.suru.springbatch.writingtofile.domain.CustomerRowMapper;

@Configuration
public class WritingToFileJobConfiguration {
	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private DataSource dataSource;

	// for cursor JDBC item reader
	public JdbcCursorItemReader<Customer> cursorItemReader() {
		JdbcCursorItemReader<Customer> reader = new JdbcCursorItemReader<>();
		reader.setDataSource(dataSource);
		reader.setFetchSize(10);
		reader.setSql("SELECT * FROM customer");
		reader.setRowMapper(new CustomerRowMapper());
		return reader;
	}

	@Bean
	public JdbcPagingItemReader<Customer> jdbcPagingItemReader() {
		JdbcPagingItemReader<Customer> reader = new JdbcPagingItemReader<>();
		reader.setDataSource(dataSource);
		reader.setFetchSize(10);
		reader.setRowMapper(new CustomerRowMapper());

		MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
		queryProvider.setSelectClause("id, first_name, last_name, random");
		queryProvider.setFromClause("from customer");

		Map<String, Order> sortKeys = new HashMap<>();
		sortKeys.put("id", Order.ASCENDING);

		queryProvider.setSortKeys(sortKeys);

		reader.setQueryProvider(queryProvider);

		return reader;
	}

	@Bean
	public FlatFileItemWriter<Customer> flatFileItemWriter() throws Exception {
		FlatFileItemWriter<Customer> writer = new FlatFileItemWriter<>();

		File out = File.createTempFile("customer", ".out", new File("./output"));
		System.out.println("out file path: " + out.getAbsolutePath());
		writer.setResource(new FileSystemResource(out));

		// toString() method in Customer object will called
		// writer.setLineAggregator(new PassThroughLineAggregator<>());
		writer.setLineAggregator(new CustomerLineAggregator());
		writer.afterPropertiesSet();

		return writer;
	}

	@Bean
	public Step step() throws Exception {
		return stepBuilderFactory.get("flat-file-writer-step")
				.<Customer, Customer>chunk(10)
				.reader(jdbcPagingItemReader())
				.writer(flatFileItemWriter())
				.build();
	}
	
	@Bean
	public Job job() throws Exception {
		return jobBuilderFactory.get("flat-file-writer-job-4")
				.start(step())
				.build();
	}
}
