package br.com.alura.forum.config.validacao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

	/* classe para simplificar o json devolvido em caso de erro de validação
	 * interceptador que responde quando acontece uma excepion, recebe a annotaion
	 * 'ControllerAdvice' ou 'RestControllerAdvice'
	 */
@RestControllerAdvice
public class ErroDeValidacaoHandler {
	
	//Classe do spring que ajuda a pegar mensagens de erro de acordo com o idioma que o cliente requisitar
	@Autowired
	private MessageSource messageSource;

	/* 'ExceptionHandler' + (exceção necessária):
	 * Método que precisa ser chamado quando houver uma exceção dentro de um controller
	 * 'ResponseStatus' + (status):
	 * indica qual a resposta que o spring vai devolver para o cliente, 200, 201, 400, etc
	 * 
	 * Mesmo que tratemos o erro da página, devemos continuar devolvendo 400 (solicitação inválida)
	 */
	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public List<ErroDeFormularioDto> handle(MethodArgumentNotValidException exception) {
		List<ErroDeFormularioDto> dto = new ArrayList<>();
		
		List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
		fieldErrors.forEach(e -> {
			String mensagem = messageSource.getMessage(e, LocaleContextHolder.getLocale());
			ErroDeFormularioDto erro = new ErroDeFormularioDto(e.getField(), mensagem);
			dto.add(erro);
		});
		
		return dto;
	}
	
}
