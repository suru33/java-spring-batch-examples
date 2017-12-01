package com.suru.springbatch.readmultipledatasources.configuration;

import com.suru.springbatch.readmultipledatasources.domain.Customer;
import com.suru.springbatch.readmultipledatasources.domain.CustomerFieldSetMapper;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class MultipleResourceReaderJobConfiguration {
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Value("classpath*:/data/random_data_*.csv")
    private Resource[] resources;

    @Bean
    public MultiResourceItemReader<Customer> customerMultiResourceItemReader() {
        MultiResourceItemReader<Customer> reader = new MultiResourceItemReader<>();
        reader.setResources(resources);
        reader.setDelegate(flatFileItemReader());
        return reader;
    }

    @Bean
    public FlatFileItemReader<Customer> flatFileItemReader() {
        FlatFileItemReader<Customer> reader = new FlatFileItemReader<>();

        // first line is header in my csv file
        reader.setLinesToSkip(1);

        // line mapper for mapping each line
        DefaultLineMapper<Customer> customerLineMapper = new DefaultLineMapper<>();

        // default delimiter is coma or we can specify a delimiter or a RegEx
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames(new String[]{"id", "firstName", "lastName", "random"});

        customerLineMapper.setLineTokenizer(tokenizer);
        customerLineMapper.setFieldSetMapper(new CustomerFieldSetMapper());
        customerLineMapper.afterPropertiesSet();

        // set the line mapper to reader
        reader.setLineMapper(customerLineMapper);

        return reader;
    }

    @Bean
    public ItemWriter<Customer> customerItemWriter() {
        return items -> {
            for (Customer c : items) {
                System.out.println(c);
            }
            System.out.println("--------------------------");
        };
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step-1")
                .<Customer, Customer>chunk(10)
                .reader(customerMultiResourceItemReader())
                .writer(customerItemWriter())
                .build();
    }

    @Bean
    public Job job() {
        return jobBuilderFactory.get("multiple-resource-read-job")
                .start(step1())
                .build();
    }

}
