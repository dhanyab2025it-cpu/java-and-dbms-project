package com.codequest.platform.controller;

import com.codequest.platform.model.*;
import com.codequest.platform.repository.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;

@Controller
public class PlatformController {
    private final StudentRepository students;
    private final SkillRepository skills;
    private final StudentSkillRepository studentSkills;
    private final InternshipRepository internships;
    private final InternshipApplicationRepository applications;

    public PlatformController(StudentRepository students, SkillRepository skills,
            StudentSkillRepository studentSkills, InternshipRepository internships,
            InternshipApplicationRepository applications) {
        this.students=students; this.skills=skills; this.studentSkills=studentSkills;
        this.internships=internships; this.applications=applications;
    }

    private Student current(Authentication auth) {
        return students.findByEmail(auth.getName()).orElseThrow();
    }

    @GetMapping("/")
    public String dashboard(Authentication auth, Model model) {
        Student me=current(auth);
        model.addAttribute("student", me);
        model.addAttribute("mySkills", studentSkills.findByStudentId(me.getId()));
        model.addAttribute("internships", internships.findByOpenTrueOrderByLastDateAsc());
        model.addAttribute("myApplications", applications.findByStudentIdOrderByAppliedAtDesc(me.getId()));
        return "dashboard";
    }

    @PostMapping("/skills")
    public String addSkill(Authentication auth, @RequestParam String skillName,
                           @RequestParam String proficiencyLevel, RedirectAttributes ra) {
        Student me=current(auth);
        if (!java.util.Set.of("Beginner","Intermediate","Advanced").contains(proficiencyLevel)) {
            ra.addFlashAttribute("error","Choose a valid proficiency level."); return "redirect:/";
        }
        String clean=skillName.trim();
        if (clean.isBlank()) { ra.addFlashAttribute("error","Enter a skill name."); return "redirect:/"; }
        Skill skill=skills.findByNameIgnoreCase(clean).orElseGet(() -> skills.save(new Skill(clean)));
        boolean already=studentSkills.findByStudentId(me.getId()).stream()
            .anyMatch(x -> x.getSkill().getId().equals(skill.getId()));
        if (already) { ra.addFlashAttribute("error","You have already added this skill."); return "redirect:/"; }
        StudentSkill ss=new StudentSkill(); ss.setStudent(me); ss.setSkill(skill); ss.setProficiencyLevel(proficiencyLevel);
        studentSkills.save(ss); ra.addFlashAttribute("success","Skill added."); return "redirect:/";
    }

    @PostMapping("/skills/{id}/delete")
    public String deleteSkill(Authentication auth, @PathVariable Long id, RedirectAttributes ra) {
        Student me=current(auth);
        studentSkills.findByIdAndStudentId(id,me.getId()).ifPresent(studentSkills::delete);
        ra.addFlashAttribute("success","Skill removed."); return "redirect:/";
    }

    @GetMapping("/students")
    public String searchStudents(@RequestParam(required=false) String skill, Model model) {
        model.addAttribute("skill", skill == null ? "" : skill);
        model.addAttribute("matches", (skill == null || skill.isBlank())
            ? java.util.List.of() : studentSkills.findBySkillNameIgnoreCase(skill.trim()));
        return "students";
    }

    @PostMapping("/internships")
    public String postInternship(Authentication auth, @RequestParam String company,
            @RequestParam String location, @RequestParam String title,
            @RequestParam String description, @RequestParam String requiredSkill,
            @RequestParam Integer durationMonths, @RequestParam(required=false) Double stipend,
            @RequestParam LocalDate lastDate, RedirectAttributes ra) {
        if (lastDate.isBefore(LocalDate.now()) || durationMonths < 1) {
            ra.addFlashAttribute("error","Enter a future deadline and a valid duration."); return "redirect:/";
        }
        Internship i=new Internship(); i.setPostedBy(current(auth)); i.setCompany(company.trim());
        i.setLocation(location); i.setTitle(title.trim()); i.setDescription(description);
        i.setRequiredSkill(requiredSkill.trim()); i.setDurationMonths(durationMonths);
        i.setStipend(stipend); i.setLastDate(lastDate); i.setOpen(true);
        internships.save(i); ra.addFlashAttribute("success","Internship opportunity posted."); return "redirect:/";
    }

    @PostMapping("/internships/{id}/apply")
    public String apply(Authentication auth, @PathVariable Long id, RedirectAttributes ra) {
        Student me=current(auth);
        Internship i=internships.findById(id).orElseThrow();
        if (!i.isOpen() || (i.getLastDate()!=null && i.getLastDate().isBefore(LocalDate.now()))) {
            ra.addFlashAttribute("error","This opportunity is closed.");
        } else if (applications.existsByStudentIdAndInternshipId(me.getId(),id)) {
            ra.addFlashAttribute("error","You have already applied for this internship.");
        } else {
            InternshipApplication a=new InternshipApplication(); a.setStudent(me); a.setInternship(i);
            applications.save(a); ra.addFlashAttribute("success","Application submitted.");
        }
        return "redirect:/";
    }

    @PostMapping("/applications/{id}/withdraw")
    public String withdraw(Authentication auth, @PathVariable Long id, RedirectAttributes ra) {
        Student me=current(auth);
        applications.findByIdAndStudentId(id,me.getId()).ifPresent(a -> {
            if ("Applied".equals(a.getStatus())) a.setStatus("Withdrawn");
            applications.save(a);
        });
        ra.addFlashAttribute("success","Application status updated."); return "redirect:/";
    }
}
