package com.safeshield.app.controller;

import com.safeshield.app.model.Complaint;
import com.safeshield.app.model.Victim;
import com.safeshield.app.repository.ComplaintRepository;
import com.safeshield.app.repository.VictimRepository;
import com.safeshield.app.repository.ChatMessageRepository;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ComplaintRepository complaintRepository;
    private final VictimRepository victimRepository;
    private final ChatMessageRepository chatRepository;

    // Constructor
    public AdminController(ComplaintRepository complaintRepository,
                           VictimRepository victimRepository,
                           ChatMessageRepository chatRepository) {

        this.complaintRepository = complaintRepository;
        this.victimRepository = victimRepository;
        this.chatRepository = chatRepository;
    }

    // ======================
    // Admin Login Page
    // ======================
    @GetMapping("/login")
    public String loginPage() {
        return "admin/login";
    }

    // ======================
    // Login Process
    // ======================
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session) {

        if(username.equals("admin") && password.equals("admin123")){
            session.setAttribute("adminUser", username);
            return "redirect:/admin/dashboard";
        }

        return "redirect:/admin/login?error";
    }

    // ======================
    // Dashboard
    // ======================
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model){

        if(session.getAttribute("adminUser") == null){
            return "redirect:/admin/login";
        }

        model.addAttribute("totalComplaints",
                complaintRepository.count());

        model.addAttribute("totalVictims",
                victimRepository.count());

        // ⭐ FIXED: Count HIGH priority chat messages
        model.addAttribute("highPriorityCases",
                chatRepository.countByPriority("HIGH"));

        return "admin/dashboard";
    }

    // ======================
    // View Complaints
    // ======================
    @GetMapping("/complaints")
    public String complaints(HttpSession session, Model model){

        if(session.getAttribute("adminUser") == null){
            return "redirect:/admin/login";
        }

        model.addAttribute("complaints",
                complaintRepository.findAll());

        return "admin/complaints";
    }

    // ======================
    // View Complaint Details
    // ======================
    @GetMapping("/view/{id}")
    public String viewComplaint(@PathVariable Long id, Model model){

        Complaint complaint = complaintRepository.findById(id).orElse(null);

        model.addAttribute("complaint", complaint);

        return "admin/view-complaint";
    }

    // ======================
    // Block Victim
    // ======================
    @PostMapping("/block/{victimId}")
    public String blockVictim(@PathVariable String victimId){

        Victim victim = victimRepository.findByVictimId(victimId).orElse(null);

        if(victim != null){
            victim.setBlocked(true);
            victimRepository.save(victim);
        }

        return "redirect:/admin/complaints";
    }

    // ======================
    // Logout
    // ======================
    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/admin/login";
    }
}