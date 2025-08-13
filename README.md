<p align="center-left"> <font size="12" color="#0A7373"><b>📜 Gemini Chatbot</b></font> </p>

O projeto é uma aplicação Java com gerenciamento pelo Maven, que implementa um chatbot via API. A arquitetura do projeto baseia-se em um dos princípios do SOLID - a separação de responsabilidades - utilizando camadas de serviço, modelo e aplicação. A entrada da aplicação é gerenciada pela classe ChatbotApplication onde está concentrado todo o ciclo de interação com o usuário (I/O) e a instanciação manual das dependências. A lógica está encapsulada na classe GeminiChatService, com função de orquestrar toda a comunicação com a API do Google Gemini. O serviço utiliza o java.net.http.HttpClient (Java 11+) para requisições POST e o ObjectMapper do Jackson para a conversão de DTOs.

No pacote model encontram-se os POJOS que servem como DTOs, e espelham o contrato JSON da API com a garanti do type-safety. Todas as configurações, como a chave da API, estão externalizadas em um arquivo .properties, carrega no início pela classe utilitária AppConfig com um bloco inicializador estático. O design obedece os princípios de SOLID facilitanto a manutençao e testes unitários da camada de serviço.



<p align="center-left"> <font size="12" color="#0A7373"><b>⚔️ Arquitetura do Código (Composição da Guilda) </b></font> </p>

Uma compreensão heterodoxa da arquitetura do projeto:

Em uma mesa de D&D cada membro tem habilidades específicas e funções na guilda, o que garante o sucesso nas aventuras e nas batalhas pelo caminho. Assim para compreender o funcionamento da arquitetura é possível pensar da seguinte forma: 

- O **Jogador** é o usuário que fará perguntas e respostas - ele é o que toma a ação na aventura, e no caso na aplicação.
- O **DM** ou **Mestre** é aquele que narra a aventura e gerencia o fluxo da história representada pela classe _ChatbotApplication_.
- O **Mago**, ou o _GeminiChatService_, é o personagem especialista que realizará os rituais para contatar a entidade "Gemini";
- Cada pergaminho que o mago precisa utilizar para que sua magia funcione estão no _package_ _model_, lá está estão as palavras e os componentes arcanos;
- No entanto, todo conhecimento antigo e as palavras necessárias para a conjuração da magia - ou como decifrar a magia - estão _application.properties_, o grimório que o mago carrega.
- O _pom.xml_ é o Livro do Jogador que vai definir as leis, as magias presentes na campanha.
