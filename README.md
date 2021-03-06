<h2>Projeto de gerenciamento de dados de localização de telefones usando API REST.</h2>

Projeto desenvolvido com base no curso "Digital Innovation: Expert class - Desenvolvimento de testes unitários para validar uma API REST de gerenciamento de estoques de cerveja." com o instrutor Rodrigo Peleias

Para executar o projeto no terminal, digite o seguinte comando:

```shell script
mvn spring-boot:run 
```

Para executar a suíte de testes desenvolvida, basta executar o seguinte comando:

```shell script
mvn clean test
```

Após executar o comando acima, basta apenas abrir o seguinte endereço e visualizar a execução do projeto:

```
http://localhost:8080/api/v1/phones
```

Esta API foi publicada no Heroku e está acessível através do link abaixo:

```
https://kd-vc-api.herokuapp.com/
```

Exemplo de inclusão de dados (JSON) usando POST:

```
{
    "number": "5511954666111",
    "data": "-23.9998948, -45.111888948, 20.0, 27-06-2021, 09:50:00, 55.0%, HoneyPot, true, 4G"
}
```

Exemplo de consulta usando GET:

```
https://kd-vc-api.herokuapp.com/api/v1/phones?number=5511954666111
```

São necessários os seguintes pré-requisitos para a execução do projeto desenvolvido:

* Java 15 ou versões superiores.
* Maven 3.6.3 ou versões superiores.
* Intellj IDEA Community Edition ou sua IDE favorita.
* Controle de versão GIT instalado na sua máquina.

Links de referência:

* [Link para o repositório do projeto original criado por Rodrigo Peleias no GitHub](https://github.com/rpeleias/beer_api_digital_innovation_one)
* [SDKMan! para gerenciamento e instalação do Java e Maven](https://sdkman.io/)
* [Referência do Intellij IDEA Community, para download](https://www.jetbrains.com/idea/download)
* [Palheta de atalhos de comandos do Intellij](https://resources.jetbrains.com/storage/products/intellij-idea/docs/IntelliJIDEA_ReferenceCard.pdf)
* [Site oficial do Spring](https://spring.io/)
* [Site oficial JUnit 5](https://junit.org/junit5/docs/current/user-guide/)
* [Site oficial Mockito](https://site.mockito.org/)
* [Site oficial Hamcrest](http://hamcrest.org/JavaHamcrest/)
* [Referências - testes em geral com o Spring Boot](https://www.baeldung.com/spring-boot-testing)
* [Referência para o padrão arquitetural REST](https://restfulapi.net/)
* [Referência pirâmide de testes - Martin Fowler](https://martinfowler.com/articles/practical-test-pyramid.html#TheImportanceOftestAutomation)