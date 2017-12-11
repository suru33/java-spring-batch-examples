package com.suru.springbatch.writemultiplefiles.configuration;

import com.suru.springbatch.writemultiplefiles.domain.Customer;
import com.suru.springbatch.writemultiplefiles.domain.CustomerClassifier;
import com.suru.springbatch.writemultiplefiles.domain.CustomerLineAggregator;
import com.suru.springbatch.writemultiplefiles.domain.CustomerRowMapper;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.xstream.XStreamMarshaller;

import javax.sql.DataSource;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class WritingMultipleFilesJobConfiguration {

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
        reader.setFetchSize(10);
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
    public FlatFileItemWriter<Customer> jsonItemWriter() throws Exception {
        FlatFileItemWriter<Customer> writer = new FlatFileItemWriter<>();
        writer.setLineAggregator(new CustomerLineAggregator());
        File out = File.createTempFile("json-file", ".json", new File("./output"));
        writer.setResource(new FileSystemResource(out));
        System.out.println("out1: " + out.getAbsolutePath());
        return writer;
    }

    @Bean
    public StaxEventItemWriter<Customer> xmlItemWriter() throws Exception {
        XStreamMarshaller marshaller = new XStreamMarshaller();

        Map<String, Class> aliases = new HashMap<>();
        aliases.put("customer", Customer.class);
        marshaller.setAliases(aliases);
        StaxEventItemWriter<Customer> writer = new StaxEventItemWriter<>();
        writer.setMarshaller(marshaller);
        writer.setRootTagName("customers");
        File out = File.createTempFile("xml-file", ".xml", new File("./output"));
        writer.setResource(new FileSystemResource(out));
        System.out.println("out1: " + out.getAbsolutePath());
        writer.afterPropertiesSet();
        return writer;
    }

    @Bean
    public CompositeItemWriter<Customer> itemWriter() throws Exception {
        List<ItemWriter<? super Customer>> writers = new ArrayList<>();
        writers.add(xmlItemWriter());
        writers.add(jsonItemWriter());

        CompositeItemWriter<Customer> compositeItemWriter = new CompositeItemWriter<>();
        compositeItemWriter.setDelegates(writers);
        compositeItemWriter.afterPropertiesSet();
        return compositeItemWriter;
    }

    // writes output based on given condition
    @Bean
    public ClassifierCompositeItemWriter<Customer> classifierCompositeItemWriter() throws Exception {
        ClassifierCompositeItemWriter<Customer> writer = new ClassifierCompositeItemWriter<>();
        writer.setClassifier(new CustomerClassifier(xmlItemWriter(), jsonItemWriter()));
        return writer;
    }

    @Bean
    public Step step1() throws Exception {
        return stepBuilderFactory.get("step1-multiple-file-write")
                .<Customer, Customer>chunk(10)
                .reader(jdbcPagingItemReader())
                .writer(itemWriter())
                .build();
    }

    @Bean
    public Step step2() throws Exception {
        return stepBuilderFactory.get("step2-multiple-file-write-classifier")
                .<Customer, Customer>chunk(10)
                .reader(jdbcPagingItemReader())
                .writer(classifierCompositeItemWriter())
                // need to define as streams
                // because ClassifierCompositeItemWriter will not implemented stream
                .stream(jsonItemWriter())
                .stream(xmlItemWriter())
                .build();
    }

    @Bean
    public Job job() throws Exception {
        return jobBuilderFactory.get("write-multiple-files-job-2")
                .start(step1())
                .next(step2())
                .build();
    }

}
