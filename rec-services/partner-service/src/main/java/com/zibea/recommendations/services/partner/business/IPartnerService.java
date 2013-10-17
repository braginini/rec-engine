package com.zibea.recommendations.services.partner.business;

import com.zibea.recommendations.common.model.Partner;
import com.zibea.recommendations.services.common.messages.partner.request.CreateUserRequest;
import com.zibea.recommendations.services.common.messages.partner.request.RegisterPartnerRequest;
import com.zibea.recommendations.services.common.messages.partner.request.UpdatePartnerInfoRequest;
import com.zibea.recommendations.services.common.messages.partner.response.PartnerItemUpdateResponse;
import com.zibea.recommendations.services.common.messages.partner.response.RegisterPartnerResponse;
import com.zibea.recommendations.services.common.messages.partner.response.UpdatePartnerInfoResponse;
import com.zibea.recommendations.common.model.Feed;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

/**
 * @author Mikhail Bragin
 */
//todo redo all methods to accept PartnerRequests
public interface IPartnerService {

    @Nullable
    Set<Partner> getPartnerSet();

    @NotNull
    PartnerItemUpdateResponse getItemUpdates(long ts, boolean allItems);

    /**
     * Deprecated, use {@link this#createUser(
     * com.zibea.recommendations.services.common.messages.partner.request.CreateUserRequest)}
     *
     * @param partnerId
     * @param ruId
     * @param partnerUserId
     */
    @Deprecated
    void createUser(long partnerId, String ruId, Long partnerUserId);

    @NotNull
    RegisterPartnerResponse registerPartner(RegisterPartnerRequest request);

    void createUser(@NotNull CreateUserRequest request);

    @NotNull
    UpdatePartnerInfoResponse updatePartnerInfo(UpdatePartnerInfoRequest request) ;

    void updateFeed(@NotNull Feed feed);

    @Nullable
    Partner getPartner(long partnerId);
}
