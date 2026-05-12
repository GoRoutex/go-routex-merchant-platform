package vn.com.routex.merchant.platform.interfaces.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import vn.com.go.routex.identity.security.log.SystemLog;
import vn.com.routex.merchant.platform.application.command.ticket.CreateTicketCommand;
import vn.com.routex.merchant.platform.application.service.TicketService;
import vn.com.routex.merchant.platform.domain.ticket.model.Ticket;
import vn.com.routex.merchant.platform.infrastructure.persistence.utils.HttpUtils;
import vn.com.routex.merchant.platform.interfaces.factory.ApiResultFactory;
import vn.com.routex.merchant.platform.interfaces.model.ticket.request.CreateTicketRequest;
import vn.com.routex.merchant.platform.interfaces.model.ticket.response.CreateTicketResponse;

import java.util.List;
import java.util.stream.Collectors;

import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.API_PATH;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.API_VERSION;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.CREATE_PATH;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.INTERNAL_PATH;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.MERCHANT_SERVICE;
import static vn.com.routex.merchant.platform.infrastructure.persistence.constant.ApiConstant.TICKETS_PATH;

@RestController
@RequestMapping(API_PATH + API_VERSION + MERCHANT_SERVICE + INTERNAL_PATH)
@RequiredArgsConstructor
public class InternalMerchantTicketController {


    private final TicketService ticketService;
    private final ApiResultFactory apiResultFactory;
    private final SystemLog sLog = SystemLog.getLogger(this.getClass());

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder, WebRequest webRequest) {
        webDataBinder.setDisallowedFields("requestId", "requestDateTime", "channel", "data");
    }

    @PostMapping(TICKETS_PATH + CREATE_PATH)
    public ResponseEntity<CreateTicketResponse> createTicket(@Valid @RequestBody CreateTicketRequest request,
                                                             HttpServletRequest servletRequest) {
        sLog.info("[INTERNAL-TICKET] Create Ticket Request: {}", request);

        List<CreateTicketCommand> commands = request.getData().stream().map(data -> 
                CreateTicketCommand.builder()
                        .bookingId(data.getBookingId())
                        .bookingSeatId(data.getBookingSeatId())
                        .merchantId(data.getMerchantId())
                        .tripId(data.getTripId())
                        .vehicleId(data.getVehicleId())
                        .seatNumber(data.getSeatNumber())
                        .customerName(data.getCustomerName())
                        .customerPhone(data.getCustomerPhone())
                        .customerEmail(data.getCustomerEmail())
                        .price(data.getPrice())
                        .issuedAt(data.getIssuedAt())
                        .creator(data.getCreator())
                        .build()
        ).toList();

        List<Ticket> results = ticketService.createTickets(commands);

        CreateTicketResponse response = CreateTicketResponse.builder()
                .result(apiResultFactory.buildSuccess())
                .data(results.stream().map(result -> 
                        CreateTicketResponse.CreateTicketResponseData.builder()
                                .ticketId(result.getId())
                                .ticketCode(result.getTicketCode())
                                .bookingSeatId(result.getBookingSeatId())
                                .status(result.getStatus())
                                .build()
                ).collect(Collectors.toList()))
                .build();

        return HttpUtils.buildResponse(request, response);
    }

}

