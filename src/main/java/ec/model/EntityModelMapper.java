package ec.model;

import ec.util.DateFormatter;

import java.util.ArrayList;
import java.util.List;

public class EntityModelMapper {
    public static List<StudentDTO> studentModelToDto(List<Student> allStudents) {
        List<StudentDTO> studentDTOS = new ArrayList<>();
        allStudents.forEach(s -> {
            StudentDTO studentDTO = new StudentDTO();
            studentDTO.setId(s.getId());
            studentDTO.setFirstName(s.getFirstName());
            studentDTO.setLastName(s.getLastName());
            studentDTO.setEmail(s.getEmail());
            studentDTO.setPhone(s.getPhone());
            studentDTO.setUniversityApplied(s.getUniName());
            studentDTO.setI20Status(s.getService());
            studentDTO.setVisaInterviewDate(s.getInterviewDate() == null ? null : s.getInterviewDate().toString());
            studentDTO.setVisaStatus(s.getVisaStatus());
            studentDTO.setCreatedDate(DateFormatter.formatDateToString(s.getCreated(), "MMM dd yyyy", "EST"));
            studentDTO.setAdditionalComments(s.getMessage());
            studentDTOS.add(studentDTO);
        });
        return studentDTOS;
    }
}
