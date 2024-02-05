package ec;

import ec.util.DateFormatter;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;
import java.util.TimeZone;


class EcApplicationTests {

	@Test
	void contextLoads() {
	}


	@Test
	public void  test() throws ParseException {
		String dateString = "2024-02-06 17:35:20.003";
		String dates = dateString.substring(0,10);
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

		System.out.println(new Date()  + " " + LocalDateTime.now());


		/*Date date = fmt.parse(myStringDate);
		System.out.println(date);
		System.out.println(fmt.format(date));*/

		String string = "2024-02-02T00:00:00.000Z";
		String defaultTimezone = TimeZone.getDefault().getID();
		Date date = (new SimpleDateFormat("yyyy-MM-dd"))
				.parse(string.replaceAll("Z$", "+0000"));

		System.out.println("string: " + string);
		System.out.println("defaultTimezone: " + defaultTimezone);
		System.out.println("date: " + (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(date));

		Date date1 = DateFormatter.formatStringToDateOnly(newDate, "yyyy-MM-dd");
		System.out.println("Date 1:" + date1);
		Date date2 = DateFormatter.formatStringToDateOnly(existingDate, "yyyy-MM-dd");
		System.out.println("Date 2:" + date2);

		String s = Base64.getEncoder().encodeToString("root".getBytes());
		System.out.println(s);
		byte[] decode = Base64.getDecoder().decode(s);
		String d = new String(decode);
		System.out.println(d);
	}

}
