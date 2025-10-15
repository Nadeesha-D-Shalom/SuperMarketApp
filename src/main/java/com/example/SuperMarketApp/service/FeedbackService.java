package com.example.SuperMarketApp.service;

import com.example.SuperMarketApp.model.Feedback;
import com.example.SuperMarketApp.repository.FeedbackRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepo;

    public FeedbackService(FeedbackRepository feedbackRepo) {
        this.feedbackRepo = feedbackRepo;
    }

    public List<Feedback> getFeedbacksByProductId(Long productId) {
        return feedbackRepo.findByProductId(productId);
    }

    public Feedback addFeedback(Feedback feedback) {
        return feedbackRepo.save(feedback);
    }

    public Feedback updateFeedback(Long id, Feedback updatedFeedback) {
        Feedback feedback = feedbackRepo.findById(id).orElse(null);
        if (feedback != null) {
            feedback.setUsername(updatedFeedback.getUsername());
            feedback.setComment(updatedFeedback.getComment());
            feedback.setRating(updatedFeedback.getRating());
            return feedbackRepo.save(feedback);
        }
        return null;
    }

    public boolean deleteFeedback(Long id) {
        if (feedbackRepo.existsById(id)) {
            feedbackRepo.deleteById(id);
            return true;
        }
        return false;
    }
}
