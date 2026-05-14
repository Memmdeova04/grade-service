package org.example.gradeservice.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gradeservice.client.AcademicServiceClient;
import org.example.gradeservice.client.AuthServiceClient;
import org.example.gradeservice.dto.*;
import org.example.gradeservice.entity.Grade;
import org.example.gradeservice.exception.GradeNotFoundException;
import org.example.gradeservice.mapper.GradeMapper;
import org.example.gradeservice.repository.GradeRepository;
import org.example.gradeservice.util.GradeStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GradingService {

    private final GradeRepository gradeRepository;
    private final GradeMapper gradeMapper;
    private final AuthServiceClient authClient;
    private final AcademicServiceClient academicClient;
    private final ExternalDataService externalDataService;




    @Transactional
    public GradeResponse updateActivity(ActivityScoreRequest req) {
        Grade grade = getOrCreate(req.getStudentId(), req.getSubjectId());
        grade.setActivity(req.getActivity());
        recalculateGpa(req.getStudentId());
        return buildResponse(gradeRepository.save(grade));
    }

    @Transactional
    public GradeResponse updateMidterm(MidtermScoreRequest req) {
        Grade grade = getOrCreate(req.getStudentId(), req.getSubjectId());
        grade.setMidterm(req.getMidterm());
        recalculateGpa(req.getStudentId());
        return buildResponse(gradeRepository.save(grade));
    }

    @Transactional
    public GradeResponse updatePresentation(PresentationScoreRequest req) {
        Grade grade = getOrCreate(req.getStudentId(), req.getSubjectId());
        grade.setPresentation(req.getPresentation());
        return buildResponse(gradeRepository.save(grade));
    }

    @Transactional
    public GradeResponse updateIndependentWork(IndependentWorkScoreRequest req) {
        Grade grade = getOrCreate(req.getStudentId(), req.getSubjectId());
        grade.setIndependentWork(req.getIndependentWork());
        return buildResponse(gradeRepository.save(grade));
    }


    private Grade getOrCreate(Long studentId, Long subjectId) {
        return gradeRepository.findByStudentIdAndSubjectId(studentId, subjectId)
                .orElseGet(() -> {
                    Grade g = new Grade();
                    g.setStudentId(studentId);
                    g.setSubjectId(subjectId);
                    return g;
                });
    }



    public GradeResponse getGrade(Long id) {
        Grade grade = gradeRepository.findById(id)
                .orElseThrow(() -> new GradeNotFoundException("Qiymət tapılmadı, id: " + id));
        return buildResponse(grade);
    }

    public List<GradeResponse> getGradesByStudent(Long studentId) {
        return gradeRepository.findAllByStudentId(studentId)
                .stream()
                .map(this::buildResponse)
                .collect(Collectors.toList());
    }
    @Transactional
    public List<GradeResponse> initializeGradesForGroup(Long subjectId, Long groupId) {
        List<StudentDto> students = authClient.getStudentsByGroup(groupId);
        return students.stream()
                .map(student -> {
                    Grade grade = getOrCreate(student.getId(), subjectId);
                    return buildResponse(gradeRepository.save(grade));
                })
                .collect(Collectors.toList());
    }

    public List<GradeResponse> getGradesBySubject(Long subjectId) {
        return gradeRepository.findAllBySubjectId(subjectId)
                .stream()
                .map(this::buildResponse)
                .collect(Collectors.toList());
    }


    public GradeResponse getGradeByStudentAndSubject(Long studentId, Long subjectId) {
        Grade grade = gradeRepository
                .findByStudentIdAndSubjectId(studentId, subjectId)
                .orElseThrow(() -> new GradeNotFoundException(
                        "Qiymət tapılmadı — studentId: " + studentId +
                                ", subjectId: " + subjectId));
        return buildResponse(grade);
    }
    @Cacheable(value = "external_students", key = "#studentId")
    public StudentDto getStudentFromAuth(Long studentId) {
        return authClient.getStudentById(studentId);
    }

    @Cacheable(value = "external_subjects", key = "#subjectId")
    public SubjectDto getSubjectFromAcademic(Long subjectId) {
        return academicClient.getSubjectById(subjectId);
    }



    private void recalculateGpa(Long studentId) {
        List<Grade> grades = gradeRepository.findAllByStudentId(studentId);

        double totalWeighted = 0.0;
        int totalCredits = 0;

        for (Grade g : grades) {
            if (g.getFinalScore() == null) continue; // yalnız tam qiyməti olan fənlər
            try {
                SubjectDto subject = externalDataService.getSubject(g.getSubjectId());
                int credits = subject.getCredits();
                totalWeighted += g.getFinalScore() * credits; // yekun bal × kredit
                totalCredits += credits;
            } catch (Exception e) {
                log.warn("GPA hesablanarkən subject alına bilmədi: {}", e.getMessage());
            }
        }

        double gpa = totalCredits > 0
                ? Math.round((totalWeighted / totalCredits) * 100.0) / 100.0
                : 0.0;

        authClient.updateStudentGpa(studentId, gpa);
        log.info("GPA yeniləndi - studentId: {}, gpa: {}", studentId, gpa);
    }



    private GradeResponse buildResponse(Grade grade) {
        GradeResponse r = new GradeResponse();
        r.setId(grade.getId());
        r.setStudentId(grade.getStudentId());
        r.setSubjectId(grade.getSubjectId());
        r.setActivity(grade.getActivity());
        r.setMidterm(grade.getMidterm());
        r.setPresentation(grade.getPresentation());
        r.setIndependentWork(grade.getIndependentWork());
        r.setSemesterScore(grade.getSemesterScore());
        r.setExamScore(grade.getExamScore());
        r.setFinalScore(grade.getFinalScore());
        r.setStatus(grade.getStatus());
        r.setLetterGrade(grade.getLetterGrade());


        try {
            StudentDto student = externalDataService.getStudent(grade.getStudentId());
            r.setStudentName(student.getFirstName() + " " + student.getLastName());
        } catch (Exception ignored) {}

        try {
            SubjectDto subject = externalDataService.getSubject(grade.getSubjectId());
            r.setSubjectName(subject.getName());
        } catch (Exception ignored) {}

        return r;
    }
}

