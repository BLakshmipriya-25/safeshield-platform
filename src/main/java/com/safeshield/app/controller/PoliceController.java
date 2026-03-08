package com.safeshield.app.controller;

import com.safeshield.app.model.Complaint;
import com.safeshield.app.repository.ComplaintRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/police")
public class PoliceController {

    private final ComplaintRepository complaintRepository;

    public PoliceController(ComplaintRepository complaintRepository) {
        this.complaintRepository = complaintRepository;
    }

    // Show Login Page
    @GetMapping("/login")
    public String showLogin() {
        return "police/login";
    }

    // Process Login
    @PostMapping("/login")
    public String processLogin(@RequestParam String officerId,
                               @RequestParam String password,
                               HttpSession session) {

        if (officerId.equals("OFF001") && password.equals("police123")) {
            session.setAttribute("policeUser", officerId);
            return "redirect:/police/dashboard";
        }

        return "redirect:/police/login?error";
    }

    // Dashboard
    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {

        if (session.getAttribute("policeUser") == null) {
            return "redirect:/police/login";
        }

        List<Complaint> complaints = complaintRepository.findAll();

        model.addAttribute("complaints", complaints);
        model.addAttribute("total", complaintRepository.count());
        model.addAttribute("underReview", complaintRepository.countByStatus("Under Review"));
        model.addAttribute("investigation", complaintRepository.countByStatus("Investigation"));
        model.addAttribute("closed", complaintRepository.countByStatus("Closed"));

        return "police/dashboard";
    }

    // View Case
    @GetMapping("/view/{id}")
    public String viewComplaint(@PathVariable Long id, Model model, HttpSession session) {

        if (session.getAttribute("policeUser") == null) {
            return "redirect:/police/login";
        }

        Complaint complaint = complaintRepository.findById(id).orElse(null);

        if (complaint == null) {
            return "redirect:/police/dashboard";
        }

        model.addAttribute("complaint", complaint);
        return "police/view";
    }

    // Update Status
    @PostMapping("/update-status/{id}")
    public String updateStatus(@PathVariable Long id,
                               @RequestParam String status,
                               HttpSession session) {

        if (session.getAttribute("policeUser") == null) {
            return "redirect:/police/login";
        }

        Complaint complaint = complaintRepository.findById(id).orElse(null);

        if (complaint != null) {
            complaint.setStatus(status);
            complaintRepository.save(complaint);
        }

        return "redirect:/police/view/" + id;
    }





    // Logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/police/login";
    }
}