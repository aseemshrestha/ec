package ec.model;

import ec.util.DateFormatter;

import java.util.ArrayList;
import java.util.List;

public class EntityModelMapper {
    public static List<StudentDTO> studentModelToDto(List<Student> allStudents) {
        List<StudentDTO> studentDTOS = new ArrayList<>();
        allStudents.forEach(s -> {
            StudentDTO studentDTO = new StudentDTO();
            List<String> docLocationList = new ArrayList<>();
            studentDTO.setId(s.getId());
            studentDTO.setFirstName(s.getFirstName());
            studentDTO.setLastName(s.getLastName());
            studentDTO.setEmail(s.getEmail());
            studentDTO.setPhone(s.getPhone());
            studentDTO.setUniversityApplied(s.getUniName());
            studentDTO.setI20Status(s.getI20Status());
            studentDTO.setVisaInterviewDate(s.getInterviewDate() == null ? null : s.getInterviewDate().toString());
            studentDTO.setVisaStatus(s.getVisaStatus());
            studentDTO.setCreatedDate(DateFormatter.formatDateToString(s.getCreated(), "MMM dd yyyy", "EST"));
            studentDTO.setAdditionalComments(s.getAdditionalComments());
            String path = s.getEmail().substring(0, s.getEmail().indexOf('@'));
            //'https://ec-students.s3.amazonaws.com/sanjaykarki555-resume-aseem-shrestha.docx'
            if (s.getDocs() != null) {
                for (Docs d : s.getDocs()) {
                    String docsLocation = d.getDocsLocation();
                  //  String floc = docsLocation.substring(docsLocation.indexOf(path));
                    // System.out.println(floc.substring(floc.indexOf('-')+1));
                    docLocationList.add(docsLocation);
                }
                studentDTO.setDocs(docLocationList);
            }

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
            universityDto.setLastUpdated(DateFormatter.formatDateToString(university.getLastUpdated(), "MMM dd yyyy", "EST"));
            universityDto.setApprovalDate(university.getApprovalDate() == null ? null :
                    university.getApprovalDate().toString());
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