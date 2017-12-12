package com.suru.springbatch.validationexample.configuration;

import com.suru.springbatch.validationexample.domain.Customer;
import com.suru.springbatch.validationexample.domain.CustomerRowMapper;
import com.suru.springbatch.validationexample.validators.CustomerValidator;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.PassThroughLineAggregator;
import org.springframework.batch.item.validator.ValidatingItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ValidationJobConfiguration {
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private DataSource dataSource;

    @Bean
    public JdbcPagingItemReader<Customer> jdbcPagingItemReader() {
        JdbcPagingItemReader<Customer> reader = new JdbcPagingItemReader<>();

        reader.setDataSource(dataSource);
        reader.setFetchSize(10); // to improve the performance
        reader.setRowMapper(new CustomerRowMapper());

        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause("id, first_name, last_name, random");
        queryProvider.setFromClause("from customer");

        Map<String, Order> sortKeys = new HashMap<>();
        sortKeys.put("id", Order.ASCENDING);

        queryProvider.setSortKeys(sortKeys);

        reader.setQueryProvider(queryProvider);
        System.out.println("-- reader --");

        return reader;
    }

    @Bean
    public ItemWriter<Customer> flatFileItemWriter() throws IOException {
        FlatFileItemWriter<Customer> writer = new FlatFileItemWriter<>();
        // PassThroughLineAggregator - calls toString() method in Customer object
        writer.setLineAggregator(new PassThroughLineAggregator<>());
        File tempFile = File.createTempFile("dump", ".out", new File("./output"));
        writer.setResource(new FileSystemResource(tempFile));
        return writer;
    }

    @Bean
    public ValidatingItemProcessor<Customer> customerValidatingItemProcessor() {
        ValidatingItemProcessor<Customer> customerValidatingItemProcessor
                = new ValidatingItemProcessor<>(new CustomerValidator());

        // default filter is false it will throw exception when ValidationException cause
        // true - just skip that particular invalid input
        customerValidatingItemProcessor.setFilter(true);

        return customerValidatingItemProcessor;
    }

    @Bean
    public Step step1() throws IOException {
        return stepBuilderFactory.get("validation-step")
                .<Customer, Customer>chunk(10)
                .reader(jdbcPagingItemReader())
                .processor(customerValidatingItemProcessor())
                .writer(flatFileItemWriter())
                .build();
    }

    @Bean
    public Job job() throws IOException {
        return jobBuilderFactory.get("validation-job-2")
                .start(step1())
                .build();
    }

}
