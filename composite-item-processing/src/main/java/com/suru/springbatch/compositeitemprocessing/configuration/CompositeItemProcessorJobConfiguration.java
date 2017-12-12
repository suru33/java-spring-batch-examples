package com.suru.springbatch.compositeitemprocessing.configuration;

import com.suru.springbatch.compositeitemprocessing.domain.Customer;
import com.suru.springbatch.compositeitemprocessing.domain.CustomerName;
import com.suru.springbatch.compositeitemprocessing.domain.CustomerRowMapper;
import com.suru.springbatch.compositeitemprocessing.processors.CustomerNameItemProcessor;
import com.suru.springbatch.compositeitemprocessing.processors.FilteringItemProcessor;
import com.suru.springbatch.compositeitemprocessing.processors.UpperCaseItemProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.PassThroughLineAggregator;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class CompositeItemProcessorJobConfiguration {
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
    public ItemWriter<CustomerName> flatFileItemWriter() throws IOException {
        FlatFileItemWriter<CustomerName> writer = new FlatFileItemWriter<>();
        // PassThroughLineAggregator - calls toString() method in Customer object
        writer.setLineAggregator(new PassThroughLineAggregator<>());
        File tempFile = File.createTempFile("dump", ".out", new File("./output"));
        System.out.println("out: " + tempFile.getAbsolutePath());
        writer.setResource(new FileSystemResource(tempFile));
        return writer;
    }

    @Bean
    public CompositeItemProcessor<Customer, CustomerName> itemProcessor() throws Exception {
        // step 1: filter all even customer ID
        // step 2: convert Customer to CustomerName
        // step 3: transform CustomerName to Upper Case

        List<ItemProcessor<?, ?>> delegates = new ArrayList<>();
        delegates.add(new FilteringItemProcessor()); // step 1
        delegates.add(new CustomerNameItemProcessor()); // step 2
        delegates.add(new UpperCaseItemProcessor()); // step 3

        CompositeItemProcessor<Customer, CustomerName> processor = new CompositeItemProcessor<>();
        processor.setDelegates(delegates);
        processor.afterPropertiesSet();

        return processor;
    }

    @Bean
    public Step step1() throws Exception {
        return stepBuilderFactory.get("composite-item-processor-step")
                .<Customer, CustomerName>chunk(10)
                .reader(jdbcPagingItemReader())
                .processor(itemProcessor())
                .writer(flatFileItemWriter())
                .build();
    }

    @Bean
    public Job job() throws Exception {
        return jobBuilderFactory.get("composite-item-processor-job")
                .start(step1())
                .build();
    }
}
