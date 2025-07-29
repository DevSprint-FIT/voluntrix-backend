package com.DevSprint.voluntrix_backend.services;

import com.DevSprint.voluntrix_backend.entities.EventEntity;
import com.DevSprint.voluntrix_backend.entities.TaskEntity;
import com.DevSprint.voluntrix_backend.entities.VolunteerEntity;
import com.DevSprint.voluntrix_backend.entities.VolunteerEventParticipationEntity;
import com.DevSprint.voluntrix_backend.enums.TaskDifficulty;
import com.DevSprint.voluntrix_backend.enums.TaskStatus;
import com.DevSprint.voluntrix_backend.repositories.EventRepository;
import com.DevSprint.voluntrix_backend.repositories.TaskRepository;
import com.DevSprint.voluntrix_backend.repositories.VolunteerEventParticipationRepository;
import com.DevSprint.voluntrix_backend.repositories.VolunteerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@RequiredArgsConstructor
@Service
@Transactional
public class RewardService {
    private final VolunteerRepository volunteerRepository;
    private final TaskRepository taskRepository;
    private final EventRepository eventRepository;
    private final VolunteerEventParticipationRepository volunteerEventParticipationRepository;

    // 1. When task is created - give 2 points to host
    public void processTaskCreation(TaskEntity task) {
        EventEntity event = task.getEvent();
        VolunteerEntity eventHost = event.getEventHost();

        // Handle null eventHostRewardPoints safely
        Integer currentEventPoints = event.getEventHostRewardPoints();
        if (currentEventPoints == null) {
            currentEventPoints = 0;
        }

        Integer currentVolunteerPoints = eventHost.getRewardPoints();
        if (currentVolunteerPoints == null) {
            currentVolunteerPoints = 0;
        }

        // Update event host points
        event.setEventHostRewardPoints(currentEventPoints + 2);
        eventHost.setRewardPoints(currentVolunteerPoints + 2);
        updateVolunteerLevel(eventHost);

        // Update participation table for event host
        updateParticipationPoints(eventHost.getVolunteerId(), event.getEventId(), 2);


        eventRepository.save(event);
        volunteerRepository.save(eventHost);
    }

    // 2. When task is assigned - give 10 points to volunteer
    public void processTaskAssignment(TaskEntity task) {
        if (task.getAssignee() != null) {
            VolunteerEntity volunteer = task.getAssignee();

            Integer currentPoints = volunteer.getRewardPoints();
            if (currentPoints == null) {
                currentPoints = 0;
            }

            volunteer.setRewardPoints(currentPoints + 10);
            updateVolunteerLevel(volunteer);

            // Update participation table for assigned volunteer
            updateParticipationPoints(volunteer.getVolunteerId(), task.getEvent().getEventId(), 10);
            volunteerRepository.save(volunteer);
        }
    }

    // 3. When task is submitted (TO_DO -> IN_PROGRESS) - handle timing bonuses/penalties
    public void processTaskSubmission(TaskEntity task) {
        if (task.getTaskStatus() == TaskStatus.IN_PROGRESS && task.getAssignee() != null) {
            LocalDateTime submittedDate = task.getTaskSubmittedDate();
            LocalDateTime dueDate = task.getDueDate();
            VolunteerEntity volunteer = task.getAssignee();

            int bonusPoints = 0;

            if (submittedDate != null && submittedDate.isBefore(dueDate)) {
                // Early submission - 2 points
                bonusPoints = 2;
            } else if (submittedDate != null && submittedDate.toLocalDate().equals(dueDate.toLocalDate())) {
                // On time submission - 1 point
                bonusPoints = 1;
            } else if (submittedDate != null) {
                // Late submission - calculate penalty
                long daysLate = 0;
                if (submittedDate != null) {
                    daysLate = ChronoUnit.DAYS.between(dueDate.toLocalDate(), submittedDate.toLocalDate());
                }

                if (daysLate == 1) {
                    bonusPoints = -1; // First day late: -1 point
                } else if (daysLate == 2) {
                    bonusPoints = -2; // Second day late: -1 more point (total -2)
                } else if (daysLate > 2) {
                    // After 2 days, remove the initial 10 points and reassign task
                    bonusPoints = -10;
                    // Note: Task reassignment should be handled by the calling service
                }
            }

            // Apply bonus/penalty points safely
            Integer currentPoints = volunteer.getRewardPoints();
            if (currentPoints == null) {
                currentPoints = 0;
            }

            volunteer.setRewardPoints(currentPoints + bonusPoints);
            updateVolunteerLevel(volunteer);

            // Update participation table for submission bonus/penalty
            updateParticipationPoints(volunteer.getVolunteerId(), task.getEvent().getEventId(), bonusPoints);
            volunteerRepository.save(volunteer);

            // ADDED: Update task reward points to reflect the bonus/penalty
            Integer currentTaskPoints = task.getTaskRewardPoints();
            if (currentTaskPoints == null) {
                currentTaskPoints = 0;
            }

            task.setTaskRewardPoints(currentTaskPoints + bonusPoints);
            taskRepository.save(task); // Save the updated task
        }
    }

