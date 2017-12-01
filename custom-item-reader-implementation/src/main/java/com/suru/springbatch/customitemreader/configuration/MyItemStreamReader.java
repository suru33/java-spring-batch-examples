package com.suru.springbatch.customitemreader.configuration;

import java.util.List;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

public class MyItemStreamReader implements ItemStreamReader<String> {

	// reading data
	private final List<String> data;
	// maintain the index for state
	private int index = 0;
	// custom implementation
	private boolean restart = false;

	public MyItemStreamReader(List<String> data) {
		this.data = data;
		this.index = 0;
	}

	// calls each and every time for an item
	@Override
	public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		String item = null;
		if (this.index < this.data.size()) {
			item = data.get(index);
			index++;
		}
		// to check the reader state at item 49
		// I throw an exception to make fail the transaction
		if (this.index == 49 && !restart) {
			throw new RuntimeException("Throwing the Runtime Exception for checking");
		}
		return item;
	}

	// executes when starting of a step
	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		// checks the `index` key is available in the map
		if (executionContext.containsKey("index")) {
			this.index = executionContext.getInt("index");
			// to test run job second time
			restart = true;
		} else {
			// put the initial index if not available
			this.index = 0;
			executionContext.put("index", 0);
		}
	}

	// executes after a successful transaction
	@Override
	public void update(ExecutionContext executionContext) throws ItemStreamException {
		// update my index
		executionContext.put("index", this.index);
	}

	@Override
	public void close() throws ItemStreamException {

	}
}

/*
 * I passed 100 items with a chunk size 10
 * for first run output will be 1 to 40 records  
 * it fail at item 49 because I threw a RuntimeException for testing
 * when I restart the job it will automatically starts from item 41
 * because it stores the last successful job update index 40
 * it outputs 41 to 100 records in 2nd job run
 * 
 * */
