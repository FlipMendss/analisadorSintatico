package felipe.org.example;

public class Sintatico {
	private Lexico lexico;
	private Token token;
	private int linha;
	private int coluna;


	public Sintatico(String nomeArquivo){
		linha = 1;
		coluna = 1;
		this.lexico = new Lexico(nomeArquivo);
	}

	public void analisar(){
		lerToken();
		programa();
	}

	public void lerToken(){
		token = lexico.getToken(linha, coluna);
		System.out.println(token);
		coluna = token.getColuna()+token.gettokenSize();
		linha = token.getLinha();
	}


	public void programa(){
		if(token.getClasse() == Classe.cPalRes &&
				token.getValor().getValorID().equalsIgnoreCase("program")){
			lerToken();
			if(token.getClasse() == Classe.cId){ //id()
				lerToken();
				//{A1}
				corpo();
				if(token.getClasse() == Classe.cPonto){
					lerToken();
					//{A30}
				}else{
					System.out.println("Erro: program não possui ponto final. Posição Linha: "+token.getLinha()+" Coluna: "+token.getColuna());
				}
			}else{
				System.out.println("Errro: program não tem identificador. Posição: Linha: "+token.getLinha()+" Coluna: "+token.getColuna());
			}
		}else{
			System.out.println("Erro: não iniciou com program");
		}
	}

