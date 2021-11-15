package com.example.demo;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class XMlItemProcessor
    implements ItemProcessor<Student, String> {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(XMlItemProcessor.class);
  

  @Override
  public String process(Student person) throws Exception {
    String greeting = "Hello " + person.getEmailAddress() + " "
        + person.getName();

  
    Thread.sleep(4l);
    
    /*if(Math.random() > .99d) {
    	throw new Exception("something went wrong oops");
    }*/
    
    LOGGER.info("converting '{}' into '{}'", person, greeting);
    return greeting;
  }
}
