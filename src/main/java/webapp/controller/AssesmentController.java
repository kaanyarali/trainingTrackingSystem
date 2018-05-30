package webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import webapp.mappings.AssessmentMapping;
import webapp.models.*;
import webapp.repos.AssesmentRepository;
import webapp.repos.UserRepository;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@Controller
public class AssesmentController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AssesmentRepository assesmentRepository;

    @PostMapping(value = "/Assessment",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String addTrainee(@CookieValue(value = "u_id")String userId , AssessmentMapping assesmentMapping,
                             BindingResult result, HttpServletResponse response)
    {
       Optional<User> user= userRepository.findById(userId);
        if(user.isPresent())
        {
            Optional<User> userOptional=userRepository.findById(assesmentMapping.getTraineeId());
            System.out.println(userOptional.get().getUsername());
            Assesment assesment =new Assesment(assesmentMapping.getWeight(),assesmentMapping.getHeight(),assesmentMapping.getArmSize(),
                    assesmentMapping.getChestSize(),assesmentMapping.getWaistSize(),assesmentMapping.getHipsSize(),assesmentMapping.getHeartRate(),
                    assesmentMapping.getMinutesKm(),(Trainer) user.get(),(Trainee) userOptional.get());
            List<Assesment> assesments=assesmentRepository.findByCreatedByAndTrainee((Trainer) user.get(),(Trainee)userOptional.get(),
                    new Sort(Sort.Direction.DESC, "createdDate"));
            if(!(assesments.size() ==0))
            {
                Assesment lastAssessment=assesments.get(0);
                int HeightPercentage=percentageCalculator(assesment.getHeight(),lastAssessment.getHeight());
                int WeightPercentage=percentageCalculator(assesment.getWeight(),lastAssessment.getWeight());
                int ChestSizeDif=percentageCalculator(assesment.getChestSize(),lastAssessment.getChestSize());
                int ArmSizeDif=percentageCalculator(assesment.getArmSize(),lastAssessment.getArmSize());
                int MinutesKm=percentageCalculator(assesment.getMinutesKm(),lastAssessment.getMinutesKm());
                int HipsSizeDif=percentageCalculator(assesment.getHipsSize(),lastAssessment.getHipsSize());
                int WaistSize  =percentageCalculator(assesment.getWaistSize(),lastAssessment.getWaistSize());
                int HeartRateDif=percentageCalculator(assesment.getHeartRate(),lastAssessment.getHeartRate());
                AssesmentDifference assessmentDifference=new AssesmentDifference(WeightPercentage,HeightPercentage,ArmSizeDif,
                        ChestSizeDif,WaistSize,HipsSizeDif,HeartRateDif,MinutesKm);
                assesment.setAssesmentDifference(assessmentDifference);
                System.out.println(assesment.getWeight());
                System.out.println(lastAssessment.getWeight());

            }
            assesmentRepository.save(assesment);
        }
        return "redirect:home";
    }
    private static int percentageCalculator(int first,int second)
    {
        int dif=(first-second);
        float dif1=((dif*100)/second);
        return (int) dif1;
    }
}
