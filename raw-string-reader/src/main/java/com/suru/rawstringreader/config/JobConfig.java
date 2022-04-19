package com.suru.rawstringreader.config;

import com.suru.rawstringreader.mappers.JustLineTokenizer;
import com.suru.rawstringreader.mappers.RawStringMapper;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration

public class JobConfig {
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Bean
    public FlatFileItemReader<String> flatFileItemReader() {
        FlatFileItemReader<String> reader = new FlatFileItemReader<>();

        // reader.setLinesToSkip(1);

        // classpath directly linked to resources folder
        reader.setResource(new FileSystemResource("data.txt"));

        // line mapper for mapping each line
        DefaultLineMapper<String> lineMapper = new DefaultLineMapper<>();

        // default delimiter is coma or we can specify a delimiter or a RegEx
        JustLineTokenizer tokenizer = new JustLineTokenizer();

        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(new RawStringMapper());
        // set the line mapper to reader
        reader.setLineMapper(lineMapper);

        return reader;
    }

    @Bean
    public ItemWriter<String> customerItemWriter() {
        return items -> {
            for (String c : items) {
                System.out.println(c);
            }
            System.out.println("--------------------------");
        };
    }

    @Bean
    public Step step1() {
        return stepBuilderFactory.get("step-1")
                .<String, String>chunk(2)
                .reader(flatFileItemReader())
                .writer(customerItemWriter())
                .build();
    }

    @Bean
    public Job job() {
        return jobBuilderFactory.get("raw-file-job")
                .start(step1())
                .build();
    }

}
