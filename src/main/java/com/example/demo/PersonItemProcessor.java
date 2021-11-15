package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class PersonItemProcessor
    implements ItemProcessor<Person, String> {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(PersonItemProcessor.class);
  

  @Override
  public String process(Person person) throws Exception {
    String greeting = "Hello " + person.getFirstName() + " "
        + person.getLastName();

  
    Thread.sleep(4l);
    
    /*if(Math.random() > .99d) {
    	throw new Exception("something went wrong oops");
    }else {
        count++;
    }*/
    
    LOGGER.info("converting '{}' into '{}'", person, greeting);
    return greeting;
  }
}