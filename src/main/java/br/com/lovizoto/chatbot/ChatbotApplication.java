package br.com.lovizoto.chatbot;

import br.com.lovizoto.chatbot.model.Content;
import br.com.lovizoto.chatbot.model.GenerationConfig;
import br.com.lovizoto.chatbot.model.Part;
import br.com.lovizoto.chatbot.service.GeminiChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class ChatbotApplication {

    private static final Logger log = LoggerFactory.getLogger(ChatbotApplication.class);
    private final GeminiChatService chatService;
    private final List<Content> chatHistory;

    public ChatbotApplication() {
        this.chatService = new GeminiChatService();
        this.chatHistory = new ArrayList<>();
        initializeChat();
    }

    private void initializeChat() {
        log.info("Inicializando o contexto do chatbot...");
        String systemPrompt = """
              
                """; // inserir um contexto inicial no systemPrompt

        // O "system prompt" pode ser o primeiro item do histórico
        chatHistory.add(new Content("user", List.of(new Part(systemPrompt))));
        chatHistory.add(new Content("model", List.of(new Part("Olá! Sou o Michel, especialista em soluções de tecnologia da Informo. Como posso te ajudar hoje?"))));
    }

    public void start() {
        System.out.println("Chatbot Iniciado. Digite 'sair' para encerrar.");
        System.out.println("Michel: Olá! Sou o Michel, especialista em soluções de tecnologia da Informo. Como posso te ajudar hoje?");

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("Você: ");
                String userInput = scanner.nextLine();

                if (userInput.equalsIgnoreCase("sair")) {
                    System.out.println("Encerrando o chatbot. Até logo!");
                    break;
                }

                // Adiciona a mensagem do usuário ao histórico
                chatHistory.add(new Content("user", List.of(new Part(userInput))));

                // Configurações de geração
                GenerationConfig config = new GenerationConfig();
                config.setTemperature(0.7f);
                config.setMaxOutputTokens(8192);

                // Chama o serviço para obter a resposta
                String botResponse = chatService.generateContent(chatHistory, config)
                        .orElse("Desculpe, não consegui processar sua solicitação no momento.");

                // Adiciona a resposta do bot ao histórico
                chatHistory.add(new Content("model", List.of(new Part(botResponse))));
                System.out.println("Michel: " + botResponse);
            }
        }
    }

    public static void main(String[] args) {
        log.info("Iniciando a aplicação Chatbot Gemini...");
        ChatbotApplication app = new ChatbotApplication();
        app.start();
        log.info("Aplicação Chatbot Gemini finalizada.");
    }
}