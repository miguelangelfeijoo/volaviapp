package com.app.Volavia.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Controller
public class DebugSessionController {

    @GetMapping("/debug/session")
    @ResponseBody
    public Map<String, Object> verAtributosSesion(HttpSession session) {
        Map<String, Object> sessionAttributes = new HashMap<>();
        Enumeration<String> attributeNames = session.getAttributeNames();

        while (attributeNames.hasMoreElements()) {
            String attributeName = attributeNames.nextElement();
            sessionAttributes.put(attributeName, session.getAttribute(attributeName));
        }

        return sessionAttributes;
    }
}

