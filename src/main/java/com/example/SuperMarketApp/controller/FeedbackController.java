package com.example.SuperMarketApp.controller;

import com.example.SuperMarketApp.model.Feedback;
import com.example.SuperMarketApp.repository.FeedbackRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedbacks")
@CrossOrigin
public class FeedbackController {
    private final FeedbackRepository repo;

    public FeedbackController(FeedbackRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public Feedback add(@RequestBody Feedback feedback) {
        return repo.save(feedback);
    }

    @GetMapping
    public List<Feedback> getAll() {
        return repo.findAll();
    }
}
