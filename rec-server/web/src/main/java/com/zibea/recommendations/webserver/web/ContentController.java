package com.zibea.recommendations.webserver.web;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Mikhail Bragin
 */
@Controller
@RequestMapping("/content")
public class ContentController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(ContentController.class);

    @Valid
    @RequestMapping(method = RequestMethod.GET, value = "/js/api.js")
    public void jsApi(HttpServletRequest httpServletRequest,
                      HttpServletResponse httpServletResponse) throws Exception {

        log.info("Request jsApi");


        httpServletResponse.setContentType("application/javascript");

        try (InputStream is = ContentController.class.getResourceAsStream("/api.js")) {

            IOUtils.copy(is, httpServletResponse.getOutputStream());
            httpServletResponse.flushBuffer();

        } catch (IOException e) {
            log.error("Error while reading api file", e);
            return;
        }

        log.info("Response jsApi");
    }
}
