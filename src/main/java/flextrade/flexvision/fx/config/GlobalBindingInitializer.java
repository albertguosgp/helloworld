package flextrade.flexvision.fx.config;

import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import java.util.Date;

/**
 * Binder help all controller to serialize from servlet request to request object
 * */
@ControllerAdvice
public class GlobalBindingInitializer {

	@InitBinder
	public void initDateBinder(WebDataBinder binder) {
		CustomDateEditor iso8601DateEditor = new CustomDateEditor(new ISO8601DateFormat(), true);
		binder.registerCustomEditor(Date.class, iso8601DateEditor);
	}
}
