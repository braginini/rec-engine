package com.zibea.recommendations.webserver.core.business.impl;

import com.zibea.recommendations.common.api.exception.ServiceException;
import com.zibea.recommendations.common.api.partnerservice.PartnerServiceGateway;
import com.zibea.recommendations.webserver.core.business.IAuthorizationService;
import com.zibea.recommendations.webserver.core.business.PartnerInfoActualizer;
import com.zibea.recommendations.webserver.core.business.entity.ParametersEntity;
import com.zibea.recommendations.webserver.core.business.exception.BusinessException;
import com.zibea.recommendations.webserver.core.business.util.BusinessUtils;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Mikhail Bragin
 */
@Service
public class AuthorizationService implements IAuthorizationService {

    private static final Logger log = LoggerFactory.getLogger(AuthorizationService.class);

    @Autowired
    PartnerInfoActualizer actualizer;

    @Autowired
    PartnerServiceGateway partnerGateway;


    @Override
    @Nullable
    public ParametersEntity assignParameters(String apiKey, String ruId) throws BusinessException {

        if (log.isDebugEnabled())
            log.debug("Business assignParameters started [apiKey=" + apiKey + "::ruId=" + ruId + "]");

        ParametersEntity entity = checkUserKeyParam(apiKey, ruId);

        if (log.isDebugEnabled())
            log.debug("Business assignParameters returning [entity=" + entity + "]");

        return entity;

    }

    /**
     * Checks whether user unique key was sent by client or not and acts proper
     *
     * @param apiKey
     * @param ruId
     * @return {@link ParametersEntity} object representing client parameters
     * @throws BusinessException
     */
    private ParametersEntity checkUserKeyParam(String apiKey, String ruId) throws BusinessException {

        //check partner existence
        if (!actualizer.hasPartner(apiKey))
            return null;

        long partnerId = actualizer.getPartner(apiKey);

        if (ruId == null || ruId.isEmpty()) {

            //user unique key does no exists -> generate new one and create user
            ruId = BusinessUtils.generateUserIdString();

            try {
                partnerGateway.createUser(partnerId, ruId, null);
            } catch (ServiceException e) {
                log.error("Error while creating user");
            }

            return new ParametersEntity(apiKey, ruId);
        } else {
            return new ParametersEntity(apiKey, ruId);
        }
    }
}
