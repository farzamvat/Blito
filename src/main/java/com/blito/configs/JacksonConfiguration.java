package com.blito.configs;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.blito.rest.viewmodels.View;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;

@Configuration
public class JacksonConfiguration extends WebMvcConfigurerAdapter {
	@Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
      MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
      ObjectMapper mapper = new ObjectMapper() {
        private static final long serialVersionUID = 1L;
        @Override
        protected DefaultSerializerProvider _serializerProvider(SerializationConfig config) {
          // replace the configuration with my modified configuration.
          // calling "withView" should keep previous config and just add my changes.
          return super._serializerProvider(config.withView(View.DefaultView.class));
        }        
      };
      mapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, true);
      converter.setObjectMapper(mapper);
      converters.add(converter);
    }
}
