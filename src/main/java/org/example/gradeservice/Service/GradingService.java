package org.example.gradeservice.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gradeservice.client.AcademicServiceClient;
import org.example.gradeservice.client.AuthServiceClient;
import org.example.gradeservice.dto.*;
import org.example.gradeservice.entity.Grade;
import org.example.gradeservice.exception.GradeNotFoundException;
import org.example.gradeservice.exception.InvalidOperationException;
import org.example.gradeservice.mapper.GradeMapper;
import org.example.gradeservice.repository.GradeRepository;
import org.example.gradeservice.util.GradeStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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



    @Transactional
    public GradeResponse updateActivity(ActivityScoreRequest req) {
        Grade grade = getOrCreate(req.getStudentId(), req.getSubjectId());
        grade.setActivity(req.getActivity());
        return buildResponse(gradeRepository.save(grade));
    }

    @Transactional
    public GradeResponse updateMidterm(MidtermScoreRequest req) {
        Grade grade = getOrCreate(req.getStudentId(), req.getSubjectId());
        grade.setMidterm(req.getMidterm());
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



    private void recalculateGpa(Long studentId) {
        List<Grade> grades = gradeRepository.findAllByStudentId(studentId);

        double totalWeighted = 0.0;
        int totalCredits = 0;

        for (Grade g : grades) {
            if (g.getExamScore() == null) continue;

            try {
                SubjectDto subject = academicClient.getSubjectById(g.getSubjectId());
                int credits = subject.getCredits();
                double point = (g.getStatus() == GradeStatus.PASSED)
                        ? toGpaPoint(g.getFinalScore()) : 0.0;
                totalWeighted += point * credits;
                totalCredits += credits;
            } catch (Exception e) {
                log.warn("GPA hesablanarkən subject alına bilmədi: {}", e.getMessage());
            }
        }

        double gpa = totalCredits > 0
                ? Math.round((totalWeighted / totalCredits) * 100.0) / 100.0
                : 0.0;

        authClient.updateStudentGpa(studentId, gpa);
    }

    private double toGpaPoint(Double score) {
        if (score == null) return 0.0;
        if (score >= 91) return 4.0;
        if (score >= 81) return 3.0;
        if (score >= 71) return 2.0;
        if (score >= 51) return 1.0;
        return 0.0;
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
            StudentDto student = authClient.getStudentById(grade.getStudentId());
            r.setStudentName(student.getFirstName() + " " + student.getLastName());
        } catch (Exception ignored) {}

        try {
            SubjectDto subject = academicClient.getSubjectById(grade.getSubjectId());
            r.setSubjectName(subject.getName());
        } catch (Exception ignored) {}

        return r;
    }
}