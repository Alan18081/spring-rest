package com.alex.springrest.services.impl;

import com.alex.springrest.services.EmailTemplatesService;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.util.Map;

@Service
public class EmailTemplatesServiceImpl implements EmailTemplatesService {

    @Autowired
    private VelocityEngine engine;

    @Override
    public String renderTemplate(String templateName, Map<String, Object> args) {
        engine.init();
        VelocityContext velocityContext = new VelocityContext();
        for(Map.Entry<String, Object> entry : args.entrySet()) {
            velocityContext.put(entry.getKey(), entry.getValue());
        }
        Template template = engine.getTemplate("./src/main/resources/templates/" + templateName + ".vm");
        StringWriter stringWriter = new StringWriter();
        template.merge(velocityContext, stringWriter);

        return stringWriter.toString();
    }
}
