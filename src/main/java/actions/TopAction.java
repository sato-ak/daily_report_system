package actions;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import actions.views.EmployeeView;
import actions.views.WeekInfo;
import constants.AttributeConst;
import constants.ForwardConst;
import models.Report;
import services.ReportService;

/**
 * トップページに関する処理を行うActionクラス
 *
 */
public class TopAction extends ActionBase {

    private ReportService service;

    /**
     * processメソッドを実行する
     */
    @Override
    public void process() throws ServletException, IOException {

        service = new ReportService();

        //メソッドを実行
        invoke();

        service.close();
    }

    /**
     * カレンダー画面を表示する
     */

    public void index() throws ServletException, IOException {

        //セッションからログイン中の従業員情報を取得
        EmployeeView loginEmployee = (EmployeeView) getSessionScope(AttributeConst.LOGIN_EMP);

        //セッションにフラッシュメッセージが設定されている場合はリクエストスコープに移し替え、セッションからは削除する
        String flush = getSessionScope(AttributeConst.FLUSH);
        if (flush != null) {
            putRequestScope(AttributeConst.FLUSH, flush);
            removeSessionScope(AttributeConst.FLUSH);
        }

        LocalDate today = LocalDate.now();
        LocalDate targetDay = null;
        int year = 0;
        int month = 0;
        String yearStr = request.getParameter("year");
        String monthStr = request.getParameter("month");

        if ((yearStr != null && !yearStr.equals("")) &&
                (monthStr != null && !monthStr.equals(""))) {
            // 年月のパラメータが含まれている場合は
            year = toNumber(yearStr);
            month = toNumber(monthStr);
            targetDay = LocalDate.of(year, month, 1);
        } else {
            //年月のパラメータが空の場合
            //本日の年月を設定
            year = today.getYear();
            month = today.getMonthValue();
            targetDay = today;
        }

        LocalDate previousMonth = targetDay.minusMonths(1); //先月
        int previousYearNum = previousMonth.getYear();
        int previousMonthNum = previousMonth.getMonthValue();

        LocalDate nextMonth = targetDay.plusMonths(1); //翌月
        int nextYearNum = nextMonth.getYear();
        int nextMonthNum = nextMonth.getMonthValue();

        List<Report> reportList = getReport(loginEmployee, year, month);

        Map<Integer, List<Report>> reportMap = new HashMap<Integer, List<Report>>();

        for (int i = 0; i < reportList.size(); ++i) {

            Report report = reportList.get(i);

            // LocalDate型からLocallDateTime型へ
            LocalTime time = LocalTime.of(0, 0);
            LocalDateTime reportDateTime = LocalDateTime.of(report.getReportDate(), time);

            // 日報の日付を取得
            int reportDay = reportDateTime.getDayOfMonth();

            List<Report> log;

            // reportMapにキーが登録されているか判定
            if (reportMap.containsKey(reportDay)) {
                // 既にキーが登録されている場合
                log = reportMap.get(reportDay);
            } else {
                //キーが登録されていいない場合
                log = new ArrayList<Report>();
            }

            log.add(report);
            reportMap.put(reportDay, log);

        }

        // 週ごとのカレンダー情報を取得
        List<WeekInfo> weekList = getCalendarByWeeks(year, month, reportMap);

        putRequestScope(AttributeConst.TOP_YEAR, year); // 年
        putRequestScope(AttributeConst.TOP_MONTH, month); // 月
        putRequestScope(AttributeConst.TOP_WEEKLIST, weekList); // 週の情報
        putRequestScope(AttributeConst.TOP_PREVIOUS_YEAR, previousYearNum);
        putRequestScope(AttributeConst.TOP_PREVIOUS_MONTH, previousMonthNum);
        putRequestScope(AttributeConst.TOP_NEXT_YEAR, nextYearNum);
        putRequestScope(AttributeConst.TOP_NEXT_MONTH, nextMonthNum);

        //一覧画面を表示
        forward(ForwardConst.FW_TOP_INDEX);

    }

