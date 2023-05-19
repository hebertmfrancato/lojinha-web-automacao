package modulos.produtos;

import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import paginas.LoginPage;

import java.time.Duration;

@DisplayName("Testes Web do Módulo de Produtos")
public class ProdutosTest {

    private WebDriver navegador;

    @BeforeEach
    public void beforeEach() {
        // Abrir o navegador
        System.setProperty("webdriver.chrome.driver", "/Users/hebertfrancato/Documents/ChromeDriver/chromedriver");
        navegador = new ChromeDriver();

        // Vou maximizar a tela
        navegador.manage().window().maximize();

        // Vou definir um tempo de espera padrão de 5 segundos
        navegador.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));

        // Navegar para a página da Lojinha Web
        navegador.get("http://165.227.93.41/lojinha-web/v2/");
    }

    @Test
    @DisplayName("Não é permitido registrar um produto com o valor igual a zero")
    public void testNaoEPermitidoRegistrarProdutoComValorIgualAZero() {
        // Fazer login
        String mensagemApresentada = new LoginPage(navegador)
                // 1 - Usuário
                .informarOUsuario("admin")
                // 2 - Senha
                .informarASenha("admin")
                // 3 - Clicar no botão entrar
                .submeterFormularioDeLogin()
                // 4 - Vou para a tela de registro de produto
                .acessarFormularioAdicaoNovoProduto()
                // 5 - Vou preencher dados do produto e o valor será igual a zero
                .informarNomeDoProduto("Macbook Air M1")
                .informarValorDoProduto("000")
                .informarCoresDoProduto("preto, branco")
                // 6 - Vou submeter o formulário
                .submeterFormularioDeAdicaoComErro()
                // 7.1 - Vou validar que a mensagem do erro foi apresentada
                .capturarMensagemApresentada();

        // Vou validar que a mensagem do erro foi apresentada
        Assertions.assertEquals("O valor do produto deve estar entre R$ 0,01 e R$ 7.000,00", mensagemApresentada);
    }

    @Test
    @DisplayName("Não é permitido regitrar um produto com o valor maior que sete mil")
    public void testNaoEPermitidoRegistrarProdutoComValorAcimaDeSeteMil() {
        String mensagemApresentada = new LoginPage(navegador)
                .informarOUsuario("admin")
                .informarASenha("admin")
                .submeterFormularioDeLogin()
                .acessarFormularioAdicaoNovoProduto()
                .informarNomeDoProduto("Macbook Pro M1")
                .informarValorDoProduto("700001")
                .informarCoresDoProduto("space gray")
                .submeterFormularioDeAdicaoComErro()
                .capturarMensagemApresentada();

        Assertions.assertEquals("O valor do produto deve estar entre R$ 0,01 e R$ 7.000,00", mensagemApresentada);
    }

    @Test
    @DisplayName("Posso adicionar produtos que estejam no limite de R$ 0,01")
    public void testPossoAdicionarProdutosComValorDeUmCentavo() {
        String mensagemApresentada = new LoginPage(navegador)
                .informarOUsuario("admin")
                .informarASenha("admin")
                .submeterFormularioDeLogin()
                .acessarFormularioAdicaoNovoProduto()
                .informarNomeDoProduto("iPhone 14 Pro")
                .informarValorDoProduto("001")
                .informarCoresDoProduto("preto")
                .submeterFormularioDeAdicaoComSucesso()
                .capturarMensagemApresentada();

        Assertions.assertEquals("Produto adicionado com sucesso", mensagemApresentada);
    }

    @Test
    @DisplayName("Posso adicionar produtos que estejam no limite de R$ 7.000,01")
    public void testPossoAdicionarProdutosComValorDeValorDeSeteMilReais() {
        String mensagemApresentada = new LoginPage(navegador)
                .informarOUsuario("admin")
                .informarASenha("admin")
                .submeterFormularioDeLogin()
                .acessarFormularioAdicaoNovoProduto()
                .informarNomeDoProduto("iPhone 14 Pro")
                .informarValorDoProduto("700000")
                .informarCoresDoProduto("preto")
                .submeterFormularioDeAdicaoComSucesso()
                .capturarMensagemApresentada();

        Assertions.assertEquals("Produto adicionado com sucesso", mensagemApresentada);
    }

    @AfterEach
    public void afterEach() {
        // Vou fechar o navegador
        navegador.quit();
    }
}
