package actions.views;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import models.Report;

@Getter //全てのクラスフィールドについてgetterを自動生成する(Lombok)
@Setter //全てのクラスフィールドについてsetterを自動生成する(Lombok)
public class WeekInfo {
    private Integer sunDate;
    private boolean sunDateOfcurrentMonth;

    private List<Report> sunDateReport;

    private Integer monDate;
    private boolean monDateOfcurrentMonth;

    private List<Report> monDateReport;

    private Integer tueDate;
    private boolean tueDateOfcurrentMonth;

    private List<Report> tueDateReport;

    private Integer wedDate;
    private boolean wedDateOfcurrentMonth;

    private List<Report> wedDateReport;

    private Integer thuDate;
    private boolean thuDateOfcurrentMonth;

    private List<Report> thuDateReport;

    private Integer friDate;
    private boolean friDateOfcurrentMonth;

    private List<Report> friDateReport;

    private Integer satDate;
    private boolean satDateOfcurrentMonth;

    private List<Report> satDateReport;

}
