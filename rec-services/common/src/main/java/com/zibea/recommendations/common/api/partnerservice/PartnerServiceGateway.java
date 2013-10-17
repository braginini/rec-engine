package com.zibea.recommendations.common.api.partnerservice;

import com.zibea.recommendations.common.api.exception.ServiceException;
import com.zibea.recommendations.common.model.Feed;
import com.zibea.recommendations.services.common.messages.partner.request.*;
import com.zibea.recommendations.services.common.messages.partner.response.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;

/**
 * @author Mikhail Bragin
 */
public class PartnerServiceGateway extends ServiceGateway<PartnerRequest, PartnerResponse> {

    public PartnerServiceGateway(CachingConnectionFactory connectionFactory, String routingKey, int replyTimeout) {
        super(connectionFactory, routingKey, replyTimeout);
    }

    public PartnerServiceGateway(CachingConnectionFactory connectionFactory, String routingKey) {
        super(connectionFactory, routingKey);
    }

    @NotNull
    public PartnerSetResponse getPartnerSet() throws ServiceException {
        PartnerSetResponse response = (PartnerSetResponse) sendAndReceive(new PartnerSetRequest());
        return response;
    }

    @NotNull
    public PartnerItemMapResponse getPartnerItemMap() throws ServiceException {
        PartnerItemMapResponse response = (PartnerItemMapResponse) sendAndReceive(new PartnerItemMapRequest());
        return response;
    }

    public void createUser(long partnerId, String ruId, @Nullable Long partnerUserId) throws ServiceException {
        send(new CreateUserRequest(partnerId, ruId, partnerUserId));
    }

    @NotNull
    public PartnerItemUpdateResponse getPartnerItemUpdates(long ts, boolean allItems) throws ServiceException {
        PartnerItemUpdateResponse response =
                (PartnerItemUpdateResponse) sendAndReceive(new PartnerItemUpdateRequest(ts, allItems));
        return response;
    }

    @NotNull
    public RegisterPartnerResponse registerPartner(String email, String password) throws ServiceException {
        RegisterPartnerResponse response =
                (RegisterPartnerResponse) sendAndReceive(new RegisterPartnerRequest(email, password));
        return response;
    }

    @NotNull
    public UpdatePartnerInfoResponse updatePartnerInfo(long partnerId, String companyTitle,
                                                       String cartUrl, String ymlUrl) throws ServiceException {
        UpdatePartnerInfoResponse response =
                (UpdatePartnerInfoResponse) sendAndReceive(
                        new UpdatePartnerInfoRequest(partnerId, companyTitle, cartUrl, ymlUrl));
        return response;
    }

    public void updateFeed(long partnerId) throws ServiceException {
        send(new UpdateFeedRequest(partnerId));
    }
}