    // 4. When task is reviewed (IN_PROGRESS -> DONE or TO_DO)
    public void processTaskReview(TaskEntity task, TaskStatus oldStatus) {
        if (oldStatus == TaskStatus.IN_PROGRESS &&
                (task.getTaskStatus() == TaskStatus.DONE || task.getTaskStatus() == TaskStatus.TO_DO)) {

            // Give review points to event host
            EventEntity event = task.getEvent();
            VolunteerEntity eventHost = event.getEventHost();

            LocalDateTime taskReviewedDate = task.getTaskReviewedDate();
            LocalDateTime submittedDate = task.getTaskSubmittedDate();

            int hostReviewPoints = 2; // Default
            if (submittedDate != null && taskReviewedDate != null) {
                long hoursBetween = ChronoUnit.HOURS.between(submittedDate, taskReviewedDate);
                if (hoursBetween <= 24) {
                    hostReviewPoints = 4; // Quick review within 24 hours
                }
            }

            // Update host points safely
            Integer currentEventPoints = event.getEventHostRewardPoints();
            if (currentEventPoints == null) {
                currentEventPoints = 0;
            }

            Integer currentHostPoints = eventHost.getRewardPoints();
            if (currentHostPoints == null) {
                currentHostPoints = 0;
            }

            event.setEventHostRewardPoints(currentEventPoints + hostReviewPoints);
            eventHost.setRewardPoints(currentHostPoints + hostReviewPoints);
            updateVolunteerLevel(eventHost);

            // Update participation table for host review points
            updateParticipationPoints(eventHost.getVolunteerId(), event.getEventId(), hostReviewPoints);

            eventRepository.save(event);
            volunteerRepository.save(eventHost);

            // If task approved (DONE), give difficulty points to volunteer
            if (task.getTaskStatus() == TaskStatus.DONE && task.getAssignee() != null) {
                VolunteerEntity volunteer = task.getAssignee();
                int difficultyPoints = getDifficultyPoints(task.getTaskDifficulty());

                Integer currentVolunteerPoints = volunteer.getRewardPoints();
                if (currentVolunteerPoints == null) {
                    currentVolunteerPoints = 0;
                }

                volunteer.setRewardPoints(currentVolunteerPoints + difficultyPoints);
                updateVolunteerLevel(volunteer);

                // Update participation table for difficulty points
                updateParticipationPoints(volunteer.getVolunteerId(), event.getEventId(), difficultyPoints);
                volunteerRepository.save(volunteer);

                // Update task reward points to reflect the difficulty bonus
                Integer currentTaskPoints = task.getTaskRewardPoints();
                if (currentTaskPoints == null) {
                    currentTaskPoints = 0;
                }

                task.setTaskRewardPoints(currentTaskPoints + difficultyPoints);
                taskRepository.save(task);
            }
        }
    }

    // 5. When event is created - give 10 points to host
    public void processEventCreation(EventEntity event) {
        VolunteerEntity eventHost = event.getEventHost();

        // Handle null values safely
        Integer currentEventPoints = event.getEventHostRewardPoints();
        if (currentEventPoints == null) {
            currentEventPoints = 0;
        }

        Integer currentHostPoints = eventHost.getRewardPoints();
        if (currentHostPoints == null) {
            currentHostPoints = 0;
        }

        // Give 10 points for event creation
        event.setEventHostRewardPoints(currentEventPoints + 10);
        eventHost.setRewardPoints(currentHostPoints + 10);
        updateVolunteerLevel(eventHost);

        // Update participation table for event creation
        updateParticipationPoints(eventHost.getVolunteerId(), event.getEventId(), 10);

        eventRepository.save(event);
        volunteerRepository.save(eventHost);
    }

