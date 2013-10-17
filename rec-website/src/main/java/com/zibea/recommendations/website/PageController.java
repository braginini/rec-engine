package com.zibea.recommendations.website;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller("pageController")
public class PageController {

    @RequestMapping(method = RequestMethod.GET, value = "/index")
    public String index() throws Exception {

        return "index";

    }
}