	public void corpo(){
		declara();

		if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorID().equalsIgnoreCase("begin"))) {
			lerToken();
			sentencas();
			if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorID().equalsIgnoreCase("end"))) {
				lerToken();
			}else {
				System.out.println("Erro: sem end no fim do programa. Posição: Linha: "+token.getLinha()+" Coluna: "+token.getColuna());
			}
		}else {
			System.out.println("Erro: sem begin no corpo do programa. Posição: Linha: "+token.getLinha()+" Coluna: "+token.getColuna());
		}
	}

	public void declara(){
		if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorID().equalsIgnoreCase("var"))) {
			lerToken();
			dvar();
			mais_dc();
		}
	}

	public void mais_dc() {
		if (token.getClasse() == Classe.cPontoVirgula) {
			lerToken();
			cont_dc();
		}else {
			System.out.println("Erro: sem ';' em mais_dc. Posição: Linha: "+token.getLinha()+" Coluna: "+token.getColuna());
		}
	}

	public void cont_dc() {
		if (token.getClasse() == Classe.cId) {
			dvar();
			mais_dc();
		}
	}

	public void dvar(){
		variaveis();
		if (token.getClasse() == Classe.cDoisPontos) {
			lerToken();
			tipo_var();
		}else {
			System.out.println("Erro: sem ':' em d_var. Posição: Linha: "+token.getLinha()+" Coluna: "+token.getColuna());
		}
	}

	public void tipo_var() {
		if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorID().equalsIgnoreCase("integer"))) {
			lerToken();
		}else {
			System.out.println("Erro: não há declaração de Integer em tipo_var. Posição: Linha: "+token.getLinha()+" Coluna: "+token.getColuna());
		}
	}

	public void variaveis() {
		if (token.getClasse() == Classe.cId) {
			lerToken();
			//{A2}
			mais_var();
		}else {
			System.out.println("Erro: não há identificador na variável. Posição: Linha: "+token.getLinha()+" Coluna: "+token.getColuna());
		}
	}

	public void mais_var() {
		if (token.getClasse() == Classe.cVirgula) {
			lerToken();
			//{A2}
			variaveis();
		}
	}

	public void sentencas() {
		comando();
		mais_sentencas();
	}

	public void mais_sentencas() {
		if (token.getClasse() == Classe.cPontoVirgula) {
			lerToken();
			cont_sentencas();
		}else {
			System.out.println("Erro: sem ';' em mais_sentencas. Poisção: Linha: "+token.getLinha()+" Coluna: "+token.getColuna());
		}
	}

	public void cont_sentencas() {
		if (((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorID().equalsIgnoreCase("read"))) ||
				((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorID().equalsIgnoreCase("write"))) ||
				((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorID().equalsIgnoreCase("for"))) ||
				((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorID().equalsIgnoreCase("repeat"))) ||
				((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorID().equalsIgnoreCase("while"))) ||
				((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorID().equalsIgnoreCase("if"))) ||
				((token.getClasse() == Classe.cId))
		) {
			sentencas();
		}
	}

	public void var_read() {
		if (token.getClasse() == Classe.cId) {
			lerToken();
			//{A5}
			mais_var_read();
		}else {
			System.out.println("Erro: sem identificador em var_read. Posição: Linha: "+token.getLinha()+" Coluna: "+token.getColuna());
		}
	}

	public void mais_var_read() {
		if (token.getClasse() == Classe.cVirgula) {
			lerToken();
			var_read();
		}
	}

	public void var_write() {
		if (token.getClasse() == Classe.cId) {
			lerToken();
			//{A6}
			mais_var_write();
		}else {
			System.out.println("Erro: sem identificador em var_write. Posição: Linha: "+token.getLinha()+" Coluna: "+token.getColuna());
		}
	}

	public void mais_var_write() {
		if (token.getClasse() == Classe.cVirgula) {
			lerToken();
			var_write();
		}
	}

	public void comando() {
		if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorID().equalsIgnoreCase("read"))){
			lerToken();
			if (token.getClasse() == Classe.cParEsq) {
				lerToken();
				var_read();
				if (token.getClasse() == Classe.cParDir) {
					lerToken();
				}else {
					System.out.println("Erro: sem '(' após o var_read. Posição: Linha: "+token.getLinha()+" Coluna: "+token.getColuna());
				}
			}else {
				System.out.println("Erro: sem '(' após o read. Posição: Linha: "+token.getLinha()+" Coluna: "+token.getColuna());
			}
		}else
			if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorID().equalsIgnoreCase("write"))){
				lerToken();
				if (token.getClasse() == Classe.cParEsq) {
					lerToken();
					var_write();
					if (token.getClasse() == Classe.cParDir) {
						lerToken();
					}else {
						System.out.println("Erro: sem ')' no comando após var_write. Posição: Linha: "+token.getLinha()+" Coluna: "+token.getColuna());
					}
				}else {
					System.out.println("Erro: sem ')' no comando após write. Posição: Linha: "+token.getLinha()+" Coluna: "+token.getColuna());
				}
			}else
				if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorID().equalsIgnoreCase("for"))){
					lerToken();
					if (token.getClasse() == Classe.cId) {
						lerToken();
						//{A25}
						if (token.getClasse() == Classe.cAtribuicao){
							lerToken();
							expressao();
							//{A26}
							if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorID().equalsIgnoreCase("to"))){
								lerToken();
								//{A27}
								expressao();
								//{A28}
								if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorID().equalsIgnoreCase("do"))){
									lerToken();
									if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorID().equalsIgnoreCase("begin"))){
										lerToken();
										sentencas();
										if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorID().equalsIgnoreCase("end"))){
											lerToken();
											//{A29}									
										}else {
											System.out.println("Erro: sem end ao final do For. Posição: Linha: "+token.getLinha()+" Coluna: "+token.getColuna());
										}
									}else {
										System.out.println("Erro: sem begin após o Do no For. Posição: Linha: "+token.getLinha()+" Coluna: "+token.getColuna());
									}
								}else {
									System.out.println("Erro: sem do no meio do For. Posção: Linha: "+token.getLinha()+" Coluna: "+token.getColuna());
								}
							}else {
								System.out.println("Erro: sem to no meio do For. Posição: Linha: "+token.getLinha()+" Coluna: "+token.getColuna());
							}
						}else {
							System.out.println("Erro: sem ':=' no meio do For. Posição: Linha: "+token.getLinha()+" Coluna: "+token.getColuna());
						}
					}else {
						System.out.println("Erro: sem identificador no inicio do For. Posição: Linha: "+token.getLinha()+" Coluna: "+token.getColuna());
					}
				}else
					if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorID().equalsIgnoreCase("repeat"))){
						lerToken();
						//{A23}
						sentencas();
						if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorID().equalsIgnoreCase("until"))){
							lerToken();
							if (token.getClasse() == Classe.cParEsq){
								lerToken();
								condicao();
								if (token.getClasse() == Classe.cParDir){
									lerToken();
									//{A24}
								}else {
									System.out.println("Erro: sem ')' no repeat após a condição. Posição: Linha: "+token.getLinha()+" Coluna: "+token.getColuna());
								}
							}else {
								System.out.println("Erro: sem '(' no repeat após o until. Posição: Linha: "+token.getLinha()+" Coluna: "+token.getColuna());
							}
						}else {
							System.out.println("Erro: sem until após o repeat. Posicao: Linha: "+token.getLinha()+" Coluna: "+token.getColuna());
						}
					}
					else if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorID().equalsIgnoreCase("while"))){
						lerToken();
						//{A20}
						if (token.getClasse() == Classe.cParEsq){
							lerToken();
							condicao();
							if (token.getClasse() == Classe.cParDir){
								lerToken();
								//{A21}
								if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorID().equalsIgnoreCase("do"))){
									lerToken();
									if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorID().equalsIgnoreCase("begin"))){
										lerToken();
										sentencas();
										if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorID().equalsIgnoreCase("end"))){
											lerToken();
											//{A22}
										}else {
											System.out.println("Erro: sem end ao final do While. Posição: Linha: "+token.getLinha()+" Coluna: "+token.getColuna());
										}
									}else {
										System.out.println("Erro: sem begin após o Do do While. Posição: Linha: "+token.getLinha()+" Coluna: "+token.getColuna());
									}
								}else {
									System.out.println("Erro: sem do dentro do While. Poisção: Linha: "+token.getLinha()+" Coluna: "+token.getColuna());
								}
							}else {
								System.out.println("Erro: sem ')' no While. Posição: Linha: "+token.getLinha()+" Coluna: "+token.getColuna());
							}
						}else {
							System.out.println("Erro: sem '(' no While. Posição: Linha: "+token.getLinha()+" Coluna: "+token.getColuna());
						}
					}
					else if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorID().equalsIgnoreCase("if"))){
						lerToken();
						if (token.getClasse() == Classe.cParEsq){
							lerToken();
							condicao();
							if (token.getClasse() == Classe.cParDir){
								lerToken();
								//{A17}
								if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorID().equalsIgnoreCase("then"))){
									lerToken();
									if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorID().equalsIgnoreCase("begin"))){
										lerToken();
										sentencas();
										if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorID().equalsIgnoreCase("end"))){
											lerToken();
											//{A18}
											pfalsa();
											//{A19}
										}else {
											System.out.println("Erro: sem end no final do If. Posição: Linha: "+token.getLinha()+" Coluna: "+token.getColuna());
										}
									}else {
										System.out.println("Erro: sem begin após o Then do If. Posição: Linha: "+token.getLinha()+" Coluna: "+token.getColuna());
									}
								}else {
									System.out.println("Erro: sem then no comando If. Posição: Linha: "+token.getLinha()+" Coluna: "+token.getColuna());
								}
							}else {
								System.out.println("Erro: sem ')' no If. Posição: Linha: "+token.getLinha()+" Coluna: "+token.getColuna());
							}
						}else {
							System.out.println("Erro: sem '(' no If. Posição: Linha: "+token.getLinha()+" Coluna: "+token.getColuna());
						}
					}
					else if (token.getClasse() == Classe.cId){
						lerToken();
						//{A13}
						if (token.getClasse() == Classe.cAtribuicao){
							lerToken();
							expressao();
							//{A14}
						}else{
							System.out.println("Erro: sem o id do Comando. Posição: Linha: "+token.getLinha()+" Coluna: "+token.getColuna());
						}
					}
	}

	public void condicao() {
		expressao();
		relacao();
		//{A15}
		expressao();
		//{A16}		
	}

	public void pfalsa() {
		if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorID().equalsIgnoreCase("else"))){
			lerToken();
			if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorID().equalsIgnoreCase("begin"))){
				lerToken();
				sentencas();
				if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorID().equalsIgnoreCase("end"))){
					lerToken();
				}else {
					System.out.println("Erro: sem end ao final de pfalsa. Posição: Linha: "+token.getLinha()+" Coluna: "+token.getColuna());
				}
			}else {
				System.out.println("Erro: sem begin após o else de pfalsa. Posição: Linha: "+token.getLinha()+" Coluna: "+token.getColuna());
			}
		}
	}

	public void relacao() {
		if (token.getClasse() == Classe.cIgual) {
			lerToken();
		}else if (token.getClasse() == Classe.cMaior) {
			lerToken();
		}else if (token.getClasse() == Classe.cMenor) {
			lerToken();
		}else if (token.getClasse() == Classe.cMaiorIgual) {
			lerToken();
		}else if (token.getClasse() == Classe.cMenorIgual) {
			lerToken();
		}else if (token.getClasse() == Classe.cDiferente) {
			lerToken();
		}else {
			System.out.println("Erro: sem operador matemático de relação. Posição: Linha: "+token.getLinha()+" Coluna: "+token.getColuna());
		}
	}

	public void expressao() {
		termo();
		outros_termos();
	}

	public void outros_termos() {
		if (token.getClasse() == Classe.cMais || token.getClasse() == Classe.cMenos) {
			op_ad();
			//{A9}
			termo();
			//{A10}
			outros_termos();
		}
	}

	public void op_ad() {
		if (token.getClasse() == Classe.cMais || token.getClasse() == Classe.cMenos) {
			lerToken();
		}else {
			System.out.println("Erro: sem operador de adição ou subtração no op_ad. Posição: Linha: "+token.getLinha()+" Coluna: "+token.getColuna());
		}
	}

	public void termo() {
		fator();
		mais_fatores();
	}

	public void mais_fatores() {
		if (token.getClasse() == Classe.cMultiplicacao || token.getClasse() == Classe.cDivisao) {
			op_mul();
			//{A11}
			fator();
			//{A12}
			mais_fatores();
		}
	}

	public void op_mul() {
		if (token.getClasse() == Classe.cMultiplicacao || token.getClasse() == Classe.cDivisao) {
			lerToken();
		}else {
			System.out.println("Erro: sem operador de multiplicação ou divisão no op_mul. Posição: Linha: "+token.getLinha()+" Coluna: "+token.getColuna());
		}
	}

	public void fator() {
		if (token.getClasse() == Classe.cId) {
			lerToken();
			//{A7}
		}else if (token.getClasse() == Classe.cInt || token.getClasse() == Classe.cReal) {
			lerToken();
			//{A8}
		}else if (token.getClasse() == Classe.cParEsq){
			lerToken();
			expressao();
			if (token.getClasse() == Classe.cParDir){
				lerToken();
			}else {
				System.out.println("Erro: sem ')' ao final da expressão. Posição: Linha: "+token.getLinha()+" Coluna: "+token.getColuna());
			}
		}else {
			System.out.println("Erro: sem fator (identificador, numero ou expressão). Posição: Linha: "+token.getLinha()+" Coluna: "+token.getColuna());
		}
	}

	public void id(){
		if(token.getClasse()==Classe.cId){
			//{A3}
		}else{
			System.out.println("Erro: sem id após o program. Posição: Linha: "+token.getLinha()+" Coluna: "+token.getColuna()+" -> Faltou o id após o program");
		}
	}
}
