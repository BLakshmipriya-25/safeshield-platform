package com.safeshield.app.controller;

import com.safeshield.app.model.ChatMessage;
import com.safeshield.app.repository.ChatMessageRepository;

import jakarta.servlet.http.HttpSession;
import com.safeshield.app.model.ChatMessage;
import com.safeshield.app.repository.ChatMessageRepository;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/counsellor")
public class CounsellorController {

    private final ChatMessageRepository chatRepository;

    public CounsellorController(ChatMessageRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    // ===============================
    // Login Page
    // ===============================
    @GetMapping("/login")
    public String loginPage() {
        return "counsellor/login";
    }

    // ===============================
    // Login Process
    // ===============================
    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session) {

        if (username.equals("counsellor") && password.equals("support123")) {
            session.setAttribute("counsellorUser", username);
            return "redirect:/counsellor/dashboard";
        }

        return "redirect:/counsellor/login?error";
    }

    // ===============================
    // Dashboard
    // ===============================
    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {

        if (session.getAttribute("counsellorUser") == null) {
            return "redirect:/counsellor/login";
        }

        model.addAttribute("messages", chatRepository.findUniqueVictimIds());

        return "counsellor/dashboard";
    }

    // ===============================
    // Send Message to Victim
    // ===============================
    @PostMapping("/send-message")
    public String sendMessage(@RequestParam String victimId,
                              @RequestParam String message) {

        ChatMessage msg = new ChatMessage();

        msg.setVictimId(victimId);
        msg.setSender("COUNSELLOR");
        msg.setMessage(message);

        chatRepository.save(msg);

        return "redirect:/counsellor/chat?victimId=" + victimId;
    }

    @GetMapping("/chat")
    public String chat(@RequestParam String victimId,
                       Model model,
                       HttpSession session) {

        if (session.getAttribute("counsellorUser") == null) {
            return "redirect:/counsellor/login";
        }

        model.addAttribute("victimId", victimId);

        model.addAttribute("messages",
                chatRepository.findByVictimIdOrderByTimestampAsc(victimId));

        return "counsellor/chat";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {

        session.invalidate();

        return "redirect:/counsellor/login";
    }
}