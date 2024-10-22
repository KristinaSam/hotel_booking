package com.academy.hotel_booking.service;

import com.academy.hotel_booking.model.entity.Payment;

import java.util.List;

public interface PaymentService {
    List<Payment> getAllPayments();
    void savePayment(Payment payment);
    Payment getPaymentById(Integer id);
    void deletePaymentById(Integer id);
    Payment getPaymentByClientId(Integer clientId);
//    List<Payment> getPaymentsByClientId(Integer clientId);
//    List<Payment> getPaymentsByPaymentStatus(PaymentStatus paymentStatus);
//    List<Payment> getPaymentsByPaymentType(PaymentType paymentType);
}
