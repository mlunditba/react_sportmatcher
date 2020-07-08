package ar.edu.itba.paw.service;

import java.util.Locale;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import ar.edu.itba.paw.interfaces.EmailService;
import ar.edu.itba.paw.model.Event;
import ar.edu.itba.paw.model.Tournament;
import ar.edu.itba.paw.model.User;

@Async
@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	private JavaMailSender mailSender;

	@Autowired
	private TemplateEngine htmlTemplateEngine;
	
	@Qualifier("emailMessageSource")
	@Autowired
	private MessageSource ems;

	private static final String USER_REGISTERED_TEMPLATE = "userRegistered";
	private static final String YOU_WERE_KICKED_TEMPLATE = "youWereKicked";
	private static final String TOURNAMENT_KICKED_TEMPLATE = "tournamentKicked";
	private static final String TOURNAMENT_STARTED_TEMPLATE = "tournamentStarted";
	private static final String EVENT_STARTED_TEMPLATE = "eventStarted";
	private static final String TOURNAMENT_CANCELLED_TEMPLATE = "tournamentCancelled";
	private static final String EVENT_CANCELLED_TEMPLATE = "eventCancelled";


	private void sendMail(final User user, final Locale locale, String titleMessage, Context ctx, String  template, Object[] titleParams) {

		final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
		final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
		try {
			message.setSubject(ems.getMessage(titleMessage, titleParams, locale));
			message.setFrom("SportMatcher");
			message.setTo(user.getUsername());

			final String htmlContent = this.htmlTemplateEngine.process(template, ctx);
			message.setText(htmlContent, true);
		} catch (Exception e) {
			//LOGGER.error("Message sending exception", e);
		}


		this.mailSender.send(mimeMessage);
	}

	@Override
	public void userRegistered(final User user, final Locale locale) {

		final Context ctx = new Context(locale);
		ctx.setVariable("email_body", ems.getMessage("user_registered_body", new Object[]{
				user.getFirstname(),
				user.getLastname()
		}, locale));

		sendMail(user,locale,"user_registered_title", ctx, USER_REGISTERED_TEMPLATE,null);
	}

	@Override
	public void youWereKicked(User kickedUser, Event event, final Locale locale) {
		final Context ctx = new Context(locale);
		ctx.setVariable("event_name", event.getName());
		ctx.setVariable("email_body", ems.getMessage("user_kicked_body", new Object[]{
				kickedUser.getFirstname(),
				kickedUser.getLastname(),
				event.getName()
			}, locale));

		sendMail(kickedUser, locale, "user_kicked_title",ctx, YOU_WERE_KICKED_TEMPLATE, new Object[]{event.getName()});
	}

	@Override
	public void youWereKicked(User kickedUser, Tournament tournament, Locale locale) {
		final Context ctx = new Context(locale);
		ctx.setVariable("tournament_name", tournament.getName());
		ctx.setVariable("email_body", ems.getMessage("tour_kicked_body", new Object[]{
				kickedUser.getFirstname(),
				kickedUser.getLastname(),
				tournament.getName()
			}, locale));

		sendMail(kickedUser, locale, "tour_kicked_title", ctx, TOURNAMENT_KICKED_TEMPLATE, new Object[]{tournament.getName()});
	}

	@Override
	public void tournamentStarted(final User user, final Tournament tournament, final Locale locale) {
		final Context ctx = new Context(locale);
		ctx.setVariable("tournament_name", tournament.getName());
		ctx.setVariable("email_body", ems.getMessage("tournament_started_body", new Object[]{
				user.getFirstname(),
				user.getLastname(),
				tournament.getName()
			}, locale));

		sendMail(user, locale, "tournament_started_title", ctx, TOURNAMENT_STARTED_TEMPLATE, new Object[]{tournament.getName()});
	}

	@Override
	public void eventStarted(final User user, final Event event, final Locale locale) {
		final Context ctx = new Context(locale);
		ctx.setVariable("event_name", event.getName());
		ctx.setVariable("email_body", ems.getMessage("event_started_body", new Object[]{
				user.getFirstname(),
				user.getLastname(),
				event.getName()
			}, locale));

		sendMail(user, locale, "event_started_title", ctx, EVENT_STARTED_TEMPLATE, new Object[]{event.getName()});
	}

	@Override
	public void tournamentCancelled(final User user, final Tournament tournament, final Locale locale) {
		tournamentCancelled(user, tournament.getName(), locale);
	}
	
	@Override
	public void tournamentCancelled(User user, String tournamentName, Locale locale) {
		final Context ctx = new Context(locale);
		ctx.setVariable("tournament_name", tournamentName);
		ctx.setVariable("email_body", ems.getMessage("tournament_cancelled_body", new Object[]{
				user.getFirstname(),
				user.getLastname(),
				tournamentName
			}, locale));

		sendMail(user, locale, "tournament_cancelled_title", ctx, TOURNAMENT_CANCELLED_TEMPLATE, new Object[]{tournamentName});
	}

	@Override
	public void eventCancelled(User user, Event event, final Locale locale) {
		eventCancelled(user, event.getName(), locale);
	}

	@Override
	public void eventCancelled(User user, String eventName, Locale locale) {
		final Context ctx = new Context(locale);
		ctx.setVariable("event_name", eventName);
		ctx.setVariable("email_body", ems.getMessage("event_cancelled_body", new Object[]{
				user.getFirstname(),
				user.getLastname(),
				eventName
			}, locale));
		sendMail(user, locale, "event_cancelled_title",ctx, EVENT_CANCELLED_TEMPLATE, new Object[]{eventName});
	}

}
