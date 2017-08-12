package myJUnitTests;

import static org.junit.Assert.fail;
import java.io.File;
import java.util.concurrent.TimeUnit;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import utility.excelUtils;

public class myJUnit1 {
	private String testUrl;
	WebDriver driver = new FirefoxDriver();
	//WebDriver driver = new InternetExplorerDriver();
	//nome e caminho padrão onde grava os prints de erro
    String scrShot_arq = "c:\\erro.jpg";
    //caminho do arquivo excel para massa de dados
    String excelArq = "C:\\massa_FormCadastro.xlsx";
    
    excelUtils workBook = new excelUtils();
    
    //campos do form
	String nome;
	String email;
	String endereco;
	String nascDia;
	String nascMes;
	String nascAno;
	String cpf;
	String escolaridade;

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
		
	@Before
	public void inicializar () {
		testUrl = "https://docs.google.com/forms/d/e/1FAIpQLSfyRiKLobjHXoTI0PjEWK4hpmKHfmNuL0Sx-6ut31AGMcMNwg/viewform?hl=en"; //obs: o parametro hl=en é para abrir o form em ingles (e todas as mensagens de erro, etc)
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS); //configuração para esperar a página carregar com tempo de sobra
		driver.get(testUrl);
		driver.manage().window().setSize(new Dimension(1600,900));
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@After
	public void finalizar () {
		//desabilita pop de confirmação do fechamento do browser
		JavascriptExecutor js = (JavascriptExecutor) driver;
	    js.executeScript("window.onbeforeunload = function() {};"); 

	    if (driver != null) {
	        driver.quit();	        
	    }
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Teste 0 - Criar Massa
	// Pass criteria: Insere no sistema vários registros importados de arquivo Excel
	@Test
	
	public void Test0() throws Exception {
		workBook.setExcelFile(excelArq, "Massa1");

		
		// inicializando variáveis. OBS: AS coordenadas comecam em (0,0) e nao (1,1)
		
			nome = workBook.getCellData(0, 0);
			email = workBook.getCellData(0, 1);
			endereco = workBook.getCellData(0, 2);
			nascDia = workBook.getCellData(0, 3);
			nascMes = workBook.getCellData(0, 4);
			nascAno = workBook.getCellData(0, 5);
			cpf = workBook.getCellData(0, 6);
			escolaridade = workBook.getCellData(0, 7);
		
		String msgSucessoExpected = "Obrigado pelas informações!";
		scrShot_arq = "c:\\erro_Test0.jpg";
		
		try {
			//chamando a class preencheForm que irá preencher os dados na tela e retornará a msg de sucesso
			String msgSucessoActual = preencheForm(driver, nome, email, endereco, nascDia, nascMes, nascAno, cpf, escolaridade);		
			
			//verifica se a mensagem de sucesso está correta
			if (msgSucessoActual.equals(msgSucessoExpected)) {
				System.out.println("Cadastro realizado com sucesso");
			}
			else {
				// em caso de defeito, tira um screenshot
				tiraScreenshot(scrShot_arq); //tira um screenshot
				fail("Ocorreu alguma falha na finalização do cadastro/Mensagem de sucesso incorreta");	
			}
			System.out.println("Test_0 PASSOU!!");	
		}
		catch (Exception e) {
			tiraScreenshot(scrShot_arq); //tira um screenshot
			fail("Ocorreu um erro desconhecido!");
		}
	}//Test0
	
	
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Teste 1 - Cadastro realizado com sucesso
	// Pass criteria: Dados salvos no sistema e mensagem final "Obrigado pelas informações!" é exibida.
	//@Test
	
	public void Test1() throws Exception {	
		// inicializando variáveis. Inserir somente valores válidos, de acordo com o requisito	
		nome = "Sullivan";
		email = "sullivan.tanaka@gmail.com";
		endereco = "Av qualquer";
		nascDia = "12";
		nascMes = "12";
		nascAno = "1978";
		cpf = "12345678901";
		escolaridade = "Superior completo";
		String msgSucessoExpected = "Obrigado pelas informações!";
		scrShot_arq = "c:\\erro_Test1.jpg";
					
		try {
			//chamando a class preencheForm que irá preencher os dados na tela e retornará a msg de sucesso
			String msgSucessoActual = preencheForm(driver, nome, email, endereco, nascDia, nascMes, nascAno, cpf, escolaridade);		
			
			//verifica se a mensagem de sucesso está correta
			if (msgSucessoActual.equals(msgSucessoExpected)) {
				System.out.println("Cadastro realizado com sucesso");
			}
			else {
				// em caso de defeito, tira um screenshot
				tiraScreenshot(scrShot_arq); //tira um screenshot
				fail("Ocorreu alguma falha na finalização do cadastro/Mensagem de sucesso incorreta");	
			}
			System.out.println("Test_1 PASSOU!!");	
		}
		catch (Exception e) {
			tiraScreenshot(scrShot_arq); //tira um screenshot
			fail("Ocorreu um erro desconhecido!");
		}
	}//Test1
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Teste 2 - Validar campo Nome em branco
	// Pass criteria: Form exibe a mensagem: "This is a required question" e pinta de vermelho
	//	
	@Test
	public void Test2() throws Exception {
			// inicializando variáveis. Inserir somente valores válidos, de acordo com o requisito
		nome = "";
		email = "sullivan.tanaka@gmail.com";
		endereco = "Av qualquer";
		nascDia = "12";
		nascMes = "12";
		nascAno = "1978";
		cpf = "12345678901";
		escolaridade = "Superior completo"; 			
		String msgErroExpected = "This is a required question";
		//String msgErroExpected = "Esta pergunta é obrigatória"; //caso queira validar em portugues, altere também o parametro hl na URL!!
		String msgErroActual = "Nao iniciado";
		scrShot_arq = "c:\\erro_Test2.jpg";
						
		try {
			//chamando a class preencheForm que retorna a mensagem de erro exibida na tela
			msgErroActual = preencheForm(driver, nome, email, endereco, nascDia, nascMes, nascAno, cpf, escolaridade);	
				
			//verifica se a mensagem de erro é exibida e está correta
			if (msgErroActual.equals(msgErroExpected)) { //Pass
				System.out.println("Campo em branco validado com sucesso");
			}
			else { //Fail
				System.out.println("Mensagem de erro esperada: <" + msgErroExpected + ">");
				System.out.println("Mensagem de erro exibida: <" + msgErroActual + ">");
				tiraScreenshot(scrShot_arq); //tira um screenshot
				fail("O campo não foi validado corretamente. A mensagem de erro esperada não foi exibida ou foi diferente da esperada");	
			}
			System.out.println("Test2 PASSOU!!");	
		}
		catch (Exception e) { //Fail por exception
			tiraScreenshot(scrShot_arq); //tira um screenshot
			System.out.println("Mensagem de erro exibida: <" + msgErroActual + ">");
			fail("Ocorreu um erro desconhecido!");
		}
	}//Test2
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Teste 3 - Validar campo Email em branco
	// Pass criteria: Form exibe a mensagem: "This is a required question" e pinta de vermelho
	//	
	//@Test
	public void Test3() throws Exception {
			// inicializando variáveis. Inserir somente valores válidos, de acordo com o requisito
		nome = "Sullivan";
		email = "";
		endereco = "Av qualquer";
		nascDia = "12";
		nascMes = "12";
		nascAno = "1978";
		cpf = "12345678901";
		escolaridade = "Superior completo"; 			
			String msgErroExpected = "This is a required question";
			//String msgErroExpected = "Esta pergunta é obrigatória"; //caso queira validar em portugues, altere também o parametro hl na URL!!
			String msgErroActual = "Nao iniciado";
			scrShot_arq = "c:\\erro_Test3.jpg";
						
			try {
				//chamando a class preencheForm que retorna a mensagem de erro exibida na tela
				msgErroActual = preencheForm(driver, nome, email, endereco, nascDia, nascMes, nascAno, cpf, escolaridade);	
				
				//verifica se a mensagem de erro é exibida e está correta
				if (msgErroActual.equals(msgErroExpected)) { //Pass
					System.out.println("Campo em branco validado com sucesso");
				}
				else { //Fail
					System.out.println("Mensagem de erro esperada: <" + msgErroExpected + ">");
					System.out.println("Mensagem de erro exibida: <" + msgErroActual+">");
					tiraScreenshot(scrShot_arq); //tira um screenshot
					fail("O campo não foi validado corretamente. A mensagem de erro esperada não foi exibida ou foi diferente da esperada");
				}
				System.out.println("Test_3 PASSOU!!");	
			}
			catch (Exception e) { //Fail por exception
				tiraScreenshot(scrShot_arq); //tira um screenshot
				fail("Ocorreu um erro desconhecido!");
			}
		}//Test3

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//
	// Teste 4 - Validar campo CPF somente números
	// Pass criteria: Form exibe a mensagem: "Digitar somente números!" e pinta de vermelho
	//	
	//@Test
	public void Test4() throws Exception {
	// inicializando variáveis. Inserir somente valores válidos, de acordo com o requisito
		nome = "Sullivan";
		email = "sullivan.tanaka@gmail.com";
		endereco = "Av qualquer";
		nascDia = "12";
		nascMes = "12";
		nascAno = "1978";
		cpf = "abcdef";
		escolaridade = "Superior completo"; 			
		String msgErroExpected = "Digitar somente números!";
		//String msgErroExpected = "Esta pergunta é obrigatória"; //caso queira validar em portugues, altere também o parametro hl na URL!!
		String msgErroActual = "Nao iniciado";
		scrShot_arq = "c:\\erro_Test4.jpg";
		
		try {
			//chamando a class preencheForm que retorna a mensagem de erro exibida na tela
			msgErroActual = preencheForm(driver, nome, email, endereco, nascDia, nascMes, nascAno, cpf, escolaridade);	
			
			//verifica se a mensagem de erro é exibida e está correta
			if (msgErroActual.equals(msgErroExpected)) { //Pass
				System.out.println("Campo em branco validado com sucesso");
			}
			else { //Fail
				System.out.println("Mensagem de erro esperada: <" + msgErroExpected + ">");
				System.out.println("Mensagem de erro exibida: <" + msgErroActual+">");
				tiraScreenshot(scrShot_arq); //tira um screenshot
				fail("O campo não foi validado corretamente. A mensagem de erro esperada não foi exibida ou foi diferente da esperada");
			}
			System.out.println("Test_4 PASSOU!!");	
		}
		catch (Exception e) { //Fail por exception
			tiraScreenshot(scrShot_arq); //tira um screenshot
			fail("Ocorreu um erro desconhecido!");
			}
	}//Test4

	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//Declaração das classes utilizadas nesta suíte de testes																		 //
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	public String preencheForm(WebDriver wd, String nome, String email, String endereco, String nascDia, String nascMes, String nascAno, String cpf, String escolaridade) {
	//
	//preencheForm: preenche todos os campos com valores passados nos parâmetros, faz o submit e retorna a msg final de sucesso
	//OBS: Campos mapeados para o Firefox! No chrome este mapeamento nao fuciona para os xpath abaixo!
	//
		String errorMsg = "";
			
		// localizando as variáveis correspondendo aos campos do formulário de contato
		WebElement campoNome = wd.findElement(By.name("entry.2005620554"));
		WebElement campoEmail = wd.findElement(By.name("entry.1045781291"));
		WebElement campoEndereco = wd.findElement(By.name("entry.1065046570"));
		WebElement campoNascDia = wd.findElement(By.xpath(".//*[@id='mG61Hd']/div/div[2]/div[2]/div[4]/div[2]/div/div[1]/div/div[2]/div[1]/div/div[1]/input"));
		WebElement campoNascMes = wd.findElement(By.xpath(".//*[@id='mG61Hd']/div/div[2]/div[2]/div[4]/div[2]/div/div[3]/div/div[2]/div[1]/div/div[1]/input"));
		WebElement campoNascAno = wd.findElement(By.xpath(".//*[@id='mG61Hd']/div/div[2]/div[2]/div[4]/div[2]/div/div[5]/div/div[2]/div[1]/div/div[1]/input"));
		WebElement campoCPF = wd.findElement(By.name("entry.1166974658"));
		WebElement campoEscolaridade = wd.findElement(By.xpath(".//*[@id='mG61Hd']/div/div[2]/div[2]/div[6]/div[2]/div[1]/div[1]/div[1]"));
	
		//preenchendo os campos do formulário de acordo com os parâmetros passados
		campoNome.sendKeys(nome);
		campoEmail.sendKeys(email);
		campoEndereco.sendKeys(endereco);
		campoNascDia.sendKeys(nascDia);
		campoNascMes.sendKeys(nascMes);
		campoNascAno.clear(); //Como este campo vem com valor pré-definido devemos limpá-lo antes!!
		campoNascAno.sendKeys(nascAno);
		campoCPF.sendKeys(cpf);
		campoEscolaridade.sendKeys(escolaridade);

		//submit do formulário
		campoNome.submit();

					
		//validação campos em branco
		if (nome == "") { 
			errorMsg = wd.findElement(By.id("i.err.1633920210")).getText(); //captura msg de erro exibida na tela abaixo do campo Nome
		}
		
		if (email == "") {
			errorMsg = wd.findElement(By.id("i.err.227649005")).getText(); //captura msg de erro exibida na tela abaixo do campo Email
		}
		
		if (endereco == "") {
			errorMsg = wd.findElement(By.id("i.err.790080973")).getText(); //captura msg de erro exibida na tela abaixo do campo Endereço
		}
		
		if (cpf == "") {
			errorMsg = wd.findElement(By.id("i.err.1770822543")).getText(); //captura msg de erro exibida na tela abaixo do campo CPF
		}
		
		//validacao formatacao campos ////////////////////////// TERMINAR DEPOIS !!!!!!!!!!!!!!!!!!!!!! ///////////////////////////////////////
		if (!isNumeric(cpf)) {
			errorMsg = wd.findElement(By.id("i.err.1770822543")).getText(); //CPF nao numérico, captura msg de erro exibida na tela abaixo do campo CPF
		}
		
		
		//inserir depois demais campos
						
		//captura e retorna a mensagem de sucesso (se houver)	
		if (errorMsg.isEmpty()) {
			String msgSucessoActual = wd.findElement(By.className("freebirdFormviewerViewResponseConfirmationMessage")).getText();
			return msgSucessoActual;
		}
		else {
			return errorMsg;
		}
	}//preencheForm

	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	
	public void tiraScreenshot (String scrShot_arq) throws Exception {
		File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(scrFile, new File(scrShot_arq));
	} //tiraScreenshot
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public boolean isNumeric(String s) {
	    return s != null && s.matches("[-+]?\\d*\\.?\\d+");
	}//isNumeric
	
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
} //myJUnit1
