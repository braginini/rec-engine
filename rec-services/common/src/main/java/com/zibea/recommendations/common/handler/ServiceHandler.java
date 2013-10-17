package com.zibea.recommendations.common.handler;

import com.zibea.recommendations.services.common.messages.ServiceMessage;
import com.zibea.recommendations.services.common.messages.ServiceRequest;
import com.zibea.recommendations.services.common.messages.ServiceResponse;

import java.rmi.ServerException;

/**
 * @author Mikhail Bragin
 */
public interface ServiceHandler<T extends ServiceMessage, E extends ServiceMessage> {

    void validateRequest(T request) throws ServerException;

    E handleMessage(T request);
}
