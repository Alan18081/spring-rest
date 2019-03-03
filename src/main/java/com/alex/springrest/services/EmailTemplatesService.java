package com.alex.springrest.services;

import java.util.Map;

public interface EmailTemplatesService {

    String renderTemplate(String templateName, Map<String, Object> args);

}
