package com.growcorehub.version1.service;

import java.time.LocalDate;
import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import com.growcorehub.version1.entity.Certification;
import com.growcorehub.version1.entity.Job;
import com.growcorehub.version1.entity.Payment;
import com.growcorehub.version1.entity.Role;
import com.growcorehub.version1.entity.User;
import com.growcorehub.version1.repository.CertificationRepository;
import com.growcorehub.version1.repository.JobRepository;
import com.growcorehub.version1.repository.PaymentRepository;
import com.growcorehub.version1.repository.UserRepository;
import com.growcorehub.version1.util.PasswordEncoderUtil;

@Service
public class DataInitializationService implements CommandLineRunner {

	private final UserRepository userRepository;
	private final JobRepository jobRepository;
	private final CertificationRepository certificationRepository;
	private final PaymentRepository paymentRepository;

	public DataInitializationService(UserRepository userRepository, JobRepository jobRepository,
			CertificationRepository certificationRepository, PaymentRepository paymentRepository) {
		this.userRepository = userRepository;
		this.jobRepository = jobRepository;
		this.certificationRepository = certificationRepository;
		this.paymentRepository = paymentRepository;
	}

	@Override
	public void run(String... args) throws Exception {
		// Only initialize if no users exist
		if (userRepository.count() == 0) {
			initializeUsers();
			initializeJobs();
			initializeCertifications();
			initializePayments();
			System.out.println("Sample data initialized successfully!");
		}
	}

	private void initializeUsers() {
		// Create admin user
		User admin = new User();
		admin.setUsername("admin");
		admin.setEmail("admin@growcorehub.com");
		admin.setPassword(PasswordEncoderUtil.encodePassword("admin123"));
		admin.setFullName("Administrator");
		admin.setProfessionalDetails("System Administrator");
		admin.setRoles(Set.of(Role.ADMIN));
		userRepository.save(admin);

		// Create freelancer users
		User freelancer1 = new User();
		freelancer1.setUsername("john_doe");
		freelancer1.setEmail("john@example.com");
		freelancer1.setPassword(PasswordEncoderUtil.encodePassword("password123"));
		freelancer1.setFullName("John Doe");
		freelancer1.setProfessionalDetails("Full Stack Developer with 5 years experience in React and Node.js");
		freelancer1.setRoles(Set.of(Role.FREELANCER));
		userRepository.save(freelancer1);

		User freelancer2 = new User();
		freelancer2.setUsername("jane_smith");
		freelancer2.setEmail("jane@example.com");
		freelancer2.setPassword(PasswordEncoderUtil.encodePassword("password123"));
		freelancer2.setFullName("Jane Smith");
		freelancer2.setProfessionalDetails("UI/UX Designer specializing in modern web applications");
		freelancer2.setRoles(Set.of(Role.FREELANCER));
		userRepository.save(freelancer2);

		User client1 = new User();
		client1.setUsername("tech_corp");
		client1.setEmail("hr@techcorp.com");
		client1.setPassword(PasswordEncoderUtil.encodePassword("client123"));
		client1.setFullName("Tech Corp");
		client1.setProfessionalDetails("Technology company looking for talented developers");
		client1.setRoles(Set.of(Role.FREELANCER)); // Using FREELANCER role as we don't have CLIENT role
		userRepository.save(client1);
	}

