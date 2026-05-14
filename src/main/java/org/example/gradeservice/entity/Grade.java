package org.example.gradeservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.gradeservice.util.GradeStatus;

@Entity
@Table(name = "grades",
        uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "subject_id"}))
@Getter
@Setter
public class Grade extends BaseEntity {

    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "subject_id", nullable = false)
    private Long subjectId;


    @Column(name = "activity")
    private Double activity;

    @Column(name = "midterm")
    private Double midterm;

    @Column(name = "presentation")
    private Double presentation;

    @Column(name = "independent_work")
    private Double independentWork;


    @Column(name = "exam_score")
    private Double examScore;

    public double getSemesterScore() {
        return n(activity) + n(midterm) + n(presentation) + n(independentWork);
    }

    public Double getFinalScore() {
        if (examScore == null) return null;
        return getSemesterScore() + examScore;
    }

    public GradeStatus getStatus() {

        if (activity == null || midterm == null ||
                presentation == null || independentWork == null) {
            return GradeStatus.INCOMPLETE;
        }


        if (examScore == null) {
            return GradeStatus.INCOMPLETE;
        }

        double sem = getSemesterScore();
        if (sem < 17)             return GradeStatus.NOT_ALLOWED_TO_EXAM;
        if (examScore < 17)       return GradeStatus.FAILED_EXAM;
        if (getFinalScore() < 51) return GradeStatus.FAILED_TOTAL;
        return GradeStatus.PASSED;
    }

    public String getLetterGrade() {
        GradeStatus s = getStatus();
        if (s == GradeStatus.INCOMPLETE)          return null;
        if (s == GradeStatus.NOT_ALLOWED_TO_EXAM) return null;
        if (s == GradeStatus.PENDING_EXAM)        return null;
        if (s == GradeStatus.FAILED_EXAM)         return null;
        if (s == GradeStatus.FAILED_TOTAL)        return "F";

        Double f = getFinalScore();
        if (f == null) return null;
        if (f >= 91) return "A";
        if (f >= 81) return "B";
        if (f >= 71) return "C";
        if (f >= 61) return "D";
        return "E";
    }

    private double n(Double v) { return v != null ? v : 0.0; }
}