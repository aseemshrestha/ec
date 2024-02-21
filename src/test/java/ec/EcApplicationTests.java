package ec;

import ec.cloud.AwsConfig;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;


class EcApplicationTests {

    @Test
    void contextLoads() {
    }


    @Test
    public void test() throws ParseException {
        String dateString = "2024-02-06 17:35:20.003";
        String dates = dateString.substring(0, 10);
        //System.out.println(dates);
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        //	SimpleDateFormat outputFormat = new SimpleDateFormat("MM-dd-yyyy");
        Date date = inputFormat.parse(dates);
        //String output = outputFormat.format(date);
        //System.out.println(date);
        Clock clock = Clock.system(ZoneId.of("Asia/Kathmandu"));
        LocalDate localDate = LocalDate.now(clock);
        LocalTime localTime = LocalTime.now(clock);
        System.out.println(localTime);
        //eSystem.out.println(localDate + " " + localTime.getHour() + ":" + localTime.getMinute());

        LocalDateTime localDateTime = LocalDateTime.now(clock);
        System.out.println(localDateTime);


    }

    @Test
    public void test2() throws ParseException {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        String newDate = "2024-02-02T00:00:00.000Z";
        String existingDate = "2024-02-06 17:35:20.003";

        //System.out.println(new Date()  + " " + LocalDateTime.now());


		/*Date date = fmt.parse(myStringDate);
		System.out.println(date);
		System.out.println(fmt.format(date));*/

        String string = "2024-02-02T00:00:00.000Z";
        String d = "Fri Feb 23 2024 19:00:00 GMT-0500 (Eastern Standard Time)";
        String dateStr = "Fri Feb 23 2024 19:00:00 GMT-0500 (Eastern Standard Time)";
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss 'GMT'Z (zzzz)");

       // Date date = sdf.parse(dateStr);
        //System.out.println(date);

      //  String s = "https://ec-students.s3.amazonaws.com/sanjaykarki555-1708014415416-Sangeet-Shrestha-Resume.docx";
       // String[] split = s.split("/");
        //String s1 = split[3];
        //System.out.println(s1.split("-")[1]);


       //https://www.youtube.com/watch?v=ZNw8qZwqv_4
        //String defaultTimezone = TimeZone.getDefault().getID();
        //Date date = (new SimpleDateFormat("yyyy-MM-dd"))
        //	.parse(string.replaceAll("Z$", "+0000"));

		/*System.out.println("string: " + string);
		System.out.println("defaultTimezone: " + defaultTimezone);
		System.out.println("date: " + (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(date));
*/
        //Date date1 = DateFormatter.formatStringToDateOnly(d, "yyyy-MM-dd");
        //System.out.println("Date 1:" + date1);
        //Date date2 = DateFormatter.formatStringToDateOnly(existingDate, "yyyy-MM-dd");
        //System.out.println("Date 2:" + date2);

        String s = Base64.getEncoder().encodeToString("Kathmandu123@@@".getBytes());
        System.out.println(s);
        byte[] decode = Base64.getDecoder().decode(s);
        String ds = new String(decode);
        System.out.println(ds);
    }

}
