package vn.com.routex.merchant.platform.interfaces.model.ticket.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import vn.com.routex.merchant.platform.domain.ticket.TicketStatus;
import vn.com.routex.merchant.platform.interfaces.model.base.BaseResponse;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class UpdateTicketResponse extends BaseResponse<UpdateTicketResponse.UpdateTicketResponseData> {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    public static class UpdateTicketResponseData {
        private String ticketId;
        private TicketStatus status;
    }
}
