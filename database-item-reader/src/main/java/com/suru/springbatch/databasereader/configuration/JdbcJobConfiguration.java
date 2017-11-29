package com.suru.springbatch.databasereader.configuration;

import com.suru.springbatch.databasereader.domain.CustomRowMapper;
import com.suru.springbatch.databasereader.domain.Customer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class JdbcJobConfiguration {
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    private DataSource dataSource;

    // getting conflict from multiple resources so, added Qualifier annotation
    @Autowired
    public void setDataSource(@Qualifier("dataSource") DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public JdbcCursorItemReader<Customer> cursorItemReader() {
        JdbcCursorItemReader<Customer> reader = new JdbcCursorItemReader<>();
        reader.setDataSource(this.dataSource); // setting the data source
        reader.setRowMapper(new CustomRowMapper()); // adding custom row mapper
        // defining SQL
        reader.setSql("select id, first_name, last_name, random from customer order by id");
        return reader;
    }

    @Bean
    public JdbcPagingItemReader<Customer> pagingItemReader() {
        JdbcPagingItemReader<Customer> reader = new JdbcPagingItemReader<>();
        reader.setDataSource(dataSource);
        // Records per page
        // preferred value:  chunk size in step
        reader.setFetchSize(10);
        reader.setRowMapper(new CustomRowMapper());

        // Need custom query provider for paging
        // Different for different databases
        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause("id, first_name, last_name, random");
        queryProvider.setFromClause("from customer");

        // sorting keys for sync
        // can able to add multiple columns for sorting
        Map<String, Order> sortKeys = new HashMap<>();
        sortKeys.put("id", Order.ASCENDING);

        queryProvider.setSortKeys(sortKeys);

        reader.setQueryProvider(queryProvider);
        return reader;
    }


    @Bean
    public ItemWriter<Customer> customerItemWriter() {
        return new ItemWriter<Customer>() {
            @Override
            public void write(List<? extends Customer> items) throws Exception {
                for (Customer c : items) {
                    System.out.println(c);
                }
                System.out.println("---------------------------");
            }
        };
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step-1")
                //<Input, Output> and chunk size
                .<Customer, Customer>chunk(10) // 10 records per each transaction
                .reader(pagingItemReader())
                .writer(customerItemWriter())
                .build();
    }

    @Bean
    public Job job() {
        return jobBuilderFactory.get("jdbc-job-5")
                .start(step1())
                .build();
    }

}
