package com.suru.springbatch.flatfilereader.configuration;

import com.suru.springbatch.flatfilereader.domain.Customer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class FlatFileReadJobConfiguration {
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Bean
    public FlatFileItemReader<Customer> flatFileItemReader() {
        FlatFileItemReader<Customer> reader = new FlatFileItemReader<>();

        // first line is header in my csv file
        reader.setLinesToSkip(1);

        // classpath directly linked to resources folder
        reader.setResource(new ClassPathResource("/data/random_data.csv"));

        // line mapper for mapping each line
        DefaultLineMapper<Customer> lineMapper = new DefaultLineMapper<>();

        // default delimiter is coma or we can specify a delimiter or a RegEx
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames(new String[]{"id", "firstName", "lastName", "random"});

        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(new CustomerFieldSetMapper());

        // set the line mapper to reader
        reader.setLineMapper(lineMapper);

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
                .reader(flatFileItemReader())
                .writer(customerItemWriter())
                .build();
    }

    @Bean
    public Job job() {
        return jobBuilderFactory.get("file-job")
                .start(step1())
                .build();
    }
}
