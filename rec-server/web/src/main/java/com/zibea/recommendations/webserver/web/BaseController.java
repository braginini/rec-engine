package com.zibea.recommendations.webserver.web;

import com.zibea.recommendations.webserver.web.request.IRequest;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mikhail Bragin
 */
public abstract class BaseController {

    protected static final String RUID_COOKIE_NAME = "ruid";
    protected static final String API_KEY_COOKIE_NAME = "pid";
    protected static final String OK_STRING_RESPONSE = "ok";

    private static final Logger log = LoggerFactory.getLogger(BaseController.class);

    @Autowired
    protected MappingJacksonHttpMessageConverter jsonConverter;

    @Autowired
    protected ByteArrayHttpMessageConverter byteArrayConverter;

    ObjectMapper jsonMapper = new ObjectMapper();

    @PostConstruct
    public void init() {
        jsonConverter.getObjectMapper().configure(SerializationConfig.Feature.WRITE_EMPTY_JSON_ARRAYS, false);
    }


    /**
     * Handles an exception thrown by a web. This method is needed
     * because the application front-end access some features using http POST
     * and GET. We need to deliver proper error codes to the client. The
     * response is a pure string in a json form ({"error_code":NUMBER})
     *
     * @param response
     * @param exception
     * @throws Exception
     * @see
     */
    @ExceptionHandler(Exception.class)
    public void handleException(HttpServletResponse response, Exception exception) throws IOException {

        if (exception != null) {

        }
    }

    /**
     * Sets cookie to response
     *
     * @param httpServletResponse {@link javax.servlet.http.HttpServletResponse} response object to set cookie to
     * @param name                {@link String} cookie name
     * @param value               {@link String} cookie value
     * @param age                 an integer specifying the maximum age of the
     *                            cookie in seconds; if negative, means
     *                            the cookie is not stored; if zero, deletes
     *                            the cookie
     */
    protected void setCookie(HttpServletResponse httpServletResponse, String name, String value, int age) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(age);
        cookie.setPath("/");
        httpServletResponse.addCookie(cookie);
    }

    protected void killCookie(HttpServletResponse httpServletResponse, String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        httpServletResponse.addCookie(cookie);
    }


    /**
     * Validates specified request
     *
     * @param request
     */
    protected void validate(IRequest request) {
        request.validate();
    }

    protected Map<String, String> extractStringAttributes(HttpServletRequest request) {

        Map<String, String> attrMap = new HashMap<>();

        String host = request.getRemoteHost();
        if (host != null)
            attrMap.put("host", host);

        String userAgent = request.getHeader("User-Agent");
        if (userAgent != null)
            attrMap.put("User-Agent", userAgent);

        String referrer = request.getHeader("referrer");
        if (referrer != null)
            attrMap.put("referrer", referrer);

        return attrMap;
    }

}
