package com.suru.springbatch.customitemreader.configuration;

import java.util.List;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

public class MyItemStreamReader implements ItemStreamReader<String> {

	private final List<String> data;
	private int index = 0;
	private boolean restart = false;

	public MyItemStreamReader(List<String> data) {
		this.data = data;
		this.index = 0;
	}

	@Override
	public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		String item = null;
		if (this.index < this.data.size()) {
			item = data.get(index);
			index++;
		}
		if (this.index == 49 && !restart) {
			throw new RuntimeException("Throwing the Runtime Exception for checking");
		}
		return item;
	}

	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		if (executionContext.containsKey("index")) {
			this.index = executionContext.getInt("index");
			restart = true;
		} else {
			this.index = 0;
			executionContext.put("index", 0);
		}
	}

	@Override
	public void update(ExecutionContext executionContext) throws ItemStreamException {
		executionContext.put("index", this.index);
	}

	@Override
	public void close() throws ItemStreamException {

	}
}
