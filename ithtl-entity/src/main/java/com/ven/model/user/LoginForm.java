package com.ven.model.user;

import org.hibernate.validator.constraints.NotBlank;

public class LoginForm {
	private static final String NOT_BLANK_MESSAGE = "{notBlank.message}";
	
	@NotBlank(message = LoginForm.NOT_BLANK_MESSAGE)
	private String nickname;

    @NotBlank(message = LoginForm.NOT_BLANK_MESSAGE)
	private String password;
    
}
