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

    public static List<UniversityDto> universityEntityToDto(List<University> universities) {
        List<UniversityDto> universitiesDtoList = new ArrayList<>();
        universities.forEach(university -> {
            UniversityDto universityDto = new UniversityDto();
            universityDto.setId(university.getId());
            universityDto.setEmail(university.getEmail());
            universityDto.setUniversityName(university.getUniversityName());
            universityDto.setAdditionalComments(university.getAdditionalComments());
            universityDto.setLastUpdated(DateFormatter.formatDateToString(university.getLastUpdated(),"MMM dd yyyy", "EST"));
            universityDto.setApprovalDate(university.getApprovalDate() != null ?
                    DateFormatter.formatDateToString(university.getApprovalDate(), "MMM dd yyyy", "EST") : "Not yet approved");
            universityDto.setContactPerson(university.getContactPerson());
            universityDto.setStatus(university.getService());
            universityDto.setPhone(university.getPhone());
            universityDto.setUniversityAddress(university.getUniversityAddress());
            universityDto.setUniversityAddress1(university.getUniversityAddress1());
            universitiesDtoList.add(universityDto);
        });
        return universitiesDtoList;
    }
}
