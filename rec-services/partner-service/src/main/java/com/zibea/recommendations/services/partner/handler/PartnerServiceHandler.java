package com.zibea.recommendations.services.partner.handler;

import com.zibea.recommendations.common.handler.ServiceHandler;
import com.zibea.recommendations.common.model.Partner;
import com.zibea.recommendations.services.common.messages.partner.request.*;
import com.zibea.recommendations.services.common.messages.partner.response.PartnerResponse;
import com.zibea.recommendations.services.common.messages.partner.response.PartnerSetResponse;
import com.zibea.recommendations.services.partner.business.IPartnerService;
import com.zibea.recommendations.services.partner.business.feed.FeedActualizer;
import org.springframework.beans.factory.annotation.Autowired;

import java.rmi.ServerException;
import java.util.Set;

/**
 * @author Mikhail Bragin
 */

//todo create error handler
public class PartnerServiceHandler implements ServiceHandler<PartnerRequest, PartnerResponse> {

    @Autowired
    IPartnerService partnerService;

    @Autowired
    FeedActualizer feedActualizer;

    @Override
    public PartnerResponse handleMessage(PartnerRequest request) {

        try {

            validateRequest(request);

        } catch (ServerException e) {
            return null; //todo return error response
        }

        switch (request.getType()) {

            case PARTNER_SET_REQUEST:

                Set<Partner> partners = partnerService.getPartnerSet();
                return new PartnerSetResponse(partners);

            case PARTNER_ITEM_UPDATE_REQUEST:

                long ts = ((PartnerItemUpdateRequest) request).getTs();
                boolean allItems = ((PartnerItemUpdateRequest) request).isAllItems();
                return partnerService.getItemUpdates(ts, allItems);

            case CREATE_USER_REQUEST:

                partnerService.createUser((CreateUserRequest) request);

                break;

            case REGISTER_PARTNER_REQUEST:
                return partnerService.registerPartner((RegisterPartnerRequest) request);

            case UPDATE_PARTNER_INFO_REQUEST:

                return partnerService.updatePartnerInfo((UpdatePartnerInfoRequest) request);

            case UPDATE_PARTNER_FEED_REQUEST:
                Partner partner = partnerService.getPartner(((UpdateFeedRequest) request).getPartnerId());

                if (partner != null)
                    feedActualizer.updatePartnerFeed(partner);

            default:
                break;
        }

        return null;

    }

    @Override
    public void validateRequest(PartnerRequest request) throws ServerException {
        request.validate();
    }

}
