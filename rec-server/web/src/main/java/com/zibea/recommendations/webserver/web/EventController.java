package com.zibea.recommendations.webserver.web;

import com.zibea.recommendations.webserver.web.request.LogPurchaseRequest;
import com.zibea.recommendations.webserver.web.request.LogViewRequest;
import com.zibea.recommendations.webserver.core.business.IEventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Used to handle all statistics requests
 *
 * @author Mikhail Bragin
 */
@Controller
@RequestMapping("/event")
public class EventController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(EventController.class);

    @Autowired
    IEventService eventService;

    /**
     * Used to log page view no matter it is product page or not
     *
     * @param ruId                the user unique identifier. Not <code>null</code>
     * @param apiKey              the partner (store) unique identifier. Not <code>null</code>
     * @param itemId              the item (product) unique identifier. Not <code>null</code>. Does not treated in this
     *                            request
     * @param timestamp           timestamp of the event in millis
     * @param httpServletRequest
     * @param httpServletResponse
     * @throws Exception
     */
    @Valid
    @RequestMapping(method = RequestMethod.GET, value = "/pageview/{apiKey}/{itemid}")
    public ResponseEntity<String> pageView(@CookieValue(value = RUID_COOKIE_NAME, required = true) @NotNull String ruId,
                                           @PathVariable("apiKey") @NotNull String apiKey,
                                           @PathVariable("itemid") @NotNull Long itemId,
                                           @RequestParam("ts") @NotNull Long timestamp,
                                           HttpServletRequest httpServletRequest,
                                           HttpServletResponse httpServletResponse) throws Exception {

        LogViewRequest request = new LogViewRequest(apiKey, ruId, itemId, timestamp);

        log.info("Request pageView [" + jsonMapper.writeValueAsString(request) + "]");

        validate(request);

        log.info("Response pageView [" + jsonMapper.writeValueAsString(OK_STRING_RESPONSE) + "]");

        return new ResponseEntity<>(OK_STRING_RESPONSE, HttpStatus.OK);
    }

    /**
     * Used to log item view
     *
     * @param ruId                user unique identifier. Not <code>null</code>
     * @param apiKey              the partner (store) unique identifier. Not <code>null</code>
     * @param itemId              the item (product) unique identifier. Not <code>null</code>.
     * @param timestamp           timestamp of the event in millis
     * @param httpServletRequest
     * @param httpServletResponse
     * @throws Exception
     */
    @Valid
    @RequestMapping(method = RequestMethod.GET, value = "/itemview/{apiKey}/{itemid}")
    public ResponseEntity<String> itemView(@CookieValue(value = RUID_COOKIE_NAME, required = true) String ruId,
                                           @PathVariable("apiKey") @NotNull String apiKey,
                                           @PathVariable("itemid") @NotNull Long itemId,
                                           @RequestParam("ts") @NotNull Long timestamp,
                                           HttpServletRequest httpServletRequest,
                                           HttpServletResponse httpServletResponse) throws Exception {

        LogViewRequest request = new LogViewRequest(apiKey, ruId, itemId, timestamp);

        log.info("Request itemView [" + jsonMapper.writeValueAsString(request) + "]");

        validate(request);

        eventService.logItemViewEvent(apiKey, ruId,
                itemId, timestamp, extractStringAttributes(httpServletRequest));

        log.info("Response itemView [" + jsonMapper.writeValueAsString(OK_STRING_RESPONSE) + "]");

        return new ResponseEntity<>(OK_STRING_RESPONSE, HttpStatus.OK);

    }

    /**
     * Used to log item view
     *
     * @param ruId                user unique identifier. Not <code>null</code>
     * @param apiKey              the partner (store) unique identifier. Not <code>null</code>
     * @param rawItemIds          item (product) unique identifiers. Not <code>null</code>.
     * @param timestamp           timestamp of the event in millis
     * @param httpServletRequest
     * @param httpServletResponse
     * @throws Exception
     */
    @Valid
    @RequestMapping(method = RequestMethod.GET, value = "/purchase/{apiKey}/{itemids}")
    public ResponseEntity<String> purchase(@CookieValue(value = RUID_COOKIE_NAME, required = true) String ruId,
                                           @PathVariable("apiKey") @NotNull String apiKey,
                                           @PathVariable("itemids") @NotNull String rawItemIds,
                                           @RequestParam("ts") @NotNull Long timestamp,
                                           HttpServletRequest httpServletRequest,
                                           HttpServletResponse httpServletResponse) throws Exception {

        LogPurchaseRequest request = new LogPurchaseRequest(apiKey, ruId, timestamp, rawItemIds);

        log.info("Request purchase [" + jsonMapper.writeValueAsString(request) + "]");

        validate(request);

        eventService.logPurchaseEvent(apiKey, ruId,
                request.getItemIds(), timestamp, extractStringAttributes(httpServletRequest));

        log.info("Response purchase [" + jsonMapper.writeValueAsString(OK_STRING_RESPONSE) + "]");

        return new ResponseEntity<>(OK_STRING_RESPONSE, HttpStatus.OK);

    }

}
