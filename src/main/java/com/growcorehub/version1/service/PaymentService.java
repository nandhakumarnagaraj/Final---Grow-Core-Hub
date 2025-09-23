package com.growcorehub.version1.service;

import org.springframework.stereotype.Service;

import com.growcorehub.version1.entity.Payment;
import com.growcorehub.version1.repository.PaymentRepository;

import java.util.List;

@Service
public class PaymentService {
	private final PaymentRepository paymentRepository;

	public PaymentService(PaymentRepository paymentRepository) {
		this.paymentRepository = paymentRepository;
	}

	public List<Payment> getPaymentsByUserId(Long userId) {
		return paymentRepository.findByUserId(userId);
	}

	public Payment savePayment(Payment payment) {
		return paymentRepository.save(payment);
	}
}
