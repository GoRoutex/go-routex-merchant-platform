package vn.com.routex.merchant.platform.interfaces.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import vn.com.go.routex.identity.security.log.SystemLog;
import vn.com.routex.merchant.platform.application.service.TicketService;
import vn.com.routex.merchant.platform.domain.ticket.model.Ticket;
import vn.com.routex.merchant.platform.infrastructure.persistence.utils.ApiRequestUtils;
import vn.com.routex.merchant.platform.infrastructure.persistence.utils.HttpUtils;
import vn.com.routex.merchant.platform.interfaces.factory.ApiResultFactory;
import vn.com.routex.merchant.platform.interfaces.model.base.BaseRequest;
import vn.com.routex.merchant.platform.interfaces.model.ticket.request.UpdateTicketRequest;
import vn.com.routex.merchant.platform.interfaces.model.ticket.response.FetchTicketDetailResponse;
import vn.com.routex.merchant.platform.interfaces.model.ticket.response.FetchTicketListResponse;
import vn.com.routex.merchant.platform.interfaces.model.ticket.response.TicketResponse;
import vn.com.routex.merchant.platform.interfaces.model.ticket.response.UpdateTicketResponse;

import java.util.List;
import java.util.stream.Collectors;

import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.*;

@RestController
@RequestMapping(API_PATH + API_VERSION + MERCHANT_SERVICE)
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ticket:management') or hasRole('ADMIN')")
public class MerchantTicketController {

    private final TicketService ticketService;
    private final ApiResultFactory apiResultFactory;
    private final SystemLog sLog = SystemLog.getLogger(this.getClass());

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder, WebRequest webRequest) {
        webDataBinder.setDisallowedFields("requestId", "requestDateTime", "channel", "data");
    }

    @PostMapping(TICKETS_PATH + UPDATE_PATH)
    public ResponseEntity<UpdateTicketResponse> updateTicket(@Valid @RequestBody UpdateTicketRequest request,
                                                             HttpServletRequest servletRequest) {
        sLog.info("[TICKET-MANAGEMENT] Update Ticket Request: {}", request);
        
        Ticket ticketUpdate = Ticket.builder()
                .customerName(request.getData().getCustomerName())
                .customerPhone(request.getData().getCustomerPhone())
                .customerEmail(request.getData().getCustomerEmail())
                .status(request.getData().getStatus())
                .build();

        Ticket result = ticketService.updateTicket(request.getData().getTicketId(), ticketUpdate);

        UpdateTicketResponse response = UpdateTicketResponse.builder()
                .result(apiResultFactory.buildSuccess())
                .data(UpdateTicketResponse.UpdateTicketResponseData.builder()
                        .ticketId(result.getId())
                        .status(result.getStatus())
                        .build())
                .build();

        return HttpUtils.buildResponse(request, response);
    }

    @GetMapping(TICKETS_PATH + DETAIL_PATH)
    public ResponseEntity<FetchTicketDetailResponse> fetchDetail(
            @RequestParam String ticketId,
            HttpServletRequest servletRequest
    ) {
        BaseRequest baseRequest = ApiRequestUtils.getBaseRequestOrDefault(servletRequest);
        Ticket result = ticketService.getTicketDetail(ticketId);

        FetchTicketDetailResponse response = FetchTicketDetailResponse.builder()
                .result(apiResultFactory.buildSuccess())
                .data(mapToTicketResponse(result))
                .build();

        return HttpUtils.buildResponse(baseRequest, response);
    }

    @GetMapping(TICKETS_PATH + FETCH_PATH)
    public ResponseEntity<FetchTicketListResponse> fetchList(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String query,
            HttpServletRequest servletRequest
    ) {
        BaseRequest baseRequest = ApiRequestUtils.getBaseRequestOrDefault(servletRequest);
        Page<Ticket> result = ticketService.getTickets(query, PageRequest.of(pageNumber - 1, pageSize));

        List<TicketResponse> items = result.getContent().stream()
                .map(this::mapToTicketResponse)
                .collect(Collectors.toList());

        FetchTicketListResponse response = FetchTicketListResponse.builder()
                .result(apiResultFactory.buildSuccess())
                .data(FetchTicketListResponse.FetchTicketListResponsePage.builder()
                        .items(items)
                        .pagination(FetchTicketListResponse.Pagination.builder()
                                .pageNumber(result.getNumber() + 1)
                                .pageSize(result.getSize())
                                .totalElements(result.getTotalElements())
                                .totalPages(result.getTotalPages())
                                .build())
                        .build())
                .build();

        return HttpUtils.buildResponse(baseRequest, response);
    }


    private TicketResponse mapToTicketResponse(Ticket ticket) {
        return TicketResponse.builder()
                .id(ticket.getId())
                .ticketCode(ticket.getTicketCode())
                .bookingId(ticket.getBookingId())
                .tripId(ticket.getTripId())
                .vehicleId(ticket.getVehicleId())
                .seatNumber(ticket.getSeatNumber())
                .customerName(ticket.getCustomerName())
                .customerPhone(ticket.getCustomerPhone())
                .customerEmail(ticket.getCustomerEmail())
                .price(ticket.getPrice())
                .status(ticket.getStatus())
                .issuedAt(ticket.getIssuedAt())
                .checkedInAt(ticket.getCheckedInAt())
                .boardedAt(ticket.getBoardedAt())
                .cancelledAt(ticket.getCancelledAt())
                .build();
    }
}
