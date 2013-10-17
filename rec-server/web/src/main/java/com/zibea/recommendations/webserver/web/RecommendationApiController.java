package com.zibea.recommendations.webserver.web;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * @author Mikhail Bragin
 */
@Controller
@RequestMapping("/rec/api")
public class RecommendationApiController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(RecommendationApiController.class);

    /*@Autowired
    IRecommendationService recommendationService;


    @Valid
    @RequestMapping(method = RequestMethod.GET, value = "/view/{apiKey}/{itemid}")
    public ResponseEntity<RecommendationApiResponse> getViewRecommendations(@CookieValue(value = RUID_COOKIE_NAME, required = true)
                                                                            @NotNull String ruId,
                                                                            @PathVariable("apiKey") @NotNull String apiKey,
                                                                            @PathVariable("itemid") @NotNull Long itemId,
                                                                            HttpServletRequest httpServletRequest,
                                                                            HttpServletResponse httpServletResponse) throws Exception {


        GetRecommendationsRequest request = new GetRecommendationsRequest(Similarity.RecommendationType.VIEW,
                apiKey, ruId, itemId);
        log.info("Request getViewRecommendations [" + jsonMapper.writeValueAsString(request) + "]");

        validate(request);

        List<Recommendation> recommendations = recommendationService.getRecommendations(request.getApiKey(),
                request.getRuId(), request.getApiKey(), request.getType(), request.getItemId());

        RecommendationApiResponse response = new RecommendationApiResponse(recommendations);

        log.info("Response getViewRecommendations [" + jsonMapper.writeValueAsString(response) + "]");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }*/


}
