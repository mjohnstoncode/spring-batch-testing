package com.example.demo;

import java.util.concurrent.Future;

import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.integration.async.AsyncItemProcessor;
import org.springframework.batch.integration.async.AsyncItemWriter;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.PassThroughLineAggregator;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class HelloWorldJobConfig {

  @Bean
  public Job helloWorlJob(JobBuilderFactory jobBuilders,
      StepBuilderFactory stepBuilders) {
    return jobBuilders.get("helloWorldJob")
        .start(helloWorldStep(stepBuilders)).build();
  }

  @Bean
  public Step helloWorldStep(StepBuilderFactory stepBuilders) {
    return stepBuilders.get("helloWorldStep")
        .<Student, Future<String>>chunk(5000).reader(itemReaderXML())
        .processor(asyncProcessor()).faultTolerant()
        .retryLimit(15).retry(Exception.class).writer(futureWriter())
        //.taskExecutor(taskExecutor())
        .build();
    //StaxEventItemReaderThreadSafe
   
	  /*return stepBuilders.get("helloWorldStep")
            .<Student, String>chunk(50).reader(itemReaderXML())
            .processor(processorXML()).faultTolerant()
            .retryLimit(3).retry(Exception.class).writer(writer())
            .taskExecutor(taskExecutor())
            .build();*/
  }
  
	@Bean
	public AsyncItemProcessor<Student, String> asyncProcessor() {
		AsyncItemProcessor<Student, String> asyncItemProcessor = new AsyncItemProcessor<>();
		asyncItemProcessor.setDelegate(processorXML());
		asyncItemProcessor.setTaskExecutor(taskExecutor2());

		return asyncItemProcessor;
	}

	
  
  
  public SimpleAsyncTaskExecutor taskExecutor() {
	  SimpleAsyncTaskExecutor simpleAsyncTaskExecutor = new SimpleAsyncTaskExecutor();
	  //simpleAsyncTaskExecutor.setConcurrencyLimit(10);
	  //simpleAsyncTaskExecutor.setThreadPriority(0);
	  //simpleAsyncTaskExecutor.setThreadNamePrefix("MySimpleAsyncThreads");
	  return simpleAsyncTaskExecutor;
  }
  
  @Bean
  public TaskExecutor taskExecutor2() {
      ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
      executor.setCorePoolSize(800);
      executor.setMaxPoolSize(800);
      executor.setQueueCapacity(10000);
      
      //executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
      executor.setThreadNamePrefix("MultiThreaded-");
      return executor;
  }
  

  @Bean
  public FlatFileItemReader<Person> reader() {
    return new FlatFileItemReaderBuilder<Person>()
        .name("personItemReader")
        .resource(new ClassPathResource("csv/persons.csv"))
        .delimited().names(new String[] {"firstName", "lastName"})
        .targetType(Person.class).build();
  }
  

  @Bean
  public ItemReader<Student> itemReaderXML() {
      Jaxb2Marshaller studentMarshaller = new Jaxb2Marshaller();
      studentMarshaller.setClassesToBeBound(Student.class);

      return new StaxEventItemReaderBuilder<Student>()
              .name("studentReader")
              .resource(new ClassPathResource("xml/bigxml.xml"))
              .addFragmentRootElements("student")
              .unmarshaller(studentMarshaller)
              .build();
  }
  
  @Bean
  public ItemReader<Student> itemReaderXMLThreadSafe() {
      Jaxb2Marshaller studentMarshaller = new Jaxb2Marshaller();
      studentMarshaller.setClassesToBeBound(Student.class);

      return new StaxEventItemReaderThreadSafe<Student>();
  }

  @Bean
  public PersonItemProcessor processor() {
    return new PersonItemProcessor();
  }
  
  @Bean
  public XMlItemProcessor processorXML() {
    return new XMlItemProcessor();
  }

  @Bean
  public FlatFileItemWriter<String> writer() {
    return new FlatFileItemWriterBuilder<String>()
        .name("greetingItemWriter")
        .resource(new FileSystemResource(
            "target/test-outputs/greetings.txt"))
        .lineAggregator(new PassThroughLineAggregator<>()).build();
  }
  
  
  @Bean
  public AsyncItemWriter<String> futureWriter() {
	  
	  FlatFileItemWriter<String> writer = new FlatFileItemWriterBuilder<String>()
		        .name("asyncWritter")
		        .resource(new FileSystemResource(
		            "target/test-outputs/greetings.txt"))
		        .lineAggregator(new PassThroughLineAggregator<>()).build();
	  
	  AsyncItemWriter<String> aWriter = new AsyncItemWriter<String>();
	  aWriter.setDelegate(writer);
	  
	  return aWriter;
     
  }
}
