package com.suru.springbatch.simpleitemprocessor.configuration;

import com.suru.springbatch.simpleitemprocessor.domain.Customer;
import com.suru.springbatch.simpleitemprocessor.domain.CustomerRowMapper;
import com.suru.springbatch.simpleitemprocessor.domain.Name;
import com.suru.springbatch.simpleitemprocessor.processor.CustomerToNameProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.xstream.XStreamMarshaller;

import javax.sql.DataSource;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class SimpleItemProcessorJobConfiguration {

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
    public ItemProcessor<Customer, Name> itemProcessor() {
        return new CustomerToNameProcessor();
    }

    @Bean
    public StaxEventItemWriter<Name> xmlItemWriter() throws Exception {
        XStreamMarshaller marshaller = new XStreamMarshaller();

        Map<String, Class> aliases = new HashMap<>();
        aliases.put("customer", Name.class);
        marshaller.setAliases(aliases);
        StaxEventItemWriter<Name> writer = new StaxEventItemWriter<>();
        writer.setMarshaller(marshaller);
        writer.setRootTagName("customers");
        File out = File.createTempFile("names-file", ".xml", new File("./output"));
        writer.setResource(new FileSystemResource(out));
        System.out.println("out-xml : " + out.getAbsolutePath());
        writer.afterPropertiesSet();
        return writer;
    }

    @Bean
    public Step step() throws Exception {
        return stepBuilderFactory.get("item-processor-step")
                .<Customer, Name>chunk(10)
                .reader(jdbcPagingItemReader())
                .processor(itemProcessor())
                .writer(xmlItemWriter())
                .build();
    }

    @Bean
    public Job job() throws Exception {
        return jobBuilderFactory.get("item-processor-job")
                .start(step())
                .build();
    }

}
