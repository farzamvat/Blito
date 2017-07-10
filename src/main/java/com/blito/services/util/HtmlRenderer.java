package com.blito.services.util;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class HtmlRenderer {

	@Autowired 
	private TemplateEngine templateEngine;
	
	public String renderHtml(String templateName, Map<String,?> map)  {
		Context context = new Context();
		context.setVariables(map);
		return templateEngine.process(templateName, context);
	}
}
