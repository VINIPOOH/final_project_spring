package ua.testing.delivery.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ua.testing.delivery.dto.BillDto;
import ua.testing.delivery.dto.BillInfoToPayDto;
import ua.testing.delivery.dto.DeliveryOrderCreateDto;
import ua.testing.delivery.entity.Bill;
import ua.testing.delivery.exception.*;

import java.util.List;
import java.util.Locale;
/**
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
@Service
public interface BillService {

    List<BillInfoToPayDto> getBillsToPayByUserID(long userId, Locale locale);

    boolean payForDelivery(long userId, long billId) throws DeliveryAlreadyPaidException, NotEnoughMoneyException;

    Bill initializeBill(DeliveryOrderCreateDto deliveryOrderCreateDto, long initiatorId) throws UnsupportableWeightFactorException, NoSuchUserException, NoSuchWayException;

    Page<BillDto> getBillHistoryByUserId(long userId, Pageable pageable);

}
