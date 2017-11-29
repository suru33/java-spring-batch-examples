package com.suru.springbatch.xmlreader.configuration;

import com.suru.springbatch.xmlreader.domain.Customer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.xstream.XStreamMarshaller;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class XmlReadJobConfiguration {
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Bean
    public ItemWriter<Customer> customerItemWriter() {
        return items -> {
            for (Customer i : items) {
                System.out.println(i);
            }
            System.out.println("-----------------------");
        };
    }

    @Bean
    public StaxEventItemReader<Customer> customerStaxEventItemReader() throws ClassNotFoundException {

        // Unmarshaller for the XML
        XStreamMarshaller marshaller = new XStreamMarshaller();

        // mapping domain class with model
        Map<String, Class> aliases = new HashMap<>();
        aliases.put("customer", Customer.class);

        marshaller.setAliases(aliases);

        // preparing reader based on above configuration
        StaxEventItemReader<Customer> reader = new StaxEventItemReader<>();
        // loading file
        reader.setResource(new ClassPathResource("/data/random_data.xml"));
        // XML root element setup
        reader.setFragmentRootElementName("customer");
        reader.setUnmarshaller(marshaller);

        return reader;
    }

    @Bean
    public Step step1() {
        try {
            return stepBuilderFactory.get("step-1")
                    .<Customer, Customer>chunk(10)
                    .reader(customerStaxEventItemReader())
                    .writer(customerItemWriter())
                    .build();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Bean
    public Job job() {
        return jobBuilderFactory.get("xml-job")
                .start(step1())
                .build();
    }

}