    // 6. When task is reassigned - remove points from old volunteer, give to new
    public void processTaskReassignment(TaskEntity task, Long oldAssigneeId) {
        // Remove 10 points from old volunteer
        if (oldAssigneeId != null) {
            VolunteerEntity oldVolunteer = volunteerRepository.findById(oldAssigneeId)
                    .orElse(null);
            if (oldVolunteer != null) {
                Integer currentPoints = oldVolunteer.getRewardPoints();
                if (currentPoints == null) {
                    currentPoints = 0;
                }

                oldVolunteer.setRewardPoints(currentPoints - 10);
                updateVolunteerLevel(oldVolunteer);

                // Update participation table for old volunteer (remove points)
                updateParticipationPoints(oldVolunteer.getVolunteerId(), task.getEvent().getEventId(), -10);
                volunteerRepository.save(oldVolunteer);
            }
        }

        // Give 10 points to new volunteer
        if (task.getAssignee() != null) {
            VolunteerEntity newVolunteer = task.getAssignee();

            Integer currentPoints = newVolunteer.getRewardPoints();
            if (currentPoints == null) {
                currentPoints = 0;
            }

            newVolunteer.setRewardPoints(currentPoints + 10);
            updateVolunteerLevel(newVolunteer);

            // Update participation table for new volunteer (add points)
            updateParticipationPoints(newVolunteer.getVolunteerId(), task.getEvent().getEventId(), 10);
            volunteerRepository.save(newVolunteer);
        }
    }

    // Helper method to get difficulty points
    private int getDifficultyPoints(TaskDifficulty difficulty) {
        if (difficulty == null) return 0;
        switch (difficulty) {
            case EASY: return 2;
            case MEDIUM: return 4;
            case HARD: return 6;
            case EXTREME: return 8;
            default: return 0;
        }
    }

    // Helper method to update volunteer level based on reward points
    private void updateVolunteerLevel(VolunteerEntity volunteer) {
        Integer points = volunteer.getRewardPoints();
        if (points == null) {
            points = 0;
        }

        if (points >= 1000) {
            volunteer.setVolunteerLevel(3);
        } else if (points >= 500) {
            volunteer.setVolunteerLevel(2);
        } else {
            volunteer.setVolunteerLevel(1);
        }
    }

    // Handle manual reward point changes (for admin use)
    public void processTaskRewardPointsChange(TaskEntity task, Integer oldRewardPoints) {
        if (task.getAssignee() != null) {
            VolunteerEntity volunteer = task.getAssignee();
            int pointsDifference = task.getTaskRewardPoints() - oldRewardPoints;

            Integer currentPoints = volunteer.getRewardPoints();
            if (currentPoints == null) {
                currentPoints = 0;
            }

            volunteer.setRewardPoints(currentPoints + pointsDifference);
            updateVolunteerLevel(volunteer);

            // Update participation table for manual changes
            updateParticipationPoints(volunteer.getVolunteerId(), task.getEvent().getEventId(), pointsDifference);
            volunteerRepository.save(volunteer);
        }
    }

    // Helper method to update participation table points
    private void updateParticipationPoints(Long volunteerId, Long eventId, int pointsToAdd) {
        VolunteerEventParticipationEntity participation =
                volunteerEventParticipationRepository.findByVolunteer_VolunteerIdAndEvent_EventId(volunteerId, eventId);

        if (participation != null) {
            Integer currentPoints = participation.getEventRewardPoints();
            if (currentPoints == null) {
                currentPoints = 0;
            }
            participation.setEventRewardPoints(currentPoints + pointsToAdd);
            volunteerEventParticipationRepository.save(participation);
        }
    }

    //  When task is deleted - deduct points from host and assignee
    public void processTaskDeletion(TaskEntity task) {
        EventEntity event = task.getEvent();

        // Deduct 2 points from event host for task creation
        if (event.getEventHost() != null) {
            VolunteerEntity eventHost = event.getEventHost();

            Integer currentEventPoints = event.getEventHostRewardPoints();
            if (currentEventPoints == null) {
                currentEventPoints = 0;
            }

            Integer currentHostPoints = eventHost.getRewardPoints();
            if (currentHostPoints == null) {
                currentHostPoints = 0;
            }

            // Deduct 2 points for task creation
            event.setEventHostRewardPoints(currentEventPoints - 2);
            eventHost.setRewardPoints(currentHostPoints - 2);
            updateVolunteerLevel(eventHost);

            // Update participation table for host (deduct points)
            updateParticipationPoints(eventHost.getVolunteerId(), event.getEventId(), -2);

            eventRepository.save(event);
            volunteerRepository.save(eventHost);
        }

        // Deduct 10 points from assigned volunteer if task was assigned
        if (task.getAssignee() != null) {
            VolunteerEntity volunteer = task.getAssignee();

            Integer currentPoints = volunteer.getRewardPoints();
            if (currentPoints == null) {
                currentPoints = 0;
            }

            volunteer.setRewardPoints(currentPoints - 10);
            updateVolunteerLevel(volunteer);

            // Update participation table for volunteer (deduct points)
            updateParticipationPoints(volunteer.getVolunteerId(), event.getEventId(), -10);
            volunteerRepository.save(volunteer);
        }
    }

