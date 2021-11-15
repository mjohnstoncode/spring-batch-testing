package com.example.demo;

import org.apache.commons.io.IOUtils;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

public class StaxEventItemReaderThreadSafe<T> implements ItemReader<T>,ItemStream,InitializingBean,ResourceAwareItemReaderItemStream<T> {
    
    private StaxEventItemReader<T> reader;
    
    
    public StaxEventItemReaderThreadSafe() {
    	Jaxb2Marshaller studentMarshaller = new Jaxb2Marshaller();
        studentMarshaller.setClassesToBeBound(Student.class);

		reader = new StaxEventItemReaderBuilder<T>()
	              .name("studentReader")
	              .resource(new ClassPathResource("xml/students.xml"))
	              .addFragmentRootElements("student")
	              .unmarshaller(studentMarshaller)
	              .build();
    }

	@Override
	public void setResource(Resource resource) {
		if(resource==null){
            reader.setResource(new InputStreamResource(IOUtils.toInputStream("")));
        } else {
            reader.setResource(resource); 
        }
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		reader.afterPropertiesSet();
	}

	@Override
	public void open(ExecutionContext executionContext) throws ItemStreamException {
		reader.open(executionContext);
	}

	@Override
	public void update(ExecutionContext executionContext) throws ItemStreamException {
		reader.update(executionContext);
	}

	@Override
	public void close() {
		reader.close();
	}

	@Override
	public synchronized T read() throws UnexpectedInputException, ParseException, Exception {
		return reader.read();
	}
	
	@Override
	public boolean equals(Object obj) {
        return reader.equals(obj);
    }
	
	@Override
	public int hashCode() {
        return reader.hashCode();
    }
	
	public boolean isSaveState() {
        return reader.isSaveState();
    }
	
	public void setStrict(boolean strict) {
        reader.setStrict(strict);
    }
	
	public void setCurrentItemCount(int count) {
        reader.setCurrentItemCount(count);
    }
	
	public void setFragmentRootElementName(String fragmentRootElementName) {
        reader.setFragmentRootElementName(fragmentRootElementName);
    }
	
	public void setMaxItemCount(int count) {
        reader.setMaxItemCount(count);
    }
	
	public void setUnmarshaller(Unmarshaller unmarshaller) {
        reader.setUnmarshaller(unmarshaller);
    }
	
	public void setName(String name) {
        reader.setName(name);
    }
	
	
	public void setSaveState(boolean saveState) {
        reader.setSaveState(saveState);
    }

	@Override
	public String toString() {
        return reader.toString();
    }
	
}