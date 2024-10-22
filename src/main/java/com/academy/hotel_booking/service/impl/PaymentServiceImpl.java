package com.academy.hotel_booking.service.impl;

import com.academy.hotel_booking.model.entity.Payment;
import com.academy.hotel_booking.model.repository.PaymentRepository;
import com.academy.hotel_booking.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;



//    public Payment createPayment(Payment payment) {
//        return paymentRepository.save(payment);
//    }
//
//    public List<Payment> getAllPayments() {
//        return paymentRepository.findAll();
//    }
//
//    public Payment getPaymentById(Integer id) {
//        return paymentRepository.findById(id).orElse(null);
//    }
//
//    public List<Payment> getPaymentsByBooking(Booking booking) {
//        return paymentRepository.findByBooking(booking);
//    }
//
//    public List<Payment> getPaymentsByType(PaymentType paymentType) {
//        return paymentRepository.findByPaymentTypeList(paymentType);
//    }
//
//    public List<Payment> getPaymentsByTotalCost(Double totalCost) {
//        return paymentRepository.findByTotalCost(totalCost);
//    }
//
//    public List<Payment> getPaymentsByDateRange(Date startDate, Date endDate) {
//        return paymentRepository.findByBooking_CheckInDateTimeBetween(startDate, endDate);
//    }







    @Override
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    @Override
    public void savePayment(Payment payment) {
        paymentRepository.save(payment);

    }

    @Override
    public Payment getPaymentById(Integer id) {
        Optional<Payment> optionalPayment = paymentRepository.findById(id);
        Payment payment = null;
        if (optionalPayment.isPresent()) {
            payment = optionalPayment.get();
        }else throw new RuntimeException("Payment not found for id: " + id);
        return payment;
    }

    @Override
    public void deletePaymentById(Integer id) {
        paymentRepository.deleteById(id);

    }

    @Override
    public Payment getPaymentByClientId(Integer clientId) {
        return null;
    }

//    @Override
//    public List<Payment> getPaymentsByClientId(Integer clientId) {
//        return List.of();
//    }
//
//    @Override
//    public List<Payment> getPaymentsByPaymentStatus(PaymentStatus paymentStatus) {
//        return List.of();
//    }
//
//    @Override
//    public List<Payment> getPaymentsByPaymentType(PaymentType paymentType) {
//        return List.of();
//    }
}
