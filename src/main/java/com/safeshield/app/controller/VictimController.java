package com.safeshield.app.controller;

import com.safeshield.app.model.Victim;
import com.safeshield.app.model.Complaint;
import com.safeshield.app.model.ChatMessage;
import com.safeshield.app.repository.ChatMessageRepository;
import com.safeshield.app.service.VictimService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;


@Controller
@RequestMapping("/victim")
public class VictimController {

    private final VictimService victimService;
    private final ChatMessageRepository chatRepository;

    public VictimController(VictimService victimService,
                            ChatMessageRepository chatRepository) {
        this.victimService = victimService;
        this.chatRepository = chatRepository;
    }

    // ===============================
    // Login Page
    // ===============================
    @GetMapping("/login")
    public String loginPage() {
        return "victim/login";
    }

    // ===============================
    // Handle Login
    // ===============================
    @PostMapping("/login")
    public String loginSubmit(@RequestParam String victimId,
                              @RequestParam String recoveryCode,
                              Model model) {

        if (victimService.validateLogin(victimId, recoveryCode)) {
            return "redirect:/victim/dashboard?victimId=" + victimId;
        }

        model.addAttribute("error", "Invalid Victim ID or Recovery Code");
        return "victim/login";
    }

    // ===============================
    // Register Victim
    // ===============================
    @GetMapping("/register")
    public String register(Model model) {

        Victim victim = victimService.createVictimAccount();

        model.addAttribute("victimId", victim.getVictimId());
        model.addAttribute("recoveryCode", victim.getRecoveryCode());

        return "victim/register-success";
    }

    // ===============================
    // Dashboard
    // ===============================
    @GetMapping("/dashboard")
    public String dashboard(@RequestParam(required = false) String victimId,
                            Model model) {

        if (victimId == null) {
            return "redirect:/victim/login";
        }

        model.addAttribute("victimId", victimId);
        model.addAttribute("complaints",
                victimService.getVictimComplaints(victimId));

        // Latest counsellor message
        ChatMessage latestMessage =
                chatRepository.findTop1ByVictimIdAndSenderOrderByTimestampDesc(
                        victimId, "COUNSELLOR");

        if (latestMessage != null) {
            model.addAttribute("counsellorMessage", latestMessage);
        }

        return "victim/dashboard";
    }

    // ===============================
    // Complaint Form
    // ===============================
    @GetMapping("/complaint/new")
    public String newComplaint(@RequestParam String victimId,
                               Model model) {

        model.addAttribute("victimId", victimId);
        return "victim/complaint-form";
    }

    // ===============================
    // File Complaint
    // ===============================
    @PostMapping("/file-complaint")
    public String fileComplaint(@RequestParam String victimId,
                                @RequestParam String type,
                                @RequestParam String description,
                                @RequestParam(required = false) Double latitude,
                                @RequestParam(required = false) Double longitude,
                                @RequestParam(value = "file", required = false) MultipartFile file,
                                Model model) {

        try {

            String filePath = null;

            if (file != null && !file.isEmpty()) {

                String uploadDir = System.getProperty("user.dir")
                        + File.separator + "uploads";

                File directory = new File(uploadDir);

                if (!directory.exists()) {
                    directory.mkdirs();
                }

                String originalFileName = file.getOriginalFilename();
                String cleanedFileName = originalFileName.replaceAll("\\s+", "_");

                String fileName = System.currentTimeMillis() + "_" + cleanedFileName;

                File destination = new File(directory, fileName);

                file.transferTo(destination);

                filePath = "uploads/" + fileName;
            }

            victimService.fileComplaint(
                    victimId,
                    type,
                    description,
                    filePath,
                    latitude,
                    longitude
            );

            // Automatic counsellor welcome message
            ChatMessage welcome = new ChatMessage();
            welcome.setVictimId(victimId);
            welcome.setSender("COUNSELLOR");
            welcome.setMessage(
                    "Hello. I'm a counsellor from SafeShield. " +
                            "You are not alone. If you feel scared or stressed, " +
                            "you can talk to me anytime."
            );

            chatRepository.save(welcome);

            return "redirect:/victim/dashboard?victimId=" + victimId;

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Complaint submission failed.");
            model.addAttribute("victimId", victimId);
            return "victim/complaint-form";
        }
    }

    // ===============================
    // View Complaint
    // ===============================
    @GetMapping("/complaint/{id}")
    public String viewComplaint(@PathVariable Long id,
                                @RequestParam(required = false) String victimId,
                                Model model) {

        if (victimId == null) {
            return "redirect:/victim/login";
        }

        Complaint complaint = victimService.getComplaintById(id);

        if (complaint == null) {
            return "redirect:/victim/dashboard?victimId=" + victimId;
        }

        model.addAttribute("victimId", victimId);
        model.addAttribute("complaint", complaint);

        return "victim/complaint-details";
    }

    // ===============================
    // Chat Page
    // ===============================
    @GetMapping("/chat")
    public String chatPage(@RequestParam String victimId, Model model) {

        model.addAttribute("victimId", victimId);
        model.addAttribute("messages",
                chatRepository.findByVictimIdOrderByTimestampAsc(victimId));

        return "victim/chat";
    }

    // ===============================
    // Send Message
    // ===============================
    @PostMapping("/send-message")
    public String sendMessage(@RequestParam String victimId,
                              @RequestParam String message) {

        ChatMessage victimMsg = new ChatMessage();
        victimMsg.setVictimId(victimId);
        victimMsg.setSender("VICTIM");
        victimMsg.setMessage(message);

// default priority
        victimMsg.setPriority("NORMAL");

        String msg = message.toLowerCase();

        if(msg.contains("die") ||
                msg.contains("suicide") ||
                msg.contains("kill myself") ||
                msg.contains("depressed") ||
                msg.contains("hopeless")){

            victimMsg.setPriority("HIGH");
        }

        chatRepository.save(victimMsg);


        ChatMessage reply = new ChatMessage();
        reply.setVictimId(victimId);
        reply.setSender("COUNSELLOR");
        reply.setMessage(generateReply(message));
        reply.setPriority("NORMAL");

        chatRepository.save(reply);

        return "redirect:/victim/chat?victimId=" + victimId;
    }

    // ===============================
    // Smart Reply
    // ===============================
    private String generateReply(String message) {

        message = message.toLowerCase();

        if (message.contains("scared") || message.contains("afraid")) {
            return "It's okay to feel scared. You are safe here and we will support you.";
        }

        if (message.contains("help")) {
            return "You did the right thing by reaching out. We will guide you through this.";
        }

        if (message.contains("hopeless")) {
            return "You are stronger than you think. There are people ready to help you.";
        }

        if (message.contains("thank")) {
            return "You're welcome. I'm always here if you need to talk.";
        }

        return "I understand. Please share whatever you feel comfortable with.";
    }

    // ===============================
    // Logout
    // ===============================
    @GetMapping("/logout")
    public String logout() {
        return "redirect:/victim/login";
    }
}