	private void initializeJobs() {
		User techCorp = userRepository.findByUsername("tech_corp").orElse(null);
		User admin = userRepository.findByUsername("admin").orElse(null);

		if (techCorp != null) {
			Job job1 = new Job();
			job1.setTitle("Senior React Developer");
			job1.setDescription("We are looking for an experienced React developer to join our team. "
					+ "Requirements: 3+ years of React experience, Redux, TypeScript, REST APIs.");
			job1.setLocation("Remote");
			job1.setStatus("OPEN");
			job1.setCreatedBy(techCorp);
			jobRepository.save(job1);

			Job job2 = new Job();
			job2.setTitle("UI/UX Designer");
			job2.setDescription("Design modern and intuitive user interfaces for our web applications. "
					+ "Requirements: Figma, Adobe XD, HTML/CSS knowledge, 2+ years experience.");
			job2.setLocation("San Francisco, CA");
			job2.setStatus("OPEN");
			job2.setCreatedBy(techCorp);
			jobRepository.save(job2);
		}

		if (admin != null) {
			Job job3 = new Job();
			job3.setTitle("Full Stack Developer");
			job3.setDescription("Build end-to-end web applications using modern technologies. "
					+ "Requirements: Node.js, React, PostgreSQL, Docker experience preferred.");
			job3.setLocation("New York, NY");
			job3.setStatus("OPEN");
			job3.setCreatedBy(admin);
			jobRepository.save(job3);

			Job job4 = new Job();
			job4.setTitle("DevOps Engineer");
			job4.setDescription("Manage CI/CD pipelines and cloud infrastructure. "
					+ "Requirements: AWS, Docker, Kubernetes, Jenkins experience.");
			job4.setLocation("Remote");
			job4.setStatus("CLOSED");
			job4.setCreatedBy(admin);
			jobRepository.save(job4);
		}
	}

	private void initializeCertifications() {
		User john = userRepository.findByUsername("john_doe").orElse(null);
		User jane = userRepository.findByUsername("jane_smith").orElse(null);

		if (john != null) {
			Certification cert1 = new Certification();
			cert1.setName("React Professional Certification");
			cert1.setDescription("Advanced React concepts including hooks, context, and performance optimization");
			cert1.setUserProgress(85.0);
			cert1.setUser(john);
			certificationRepository.save(cert1);

			Certification cert2 = new Certification();
			cert2.setName("Node.js Backend Development");
			cert2.setDescription("Building scalable backend applications with Node.js and Express");
			cert2.setUserProgress(92.5);
			cert2.setUser(john);
			certificationRepository.save(cert2);

			Certification cert3 = new Certification();
			cert3.setName("AWS Cloud Practitioner");
			cert3.setDescription("Fundamentals of Amazon Web Services cloud platform");
			cert3.setUserProgress(45.0);
			cert3.setUser(john);
			certificationRepository.save(cert3);
		}

		if (jane != null) {
			Certification cert4 = new Certification();
			cert4.setName("UI/UX Design Fundamentals");
			cert4.setDescription("User interface and user experience design principles");
			cert4.setUserProgress(100.0);
			cert4.setUser(jane);
			certificationRepository.save(cert4);

			Certification cert5 = new Certification();
			cert5.setName("Advanced Figma Techniques");
			cert5.setDescription("Advanced prototyping and design systems in Figma");
			cert5.setUserProgress(78.0);
			cert5.setUser(jane);
			certificationRepository.save(cert5);
		}
	}

	private void initializePayments() {
		User john = userRepository.findByUsername("john_doe").orElse(null);
		User jane = userRepository.findByUsername("jane_smith").orElse(null);

		if (john != null) {
			Payment payment1 = new Payment();
			payment1.setUser(john);
			payment1.setAmount(1500.0);
			payment1.setStatus("COMPLETED");
			payment1.setTransactionDate(LocalDate.now().minusDays(15));
			paymentRepository.save(payment1);

			Payment payment2 = new Payment();
			payment2.setUser(john);
			payment2.setAmount(800.0);
			payment2.setStatus("COMPLETED");
			payment2.setTransactionDate(LocalDate.now().minusDays(30));
			paymentRepository.save(payment2);

			Payment payment3 = new Payment();
			payment3.setUser(john);
			payment3.setAmount(1200.0);
			payment3.setStatus("PENDING");
			payment3.setTransactionDate(LocalDate.now().minusDays(5));
			paymentRepository.save(payment3);
		}

		if (jane != null) {
			Payment payment4 = new Payment();
			payment4.setUser(jane);
			payment4.setAmount(950.0);
			payment4.setStatus("COMPLETED");
			payment4.setTransactionDate(LocalDate.now().minusDays(20));
			paymentRepository.save(payment4);

			Payment payment5 = new Payment();
			payment5.setUser(jane);
			payment5.setAmount(1100.0);
			payment5.setStatus("PROCESSING");
			payment5.setTransactionDate(LocalDate.now().minusDays(3));
			paymentRepository.save(payment5);
		}
	}
}