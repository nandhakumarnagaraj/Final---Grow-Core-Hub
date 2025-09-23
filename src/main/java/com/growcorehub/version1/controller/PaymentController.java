package com.growcorehub.version1.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.growcorehub.version1.entity.Payment;
import com.growcorehub.version1.service.PaymentService;
import com.growcorehub.version1.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

	private final PaymentService paymentService;
	private final UserService userService;

	public PaymentController(PaymentService paymentService, UserService userService) {
		this.paymentService = paymentService;
		this.userService = userService;
	}

	@GetMapping("/")
	public ResponseEntity<List<Payment>> getUserPayments(Authentication authentication) {
		String username = authentication.getName();
		var user = userService.findByUsername(username);
		if (user.isPresent()) {
			List<Payment> payments = paymentService.getPaymentsByUserId(user.get().getId());
			return ResponseEntity.ok(payments);
		}
		return ResponseEntity.notFound().build();
	}

	@GetMapping("/{id}")
	public ResponseEntity<Payment> getPaymentById(@PathVariable Long id, Authentication authentication) {
		// Add logic to ensure user can only access their own payments
		String username = authentication.getName();
		var user = userService.findByUsername(username);
		if (user.isPresent()) {
			List<Payment> userPayments = paymentService.getPaymentsByUserId(user.get().getId());
			Payment payment = userPayments.stream().filter(p -> p.getId().equals(id)).findFirst().orElse(null);

			if (payment != null) {
				return ResponseEntity.ok(payment);
			}
		}
		return ResponseEntity.notFound().build();
	}

	@PostMapping("/")
	public ResponseEntity<Payment> createPayment(@RequestBody Payment payment, Authentication authentication) {
		String username = authentication.getName();
		var user = userService.findByUsername(username);
		if (user.isPresent()) {
			payment.setUser(user.get());
			Payment savedPayment = paymentService.savePayment(payment);
			return ResponseEntity.ok(savedPayment);
		}
		return ResponseEntity.badRequest().build();
	}
}