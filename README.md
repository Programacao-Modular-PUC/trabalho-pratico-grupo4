# SisHosp Maraú

O Ministério do Turismo tem incentivado os brasileiros a conhecer melhor o Brasil, exibindo imagens de cenários de exuberante beleza. Um desses cenários é Maraú – BA, que é uma região peninsular, reduto de Mata Atlântica preservado. Maraú possui piscinas naturais, recifes de coral, mares interiores, manguezais, cachoeiras, trilhas ecológicas e diversas praias.

Com o aumento da demanda por hospedagem, moradores locais passaram a oferecer quartos em suas residências. Para organizar esse processo de forma eficiente e escalável, será desenvolvido um Sistema de Informação Modular com API REST, utilizando conceitos avançados de Programação Orientada a Objetos (POO).

### Objetivo
Desenvolver, ao longo do semestre, um sistema completo de gerenciamento de hospedagens, contemplando:
* Modelagem orientada a objetos
* Arquitetura em camadas (Controller, Service, Repository, Model)
* API REST com Spring Boot
* Persistência em banco de dados (MySQL)
* Testes automatizados
* Aplicação de padrões de projeto

### Escopo do Sistema
O sistema deve permitir:
* Gerenciamento de residências e quartos
* Cadastro e autenticação de clientes
* Realização de reservas e aluguéis
* Cálculo automático de diárias
* Emissão de recibos
* Controle de disponibilidade
* Histórico de hospedagens

### Regras de Negócio
1.  **Cálculo de Diárias:** As diárias sempre iniciam às 12h. O cálculo deve considerar:
    * Entrada após 12h → conta como diária completa.
    * Saída após 12h → adiciona nova diária.
2.  **Valor da Diária:** Não é informado diretamente. Deve ser calculado com base em:
    * Valor base (definido pelo proprietário).
    * Tipo do quarto.
    * Itens adicionais (ar-condicionado, hidromassagem).
3.  **Disponibilidade:** Um quarto não pode ser alugado se já estiver ocupado no período.
4.  **Reservas:** Deve ser possível realizar reservas futuras.
5.  **Pagamento:** Um aluguel deve gerar um pagamento associado.

### Características e Requisitos do Sistema
1.  **Residência:** Contém pelo menos: endereço, número, bairro, CEP, telefone, e-mail e uma lista de quartos para alugar.
2.  **Quarto:** Pode ser individual ou para casal. Possui: valor da diária, indicador de ar-condicionado e indicador de banheira de hidromassagem.
3.  **Valor Base:** A diária base de cada quarto (solteiro ou casal) é definida pelo proprietário.
4.  **Registro de Aluguel:** É necessário armazenar: Residência, quarto, cliente, data de entrada e saída, quantidade de diárias e valor final.
5.  **Histórico:** Guardar histórico de aluguéis por residência.
6.  **Composição de Valor:** O valor final é definido pelo valor base + adicionais (ar-condicionado e/ou hidromassagem).
7.  **Cliente:** Contém pelo menos: nome, CPF, endereço, telefone e e-mail de contato.
8.  **Formulário de Aluguel:** Deve ser impresso na tela com o seguinte formato:
    * Data e horário de entrada:
    * Data e horário de saída:
    * Número de diárias:
    * Total a pagar:

## Integrantes

* Anny Victorya Azevedo Oliveira
* Gustavo Rodrigues Barbara Moreira 

## Professor

Glender Brás de Medeiros

## Diagrama de Classes
 
![UML](/img/UML.drawio.png)

## Cartão CRC
[Clique aqui para abrir os Cartões CRC](crc.docx)
