package utils;

import java.io.File;
import java.net.URL;

import org.apache.commons.mail.DataSourceResolver;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.ImageHtmlEmail;
import org.apache.commons.mail.resolver.DataSourceCompositeResolver;
import org.apache.commons.mail.resolver.DataSourceFileResolver;
import org.apache.commons.mail.resolver.DataSourceUrlResolver;

public class EmailUtils {

	public static void main(String[] args) {
		try {
			sendMsg();
			// String path= "E:\\result.xlsx";
			// sendEmailsWithAttachments("测试结果","请查收",path);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void sendMsg() throws Exception {
		// HtmlEmail mail = new HtmlEmail();
		ImageHtmlEmail mail = new ImageHtmlEmail();

//		String htmlEmailTemplate = "这是一张用于测试的图片，请查收。 <img src=\"test.png\"> "
//				+ " <img src=\"http://commons.apache.org/proper/commons-email/images/commons-logo.png\">";
		
		String htmlEmailTemplate = "这是一张用于测试的图片，请查收 "
				+ " <img src=\"http://commons.apache.org/proper/commons-email/images/commons-logo.png\">";

		// 解析本地图片和网络图片都有的html文件重点就是下面这两行；
		// ImageHtmlEmail通过setDataSourceResolver来识别并嵌入图片
		// 查看DataSourceResolver的继承结构发现有几个好用的子类
		DataSourceResolver[] dataSourceResolvers = new DataSourceResolver[] { new DataSourceFileResolver(), // 添加DataSourceFileResolver用于解析本地图片
				new DataSourceUrlResolver(new URL("http://")) };// 添加DataSourceUrlResolver用于解析网络图片，注意：new URL("http://")
		// DataSourceCompositeResolver类可以加入多个DataSourceResolver,
		// 把需要的DataSourceResolver放到一个数组里传进去就可以了；
		mail.setDataSourceResolver(new DataSourceCompositeResolver(dataSourceResolvers));

		String hostname = PropertiesUtils.getString("mail.host");
		String password = PropertiesUtils.getString("mail.password");
		String username = PropertiesUtils.getString("mail.username");
		String[] toList = PropertiesUtils.getStringArray("mail.touser");
		String subject = PropertiesUtils.getString("mail.title");
		mail.setHostName(hostname); // 邮件服务器域名
		mail.setAuthentication(username, password); // 邮箱账户
		mail.setCharset("UTF-8"); // 邮件的字符集
		mail.setFrom(username); // 发件人地址
		for (String to : toList) {
			mail.addTo(to); // 收件人地址，可以设置多个
		}

		mail.setSubject(subject); // 邮件主题
		mail.setHtmlMsg(htmlEmailTemplate); // 邮件正文
		String rString = mail.send(); // 发送邮件
		System.out.println(rString);
	}

	public static void sendEmailsWithAttachments(String title, String context, String... filepath)
			throws EmailException {

		String hostname = PropertiesUtils.getString("mail.host");
		String password = PropertiesUtils.getString("mail.password");
		String username = PropertiesUtils.getString("mail.username");
		String[] toList = PropertiesUtils.getStringArray("mail.touser");

		// Create the email message
		HtmlEmail email = new HtmlEmail();
		email.setHostName(hostname); // 邮件服务器域名
		email.setAuthentication(username, password); // 邮箱账户
		email.setCharset("UTF-8"); // 邮件的字符集

		// mail.setSSLOnConnect(true); // 是否启用SSL
		// mail.setSslSmtpPort(sslSmtpPort); // 若启用，设置smtp协议的SSL端口号
		email.setSubject(title);
		email.setFrom(username); // 发件人地址
		email.setHtmlMsg(context);

		for (String to : toList) {
			email.addTo(to); // 收件人地址，可以设置多个
		}
		// add the attachment
		for (String fp : filepath) {
			EmailAttachment attachment = new EmailAttachment();
			attachment.setPath(fp);
			attachment.setDisposition(EmailAttachment.ATTACHMENT);
			attachment.setDescription("测试结果");
			File f = new File(fp);
			attachment.setName(f.getName());
			email.attach(attachment);
		}
		// send the email
		email.send();
	}

}
