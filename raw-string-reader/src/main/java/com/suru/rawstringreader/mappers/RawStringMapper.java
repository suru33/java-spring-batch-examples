package com.suru.rawstringreader.mappers;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class RawStringMapper implements FieldSetMapper<String> {
    @Override
    public String mapFieldSet(FieldSet fieldSet) throws BindException {
        return fieldSet.readRawString(0);
    }
}