    /**
     * Scheduled method to check for inactive volunteers
     * Runs daily to identify volunteers who became unavailable after task assignment
     */
    @Scheduled(cron = "0 0 9 * * ?") // Run daily at 9 AM
    public void checkForInactiveVolunteers() {
        LocalDateTime twoDaysAgo = LocalDateTime.now().minus(2, ChronoUnit.DAYS);

        // Find all tasks assigned (created) more than 2 days ago with TO_DO status
        // where the assigned volunteer is now unavailable
        List<TaskEntity> tasksToCheck = taskRepository.findTasksAssignedMoreThanTwoDaysAgoWithTodoStatus(twoDaysAgo);

        for (TaskEntity task : tasksToCheck) {
            // Double-check that volunteer is unavailable (query should already filter this)
            if (task.getAssignee() != null && !task.getAssignee().getIsAvailable()) {
                processInactiveVolunteer(task);
            }
        }
    }

    /**
     * Process a volunteer who became inactive after task assignment
     */
    private void processInactiveVolunteer(TaskEntity task) {
        VolunteerEntity inactiveVolunteer = task.getAssignee();
        EventEntity event = task.getEvent();

        // Check if already marked as inactive for this event
        VolunteerEventParticipationEntity participation =
                volunteerEventParticipationRepository.findByVolunteer_VolunteerIdAndEvent_EventId(
                        inactiveVolunteer.getVolunteerId(), event.getEventId());

        if (participation != null && !participation.getIsInactive()) {
            // Mark as inactive
            participation.setIsInactive(true);
            volunteerEventParticipationRepository.save(participation);

            // Deduct 10 points from inactive volunteer
            Integer currentPoints = inactiveVolunteer.getRewardPoints();
            if (currentPoints == null) {
                currentPoints = 0;
            }

            inactiveVolunteer.setRewardPoints(Math.max(0, currentPoints - 10)); // Prevent negative points
            updateVolunteerLevel(inactiveVolunteer);
            volunteerRepository.save(inactiveVolunteer);

            // Update participation table (deduct points)
            Integer currentParticipationPoints = participation.getEventRewardPoints();
            if (currentParticipationPoints == null) {
                currentParticipationPoints = 0;
            }
            participation.setEventRewardPoints(Math.max(0, currentParticipationPoints - 10));
            volunteerEventParticipationRepository.save(participation);

            // Remove assignee so event host can reassign to someone else
            task.setAssignee(null);
            taskRepository.save(task);

            // TODO: Trigger notification to event host that task is now unassigned and needs reassignment

        }
    }

    //  Handle reassignment to new volunteers

    public void processTaskAssignmentAndReactivate(TaskEntity task) {
        if (task.getAssignee() != null) {
            VolunteerEntity volunteer = task.getAssignee();

            Integer currentPoints = volunteer.getRewardPoints();
            if (currentPoints == null) {
                currentPoints = 0;
            }

            volunteer.setRewardPoints(currentPoints + 10);
            updateVolunteerLevel(volunteer);

            // Update participation table for assigned volunteer
            updateParticipationPoints(volunteer.getVolunteerId(), task.getEvent().getEventId(), 10);

            // If this volunteer was previously inactive for this event, reactivate them
            reactivateVolunteer(volunteer.getVolunteerId(), task.getEvent().getEventId());

            volunteerRepository.save(volunteer);
        }
    }

    /**
     * Reactivate an inactive volunteer for a specific event
     */
    public void reactivateVolunteer(Long volunteerId, Long eventId) {
        VolunteerEventParticipationEntity participation =
                volunteerEventParticipationRepository.findByVolunteer_VolunteerIdAndEvent_EventId(volunteerId, eventId);

        if (participation != null && participation.getIsInactive()) {
            participation.setIsInactive(false);
            volunteerEventParticipationRepository.save(participation);
        }
    }
}

