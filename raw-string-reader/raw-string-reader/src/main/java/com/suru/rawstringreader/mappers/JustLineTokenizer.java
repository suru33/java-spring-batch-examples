package com.suru.rawstringreader.mappers;

import org.springframework.batch.item.file.transform.AbstractLineTokenizer;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;

public class JustLineTokenizer extends AbstractLineTokenizer implements InitializingBean {
    @Override
    protected List<String> doTokenize(String s) {
        return List.of(s);
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
