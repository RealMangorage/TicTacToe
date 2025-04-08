package org.mangorage.chatgpt;


import io.github.stefanbratanov.jvm.openai.ChatClient;
import io.github.stefanbratanov.jvm.openai.ChatMessage;
import io.github.stefanbratanov.jvm.openai.CreateChatCompletionRequest;
import io.github.stefanbratanov.jvm.openai.OpenAI;
import io.github.stefanbratanov.jvm.openai.OpenAIModel;

import org.mangorage.game.api.Board;
import org.mangorage.game.api.Result;

import org.mangorage.game.players.Player;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class ChatGPTPlayer implements Player {
    private static final ExecutorService SERVICE = Executors.newWorkStealingPool();
    private static final String API_KEY;

    static {
        try {
            API_KEY = Files.readString(Path.of("cfg/api_key_openai.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private OpenAI openAI;

    private final String name;
    public ChatGPTPlayer(String name) {
        this.name = name;
        this.openAI = OpenAI.newBuilder(API_KEY)
                .build();
    }

    @Override
    public void commitTurn(Board board) {
        var boardState = board.getBoard();
        var symbol = board.getActivePlayerSymbol();

        SERVICE.submit(() -> {
          try {
              Thread.sleep(20000);
              // Construct the prompt based on the current board state
              StringBuilder boardDisplay = new StringBuilder("Current Board:\n");
              for (int i = 0; i < 9; i++) {
                  if (i % 3 == 0 && i != 0) {
                      boardDisplay.append("\n");
                  }
                  boardDisplay.append("[").append(boardState[i].isEmpty() ? (i + 1) : boardState[i]).append("] ");
              }
              boardDisplay.append("\n\nYou are Character " + symbol);
              boardDisplay.append("\n\nPlease select one of the available positions by responding with the corresponding number. Respond with only the number and no additional text");

              // Create a chat message
              ChatMessage message = ChatMessage.userMessage(boardDisplay.toString());

              // Create a chat client
              ChatClient chatClient = openAI.chatClient();

              // Build the chat completion request
              CreateChatCompletionRequest request = CreateChatCompletionRequest.newBuilder()
                      .model(OpenAIModel.GPT_4o_MINI)
                      .messages(Arrays.asList(message))
                      .build();

              try {
                  // Send the request to the OpenAI API
                  String aiMove = chatClient.createChatCompletion(request).choices().get(0).message().content().trim();


                  System.out.println("-----------------------------------------------------------------");
                  System.out.println(boardDisplay);
                  System.out.println(aiMove);

                  // Parse the AI's move and update the board
                  int position = Integer.parseInt(aiMove);
                  var result = board.setPosition(this, position - 1);

                  if (result == Result.TRY_AGAIN)
                      commitTurn(board); // Try again!
              } catch (Exception e) {
                  e.printStackTrace();
                  // Handle exceptions appropriately
              }
          } catch (Exception e) {
              e.printStackTrace();
          }
        });
    }

    @Override
    public String getName() {
        return name;
    }
}
