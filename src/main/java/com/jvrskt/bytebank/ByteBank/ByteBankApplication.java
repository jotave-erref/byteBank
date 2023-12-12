package com.jvrskt.bytebank.ByteBank;

import com.jvrskt.bytebank.ByteBank.domain.RegraDeNegocioException;
import com.jvrskt.bytebank.ByteBank.domain.cliente.DadosCadastroCliente;
import com.jvrskt.bytebank.ByteBank.domain.conta.ContaService;
import com.jvrskt.bytebank.ByteBank.domain.conta.DadosAberturaConta;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class ByteBankApplication {
	private static Scanner sc = new Scanner(System.in).useDelimiter("\n");
	private static ContaService service = new ContaService();


	public static void main(String[] args) {
		SpringApplication.run(ByteBankApplication.class, args);

		int opcao = exibirMenu();
		while (opcao != 8) {
			try {
				switch (opcao) {
					case 1:
						listarContas();
						break;
					case 2:
						abrirConta();
						break;
					case 3:
						encerrarConta();
						break;
					case 4:
						consultarSaldoConta();
						break;
					case 5:
						realizarSaque();
						break;
					case 6:
						realizarDeposito();
						break;
					case 7:
						listarContaPorNumero();
						break;
				}
			} catch (RegraDeNegocioException e) {
				System.out.println("Error: " + e.getMessage());
				System.out.println("Pressione qualquer tecla e ENTER para voltar ao menu");
				sc.next();
			}
			opcao = exibirMenu();
		}
		System.out.println("Finalizadno aplicação");

	}
		private static int exibirMenu(){
			System.out.println("""
                        
           BYTEBANK - ESCOLHA UMA OPÇÃO 
                1 - Listar contas abertas
                2 - Abertura de conta
                3 - Encerramento de conta
                4 - Consultar saldo de uma conta
                5 - Realizar saque em uma conta
                6 - Realizar depósito em uma conta
                7 - Buscar Conta
                8 - Sair
                """);
			return sc.nextInt();
		}

		private static void listarContas(){
			System.out.println("Contas cadastradas:");
			var contas = service.listarContas();
			contas.stream().forEach(System.out::println);
		}
		private static void abrirConta(){
			System.out.println("Digite o numero da conta:");
			var numero = sc.nextInt();
			System.out.println("Digite o nome do titular:");
			var nome = sc.next();
			System.out.println("Digite o cpf do titular:");
			String cpf = sc.next();
			System.out.println("Digite o email do titular:");
			var email = sc.next();
			service.abrirConta(new DadosAberturaConta(numero,(new DadosCadastroCliente(nome, cpf, email))));
			System.out.println("Conta aberta com sucesso!");
			System.out.println("Pressione qualquer tecla e de ENTER para voltar ao menu principal");
			sc.next();
		}

		private static void encerrarConta(){
			System.out.println("Informe o numero da conta que deseja encerrar");
			var numero = sc.nextInt();
			service.encerrarConta(numero);
			System.out.println("Conta encerrada com sucesso!");
			System.out.println("Precione qualquer tecla e de ENTER para voltar ao menu principal");
			sc.next();
		}

		private static void consultarSaldoConta(){
			System.out.println("Informe o numero da conta:");
			var numero = sc.nextInt();
			var saldo = service.consultarSaldo(numero);
			System.out.println("Saldo: " + saldo);
			service.consultarSaldo(numero);

			System.out.println("\nPrecione qualquer tecla e de ENTER para voltar ao menu principal");
			sc.next();
		}

		private static void realizarSaque(){
			System.out.println("Informe o numero da conta:");
			var numero = sc.nextInt();
			System.out.println("Informe o valor que deseja sacar:");
			var valor = sc.nextBigDecimal();
			service.sacar(numero, valor);
			System.out.println("Saque realizado com sucesso");
			System.out.println("\nPrecione qualquer tecla e de ENTER para voltar ao menu principal");
			sc.next();
		}

		private static void realizarDeposito() {
			System.out.println("Informe o numero da conta:");
			var numero = sc.nextInt();
			System.out.println("Informe o valor do deposito:");
			var valor = sc.nextBigDecimal();
			service.depositar(numero, valor);
			System.out.println("Deposito realizado com sucesso");
			System.out.println("\nPrecione qualquer tecla e de ENTER para voltar ao menu principal");
			sc.next();
		}

		private static void listarContaPorNumero(){
			System.out.println("Informe o numero da conta que deseja procurar:");
			var numeroConta = sc.nextInt();
			System.out.println("CLIENTE:");
			service.buscarContaPorNumero(numeroConta);
			System.out.println("\nPrecione qualquer tecla e de ENTER para voltar ao menu principal");
			sc.next();
		}

}
