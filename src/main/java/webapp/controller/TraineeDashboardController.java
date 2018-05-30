package webapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import webapp.exception.ForbiddenException;
import webapp.models.*;
import org.springframework.web.bind.annotation.GetMapping;
import webapp.repos.AssesmentRepository;
import webapp.repos.SessionRepository;
import webapp.repos.UserRepository;
import webapp.repos.WorkoutRepository;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Controller
public class TraineeDashboardController {

    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WorkoutRepository workoutRepository;
    @Autowired
    private AssesmentRepository assesmentRepository;

    @GetMapping(value = "/trainees/{traineeId}/dashboard/")
    public String getSession(@PathVariable String traineeId, @CookieValue(value = "u_id") String userId, Model model) throws Exception {
        Optional<User> optionalTrainer = userRepository.findById(userId);
        Trainer trainer;
        if (optionalTrainer.isPresent()) {
            trainer = (Trainer) optionalTrainer.get();
        } else {
            throw new ForbiddenException();
        }
        Optional<User> optionalUser = userRepository.findById(traineeId);
        if (optionalUser.isPresent()) {
            Trainee trainee = (Trainee) optionalUser.get();
            List<Session> sessions = sessionRepository.findByTraineeAndIsCompleted(trainee, true, new Sort(Sort.Direction.DESC, "completeDate"));
            model.addAttribute("sessionCountsByMonth", new SessionsByMonthAggregation(sessions));
            model.addAttribute("rewardPointsByMonth", new RewardPointsByMonthlyAggregation(sessions));
            List<Assesment> assesments = assesmentRepository.findByCreatedByAndTrainee(trainer, trainee,
                    new Sort(Sort.Direction.DESC, "createdDate"));
            model.addAttribute("assessmentMetrics", new AssessmentAggregator(assesments));

        }
        return "dashboard";
    }

    private class SessionsByMonthAggregation {

        private final List<Session> sessions;
        private Map<Integer, Long> _map;

        public SessionsByMonthAggregation(List<Session> sessions) {
            this.sessions = sessions;
        }

        public Map<Integer, Long> getSessionCountsMonthly() {
            if (_map != null) {
                return _map;
            }
            Map<Integer,Long> map = sessions.stream().map(session -> {
                LocalDate date = session.getCreatedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                return date.getMonthValue();
            }).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
            TreeMap<Integer, Long> sorted = new TreeMap<>(map);
            _map = sorted;
            return sorted;
        }

        public String getMonthValues() {
            List<String> values = getSessionCountsMonthly().values().stream().map(String::valueOf).collect(Collectors.toList());
            return String.join(",", values);
        }

        public String getMonthNames() {
            DateFormatSymbols dfs = new DateFormatSymbols();
            String[] months = dfs.getMonths();

            List<String> monthNames = getSessionCountsMonthly().keySet().stream().map(monthValue -> '\'' + months[monthValue-1] + '\'').collect(Collectors.toList());
            return String.join(",", monthNames);
        }
    }

    private class RewardPointsByMonthlyAggregation {

        private final List<Session> sessions;
        private Map<Integer, Integer> _map;

        public RewardPointsByMonthlyAggregation(List<Session> sessions) {
            this.sessions = sessions;
        }

        public Map<Integer, Integer> getRewardPointsByMonth() {
            if (_map != null) {
                return _map;
            }
            Map<LocalDateTime, Integer> map = sessions.stream().collect(Collectors.toMap(session -> {
                        return session.getCreatedDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                    },
                    Session::getCompletedRewardingPoint));
            Map<Integer, Integer> m = map.entrySet().stream().map(e -> new AbstractMap.SimpleEntry<>(e.getKey().getMonthValue(), e.getValue())).
                    collect(Collectors.groupingBy(AbstractMap.SimpleEntry::getKey,
                            Collectors.summingInt(AbstractMap.SimpleEntry::getValue)));
            TreeMap<Integer, Integer> sorted = new TreeMap<>(m);
            _map = sorted;
            return sorted;
        }

        public String getMonthValues() {
            List<String> values = getRewardPointsByMonth().values().stream().map(String::valueOf).collect(Collectors.toList());
            return String.join(",", values);
        }

        public String getMonthNames() {
            DateFormatSymbols dfs = new DateFormatSymbols();
            String[] months = dfs.getMonths();

            List<String> monthNames = getRewardPointsByMonth().keySet().stream().map(monthValue -> '\'' + months[monthValue-1] + '\'').collect(Collectors.toList());
            return String.join(",", monthNames);
        }
    }

    private class AssessmentAggregator {

        private class MetricOptions {
            public String label;

            public MetricOptions(String label) {
                this.label = label;
            }
        }

        private final List<Assesment> assessments;
        private Map<String, List<Integer>> _map;

        public AssessmentAggregator(List<Assesment> assessments) {
            this.assessments = assessments;
        }

        public Map<String, List<Integer>> getAssessmentMetrics() {
            if (_map != null) {
                return _map;
            }
            Map<String, List<Integer>> m = new HashMap<>();
            m.put("Weight", assessments.stream().map(Assesment::getWeight).collect(Collectors.toList()));
            m.put("Heartrate", assessments.stream().map(Assesment::getHeartRate).collect(Collectors.toList()));
            m.put("Height", assessments.stream().map(Assesment::getHeight).collect(Collectors.toList()));
            m.put("Hipsize", assessments.stream().map(Assesment::getHipsSize).collect(Collectors.toList()));
            m.put("Armsize", assessments.stream().map(Assesment::getArmSize).collect(Collectors.toList()));
            m.put("Chestsize", assessments.stream().map(Assesment::getChestSize).collect(Collectors.toList()));
            m.put("Waistsize", assessments.stream().map(Assesment::getWaistSize).collect(Collectors.toList()));
            m.put("Minutes p/km", assessments.stream().map(Assesment::getMinutesKm).collect(Collectors.toList()));
            _map = new TreeMap<>(m);
            return _map;
        }

        public String getMetrics() {
            getAssessmentMetrics();
            System.out.println(_map.get("Weight"));
            List<String> metrics = _map.values().stream().
                    map(values -> "[" +
                            values.stream().map(String::valueOf).collect(Collectors.joining(",")) +
                            "]").collect(Collectors.toList());
            return String.join(",", metrics);
        }

        public String getMetricNames() throws IOException {
            getAssessmentMetrics();
            List<MetricOptions> metrics = _map.keySet().stream().map(name -> new MetricOptions(name)).collect(Collectors.toList());
            ObjectMapper mapper = new ObjectMapper();
            OutputStream stream = new ByteArrayOutputStream();
            mapper.writeValue(stream, metrics);
            return stream.toString();
        }

        public String getMetricDates() {
            getAssessmentMetrics();
            DateFormat format = DateFormat.getDateInstance(DateFormat.LONG);
            List<String> dates = assessments.stream().map(a -> "'" + format.format(a.getCreatedDate()) + "'").collect(Collectors.toList());
            return String.join(",", dates);
        }

    }

}
