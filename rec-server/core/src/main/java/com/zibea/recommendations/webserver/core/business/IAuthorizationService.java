package com.zibea.recommendations.webserver.core.business;

import com.zibea.recommendations.webserver.core.business.entity.ParametersEntity;
import org.jetbrains.annotations.Nullable;

/**
 * @author Mikhail Bragin
 */
public interface IAuthorizationService {

    @Nullable
    ParametersEntity assignParameters(String apiKey, String ruId) throws Exception;
}
