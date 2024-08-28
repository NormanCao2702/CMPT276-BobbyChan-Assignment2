package asn2.demo.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import asn2.models.Student;
import asn2.models.StudentRepo;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;

@Controller
public class StudentController {

    @Autowired
    private StudentRepo userRepo;

    @GetMapping("/students/view") // retrieve or fetch data from the server without making any modifications
    public String getAllUsers(Model model){
        System.out.println("Getting all users");
        // get all users from database
        List<Student> students = userRepo.findAll();
        // end of database call
        model.addAttribute("stu", students);
        return "students/showAll";
    }

    @PostMapping("/students/added") //It is typically used when you want to submit or send data to the server, usually with the intention of creating, updating, or modifying some resource on the server
    public String addUser(@RequestParam Map<String, String> newuser, HttpServletResponse response){
        System.out.println("ADD student");
        String newName = newuser.get("name");
        String newHc = newuser.get("hc");
        float newHeight = Float.parseFloat(newuser.get("h"));
        float newWeight = Float.parseFloat(newuser.get("w"));
        float newGPA = Float.parseFloat(newuser.get("gpa"));
        Student newStudent = new Student(newName, newWeight, newHeight, newGPA, newHc);
        userRepo.save(newStudent);
        response.setStatus(201);
        return "students/addedStudent";
    }

    @PostMapping("/students/deleted")
    public String deleteStudent(@RequestParam("deleteID") String id,HttpServletResponse response) {
        System.out.println("DELETE student");
        int sid = Integer.parseInt(id);
        Student deleted = userRepo.findById(sid).get();
        if(deleted!=null){
            userRepo.delete(deleted);
            return "students/addedStudent";
        }
        response.setStatus(404);
        return "students/studentNotFound";
    }


    @PostMapping("/students/update")
    public String updateStudent(@RequestParam("id") int id, @RequestParam Map<String, String> updatedUser, HttpServletResponse response) {
        System.out.println("UPDATE student");
        Optional<Student> optionalStudent = userRepo.findById(id);
        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();

            // Update the relevant properties of the student object
            String updatedHc = updatedUser.get("hc");
            if (updatedHc != null) {
                student.setHair_color(updatedHc);
            }
            
            String updatedHeightStr = updatedUser.get("h");
            if (updatedHeightStr != null) {
                float updatedHeight = Float.parseFloat(updatedHeightStr);
                student.setHeight(updatedHeight);
            }
            
            String updatedWeightStr = updatedUser.get("w");
            if (updatedWeightStr != null) {
                float updatedWeight = Float.parseFloat(updatedWeightStr);
                student.setWeight(updatedWeight);
            }
        
            String updatedGpaStr = updatedUser.get("gpa");
            if (updatedGpaStr != null) {
                float updatedGPA = Float.parseFloat(updatedGpaStr);
                student.setGpa(updatedGPA);
            }
            userRepo.save(student);
            response.setStatus(200);
            return "students/addedStudent";
        } else {
            response.setStatus(404);
            return "students/studentNotFound";
        }
    }

    @GetMapping("/")
    public RedirectView process(){
        return new RedirectView("add.html");
    }
}
