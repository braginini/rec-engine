package com.zibea.recommendations.webserver.web;

import com.zibea.recommendations.webserver.web.request.AssignParamsRequest;
import com.zibea.recommendations.webserver.web.response.AssignParamsResponse;
import com.zibea.recommendations.webserver.core.business.IAuthorizationService;
import com.zibea.recommendations.webserver.core.business.entity.ParametersEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Used to handle authorization and param assignment requests
 *
 * @author Mikhail Bragin
 */
@Controller
@RequestMapping("/auth")
public class AuthorizationController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(AuthorizationController.class);

    @Autowired
    IAuthorizationService authService;

    /**
     * Used to assign params to user session.
     * Each store page view should invoke this method.
     * For each user system generates unique id (uuid) and set in cookie.
     * Requires partner (store) unique code (id) as a path variable.
     * There are two possible cases now:
     * - user has no uuid in cookie - generate uuid and set it to cookie
     * - user already has uuid in cookie - we reuse it.
     * Also this method may be used to set additional params, feel free.
     *
     * @param ruId                user id, can be <code>null</code>
     * @param apiKey              partner id (store id). Mandatory field
     * @param httpServletRequest
     * @param httpServletResponse
     * @return
     * @throws Exception
     */
    @Valid
    @RequestMapping(method = RequestMethod.GET, value = "/assign/{apiKey}/")
    public ResponseEntity<AssignParamsResponse> assignParams(@CookieValue(value = RUID_COOKIE_NAME, required = false) String ruId,
                                                             @PathVariable("apiKey") @NotNull String apiKey,
                                                             HttpServletRequest httpServletRequest,
                                                             HttpServletResponse httpServletResponse) throws Exception {

        AssignParamsRequest request = new AssignParamsRequest(apiKey, ruId);

        log.info("Request assignParams [" + jsonMapper.writeValueAsString(request) + "]");

        validate(request);

        ParametersEntity parametersEntity = authService.assignParameters(apiKey, ruId);

        AssignParamsResponse response;

        if (parametersEntity != null) {

            //set ruId to cookie no matter it changed or not
            setCookie(httpServletResponse, RUID_COOKIE_NAME, parametersEntity.getRuId(), Integer.MAX_VALUE);
            setCookie(httpServletResponse, API_KEY_COOKIE_NAME, parametersEntity.getApiKey(), Integer.MAX_VALUE);
            response = new AssignParamsResponse(parametersEntity.getRuId(),
                    parametersEntity.getApiKey(),
                    parametersEntity.getAddAttr());
        } else {
            response = new AssignParamsResponse();
            killCookie(httpServletResponse, RUID_COOKIE_NAME);
            killCookie(httpServletResponse, API_KEY_COOKIE_NAME);
        }


        log.info("Response assignParams [" + jsonMapper.writeValueAsString(response) + "]");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
