package com.suru.springbatch.writingtofile.domain;

import org.springframework.batch.item.file.transform.LineAggregator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomerLineAggregator implements LineAggregator<Customer> {


/* - // For JSON output
	private ObjectMapper mapper = new ObjectMapper();
	
	@Override
	public String aggregate(Customer item) {
		try {
			return mapper.writeValueAsString(item);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getCause());
		}
	}
*/
	
	
	// CSV Output

	@Override
	public String aggregate(Customer item) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(item.getId());
		buffer.append(',');
		buffer.append(item.getFirstName());
		buffer.append(',');
		buffer.append(item.getLastName());
		buffer.append(',');
		buffer.append(item.getRandom());
		return buffer.toString();
	}

}