    private List<Report> getReport(EmployeeView loginEmployee, int year, int month) {
        // 月初めの日（日付）
        LocalDate firstDate = LocalDate.of(year, month, 1);

        // 月末日（日時）
        // 月初めの日の月から見て最後の日を第三引数に設定することで月末日（日時）が取得できる
        LocalDate lastDate = LocalDate.of(year, month, firstDate.lengthOfMonth());

        List<Report> reportList = new ArrayList<Report>();
        reportList = service.getAll(loginEmployee, firstDate, lastDate);
        return reportList;
    }

    /**
     * 週ごとのカレンダー情報を取得
     * @param year 取得対象の年
     * @param month 取得対象の月
     * @param reportMap 日報情報
     * @return 週ごとのカレンダー情報
     */

    private List<WeekInfo> getCalendarByWeeks(int year, int month,
            Map<Integer, List<Report>> reportMap) {

        List<WeekInfo> weekList = new ArrayList<WeekInfo>();

        LocalDate firstDay = LocalDate.of(year, month, 1);

        //曜日の取得
        DayOfWeek firstDayOfWeek = firstDay.getDayOfWeek();

        LocalDate endDay = LocalDate.of(year, month, firstDay.lengthOfMonth());
        int maxdate = endDay.getDayOfMonth();

        // 当月の週の月曜日を取得
        LocalDate startWeekDay = firstDay.with(DayOfWeek.MONDAY);

        // 前月の日付部分
        int dayOfPreMonth = startWeekDay.getDayOfMonth();
        // 当月の日付部分
        int dayOfCurrentMonth = 1;
        // 翌月の日付部分
        int dayOfNextMonth = 1;

        for (int rowweek = 0; rowweek < 6; rowweek++) {
            WeekInfo weekInfo = new WeekInfo();

            // 月曜日
            if (firstDayOfWeek.compareTo(DayOfWeek.MONDAY) > 0 && rowweek == 0) {
                weekInfo.setMonDate(dayOfPreMonth);
                weekInfo.setMonDateOfcurrentMonth(false);
                dayOfPreMonth++;
            } else if (dayOfCurrentMonth <= maxdate) {
                weekInfo.setMonDate(dayOfCurrentMonth);
                weekInfo.setMonDateOfcurrentMonth(true);

                // 日付に紐つくデータがあるか判定
                if (reportMap.containsKey(dayOfCurrentMonth)) {
                    // データがある場合
                    weekInfo.setMonDateReport(reportMap.get(dayOfCurrentMonth));
                }
                dayOfCurrentMonth++;
            } else if (maxdate < dayOfCurrentMonth) {
                // 当月の最大の日付より大きい場合
                weekInfo.setMonDate(dayOfNextMonth);
                weekInfo.setMonDateOfcurrentMonth(false);
                dayOfNextMonth++;
            }

            // 火曜日
            if (firstDayOfWeek.compareTo(DayOfWeek.TUESDAY) > 0 && rowweek == 0) {
                weekInfo.setTueDate(dayOfPreMonth);
                dayOfPreMonth++;
            } else if (dayOfCurrentMonth <= maxdate) {
                weekInfo.setTueDate(dayOfCurrentMonth);
                weekInfo.setTueDateOfcurrentMonth(true);
                // 日付に紐つくデータがあるか判定
                if (reportMap.containsKey(dayOfCurrentMonth)) {
                    // データがある場合
                    weekInfo.setTueDateReport(reportMap.get(dayOfCurrentMonth));
                }
                dayOfCurrentMonth++;
            } else if (maxdate < dayOfCurrentMonth) {
                // 当月の最大の日付より大きい場合
                weekInfo.setTueDate(dayOfNextMonth);
                dayOfNextMonth++;
            }

            // 水曜日
            if (firstDayOfWeek.compareTo(DayOfWeek.WEDNESDAY) > 0 && rowweek == 0) {
                weekInfo.setWedDate(dayOfPreMonth);
                dayOfPreMonth++;
            } else if (dayOfCurrentMonth <= maxdate) {
                weekInfo.setWedDate(dayOfCurrentMonth);
                weekInfo.setWedDateOfcurrentMonth(true);

                // 日付に紐つくデータがあるか判定
                if (reportMap.containsKey(dayOfCurrentMonth)) {
                    // データがある場合
                    weekInfo.setWedDateReport(reportMap.get(dayOfCurrentMonth));
                }
                dayOfCurrentMonth++;
            } else if (maxdate < dayOfCurrentMonth) {
                // 当月の最大の日付より大きい場合
                weekInfo.setWedDate(dayOfNextMonth);
                dayOfNextMonth++;
            }

            // 木曜日
            if (firstDayOfWeek.compareTo(DayOfWeek.THURSDAY) > 0 && rowweek == 0) {
                weekInfo.setThuDate(dayOfPreMonth);
                dayOfPreMonth++;
            } else if (dayOfCurrentMonth <= maxdate) {
                weekInfo.setThuDate(dayOfCurrentMonth);
                weekInfo.setThuDateOfcurrentMonth(true);

                // 日付に紐つくデータがあるか判定
                if (reportMap.containsKey(dayOfCurrentMonth)) {
                    // データがある場合
                    weekInfo.setThuDateReport(reportMap.get(dayOfCurrentMonth));
                }
                dayOfCurrentMonth++;
            } else if (maxdate < dayOfCurrentMonth) {
                // 当月の最大の日付より大きい場合
                weekInfo.setThuDate(dayOfNextMonth);
                dayOfNextMonth++;
            }

            // 金曜日
            if (firstDayOfWeek.compareTo(DayOfWeek.FRIDAY) > 0 && rowweek == 0) {
                weekInfo.setFriDate(dayOfPreMonth);
                dayOfPreMonth++;
            } else if (dayOfCurrentMonth <= maxdate) {
                weekInfo.setFriDate(dayOfCurrentMonth);
                weekInfo.setFriDateOfcurrentMonth(true);

                // 日付に紐つくデータがあるか判定
                if (reportMap.containsKey(dayOfCurrentMonth)) {
                    // データがある場合
                    weekInfo.setFriDateReport(reportMap.get(dayOfCurrentMonth));
                }
                dayOfCurrentMonth++;
            } else if (maxdate < dayOfCurrentMonth) {
                // 当月の最大の日付より大きい場合
                weekInfo.setFriDate(dayOfNextMonth);
                dayOfNextMonth++;
            }

            // 土曜日
            if (firstDayOfWeek.compareTo(DayOfWeek.SATURDAY) > 0 && rowweek == 0) {
                weekInfo.setSatDate(dayOfPreMonth);
                dayOfPreMonth++;
            } else if (dayOfCurrentMonth <= maxdate) {
                weekInfo.setSatDate(dayOfCurrentMonth);
                weekInfo.setSatDateOfcurrentMonth(true);

                // 日付に紐つくデータがあるか判定
                if (reportMap.containsKey(dayOfCurrentMonth)) {
                    // データがある場合
                    weekInfo.setSatDateReport(reportMap.get(dayOfCurrentMonth));
                }
                dayOfCurrentMonth++;
            } else if (maxdate < dayOfCurrentMonth) {
                // 当月の最大の日付より大きい場合
                weekInfo.setSatDate(dayOfNextMonth);
                dayOfNextMonth++;
            }

            // 日曜日
            if (firstDayOfWeek.compareTo(DayOfWeek.SUNDAY) > 0 && rowweek == 0) {
                weekInfo.setSunDate(dayOfPreMonth);
                dayOfPreMonth++;
            } else if (dayOfCurrentMonth <= maxdate) {
                weekInfo.setSunDate(dayOfCurrentMonth);
                weekInfo.setSunDateOfcurrentMonth(true);

                // 日付に紐つくデータがあるか判定
                if (reportMap.containsKey(dayOfCurrentMonth)) {
                    // データがある場合
                    weekInfo.setSunDateReport(reportMap.get(dayOfCurrentMonth));
                }
                dayOfCurrentMonth++;
            } else if (maxdate < dayOfCurrentMonth) {
                // 当月の最大の日付より大きい場合
                weekInfo.setSunDate(dayOfNextMonth);
                dayOfNextMonth++;
            }

            weekList.add(weekInfo);
        }
        return weekList;
    }
}