package com.reservation_system.controller;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.reservation_system.entity.Bus;
import com.reservation_system.entity.Reservation;
import com.reservation_system.entity.User;
import com.reservation_system.service.BusService;
import com.reservation_system.service.ReservationService;
import com.reservation_system.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private BusService busService;

	@Autowired
	private ReservationService reservationService;

	@GetMapping("/")
	@Operation
	public String home(Principal principal, Model model) {
		if (principal != null) {
			model.addAttribute("loggedIn", true);
		} else {
			model.addAttribute("loggedIn", false);
		}
		return "HomePage";
	}

	@GetMapping("/user/register")
	@Operation
	public String userRegister(Model model) {
		User user = new User();
		model.addAttribute("userReg", user);
		return "Registration";
	}

	@PostMapping("/user/register")
	@Operation
	public String postUserRegister(@ModelAttribute("userLog") User user, Model model) {
		if (userService.findByUserEmail(user.getEmail())) {
			model.addAttribute("exist", "This " + user.getEmail() + " already exist");
			return "Registration";
		} else if (userService.save(user)) {
			return "redirect:/user/login";
		}
		model.addAttribute("error", "error while creating your account try again :)");
		return "Registration";
	}

	@GetMapping("/user/login")
	@Operation
	public String userLogin(Model model) {
		User user = new User();
		model.addAttribute("userLog", user);
		return "Login";
	}

	@PostMapping("/user/login")
	@Operation
	public String postUserLogin(@ModelAttribute("userLog") User user, Model model, HttpServletResponse response) {
		if (userService.verify(user, response)) {
			return "redirect:/";
		}
		System.out.println("not verified");
		model.addAttribute("error", "Invalid email or password");
		return "Login";
	}

	@GetMapping("/user/profile")
	@Operation
	public String userProfile(Model model, Principal principal) {
		String email = principal.getName();
		User user = userService.findByEmail(email);
		model.addAttribute("user", user);
		List<Reservation> reservations = reservationService.getByUserEmail(email);
		model.addAttribute("reservations", reservations);
		List<String> bus = reservations.stream().map(Reservation::getBusNumber).collect(Collectors.toList());
		List<Bus> buses = busService.getBusesByBusNumber(bus);
		model.addAttribute("buses", buses);
		return "UserProfile";
	}

	@GetMapping("/user/edit/{id}")
	@Operation
	public String editProfile(@PathVariable int id, Model model) {
		model.addAttribute("user", userService.getUserById(id));
		return "EditPage";
	}

	@PostMapping("/user/edit/{id}")
	@Operation
	public String postEditPage(@ModelAttribute("user") User user, @PathVariable int id) {
		User users = userService.getUserById(id);
		users.setId(id);
		users.setUserName(user.getUserName());
		users.setPhone(user.getPhone());
		users.setLocation(user.getLocation());
		userService.saveUpdates(users);
		return "redirect:/user/profile";
	}

	@GetMapping("/user/bus/search")
	@Operation
	public String displayBus(Model model, @RequestParam("from") String from, @RequestParam("to") String to,
			@RequestParam("date") String date) {
		model.addAttribute("buses", busService.getBuses(from, to, date));
		return "BusDisplayPage";
	}

	@GetMapping("/user/bus/book/{id}")
	@Operation
	public String bookBus(@PathVariable int id, Model model) {
		Reservation reservation = new Reservation();
		Bus buses = busService.findById(id);
		model.addAttribute("reserve", reservation);
		model.addAttribute("buses", buses);
		return "BusBookingPage";
	}

	@PostMapping("/user/bus/book/")
	@Operation
	public String postBookBus(@ModelAttribute("reserve") Reservation reservation, Principal principal,
			RedirectAttributes redirect) {
		String email = principal.getName();
		User user = userService.findByEmail(email);
		reservation.setUserEmail(user.getEmail());
		if (reservationService.save(reservation)) {
			redirect.addFlashAttribute("success",
					"Your appointment id " + reservation.getBookingId() + " is successfull");
			return "redirect:/user/bus/confirmation";
		}
		return "BusBookingPage";
	}

	@GetMapping("/user/bus/confirmation")
	@Operation
	public String bookingConfirmation() {
		return "BookingConfirmationPage";
	}
}
